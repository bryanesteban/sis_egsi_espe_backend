#!/bin/bash

# Script de prueba para verificar el deployment Docker
# Autor: Sistema SIEGSI

set -e

# Colores
GREEN='\033[0;32m'
RED='\033[0;31m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

echo -e "${BLUE}🧪 Probando deployment Docker de SIEGSI${NC}"
echo "========================================"
echo ""

# Función para verificar
check() {
    local name=$1
    local command=$2
    
    echo -n "   $name: "
    if eval "$command" > /dev/null 2>&1; then
        echo -e "${GREEN}✓ OK${NC}"
        return 0
    else
        echo -e "${RED}✗ FAIL${NC}"
        return 1
    fi
}

# 1. Verificar Docker
echo -e "${YELLOW}1. Verificando Docker...${NC}"
check "Docker daemon" "docker info"
check "Docker Compose" "docker-compose --version"
echo ""

# 2. Verificar contenedores
echo -e "${YELLOW}2. Verificando contenedores...${NC}"
check "MySQL container" "docker ps | grep siegsi-mysql"
check "Backend container" "docker ps | grep siegsi-backend"
echo ""

# 3. Verificar conectividad de red
echo -e "${YELLOW}3. Verificando conectividad...${NC}"
check "MySQL port 3307" "nc -z localhost 3307"
check "Backend port 9090" "nc -z localhost 9090"
echo ""

# 4. Verificar health checks
echo -e "${YELLOW}4. Verificando health checks...${NC}"

# MySQL
echo -n "   MySQL health: "
if docker-compose exec -T mysql-db mysqladmin ping -h localhost -u usuario -psa12345 --silent 2>/dev/null; then
    echo -e "${GREEN}✓ OK${NC}"
else
    echo -e "${RED}✗ FAIL${NC}"
fi

# Backend
echo -n "   Backend health: "
HTTP_CODE=$(curl -s -o /dev/null -w "%{http_code}" http://localhost:9090/actuator/health 2>/dev/null || echo "000")
if [ "$HTTP_CODE" = "200" ]; then
    echo -e "${GREEN}✓ OK (HTTP $HTTP_CODE)${NC}"
else
    echo -e "${RED}✗ FAIL (HTTP $HTTP_CODE)${NC}"
fi
echo ""

# 5. Verificar base de datos
echo -e "${YELLOW}5. Verificando base de datos...${NC}"
echo -n "   Database exists: "
if docker-compose exec -T mysql-db mysql -u usuario -psa12345 -e "USE db_egsi; SHOW TABLES;" 2>/dev/null | grep -q "USERS"; then
    echo -e "${GREEN}✓ OK${NC}"
    
    # Contar tablas
    TABLES=$(docker-compose exec -T mysql-db mysql -u usuario -psa12345 -e "USE db_egsi; SHOW TABLES;" 2>/dev/null | wc -l)
    echo "   Tablas encontradas: $((TABLES - 1))"
else
    echo -e "${RED}✗ FAIL${NC}"
fi
echo ""

# 6. Test de API
echo -e "${YELLOW}6. Probando endpoints de API...${NC}"

# Health endpoint
echo -n "   GET /actuator/health: "
HEALTH=$(curl -s http://localhost:9090/actuator/health)
if echo "$HEALTH" | grep -q "UP"; then
    echo -e "${GREEN}✓ OK${NC}"
else
    echo -e "${RED}✗ FAIL${NC}"
fi

# Login endpoint (esperamos 401 o 400 sin credenciales válidas)
echo -n "   POST /login: "
HTTP_CODE=$(curl -s -o /dev/null -w "%{http_code}" -X POST http://localhost:9090/login \
    -H "Content-Type: application/json" \
    -d '{"username":"test","password":"test"}' 2>/dev/null || echo "000")
if [ "$HTTP_CODE" = "200" ] || [ "$HTTP_CODE" = "400" ] || [ "$HTTP_CODE" = "401" ]; then
    echo -e "${GREEN}✓ OK (HTTP $HTTP_CODE - endpoint responde)${NC}"
else
    echo -e "${YELLOW}⚠ WARN (HTTP $HTTP_CODE)${NC}"
fi
echo ""

# 7. Verificar logs
echo -e "${YELLOW}7. Últimas líneas de logs...${NC}"
echo -e "${BLUE}   Backend:${NC}"
docker-compose logs --tail=3 backend | sed 's/^/      /'
echo ""
echo -e "${BLUE}   MySQL:${NC}"
docker-compose logs --tail=3 mysql-db | sed 's/^/      /'
echo ""

# 8. Resumen
echo -e "${YELLOW}8. Resumen de recursos...${NC}"
docker stats --no-stream --format "table {{.Container}}\t{{.CPUPerc}}\t{{.MemUsage}}" siegsi-backend siegsi-mysql
echo ""

# Resultado final
echo -e "${GREEN}✨ Pruebas completadas${NC}"
echo ""
echo "📝 Para más detalles:"
echo "   docker-compose logs -f"
echo "   docker-compose ps"
echo ""
