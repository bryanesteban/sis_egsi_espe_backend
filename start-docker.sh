#!/bin/bash

# Script para iniciar el proyecto dockerizado SIEGSI
# Autor: Sistema SIEGSI
# Fecha: Enero 2026

set -e

echo "🐳 Iniciando SIEGSI con Docker..."
echo "=================================="
echo ""

# Colores para output
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
RED='\033[0;31m'
NC='\033[0m' # No Color

# Verificar que Docker está corriendo
if ! docker info > /dev/null 2>&1; then
    echo -e "${RED}❌ Error: Docker no está corriendo${NC}"
    echo "Por favor, inicia Docker Desktop o el daemon de Docker"
    exit 1
fi

# Verificar que docker-compose está instalado
if ! command -v docker-compose &> /dev/null; then
    echo -e "${RED}❌ Error: docker-compose no está instalado${NC}"
    exit 1
fi

echo -e "${YELLOW}📦 Construyendo imágenes...${NC}"
docker-compose build

echo ""
echo -e "${YELLOW}🚀 Iniciando servicios...${NC}"
docker-compose up -d

echo ""
echo -e "${GREEN}✅ Servicios iniciados${NC}"
echo ""
echo "📊 Estado de los contenedores:"
docker-compose ps

echo ""
echo "🔍 Esperando a que los servicios estén listos..."
echo "   (Esto puede tomar 30-60 segundos)"
sleep 10

# Esperar a MySQL
echo -n "   MySQL: "
for i in {1..30}; do
    if docker-compose exec -T mysql-db mysqladmin ping -h localhost -u usuario -psa12345 --silent 2>/dev/null; then
        echo -e "${GREEN}✓ Listo${NC}"
        break
    fi
    echo -n "."
    sleep 2
    if [ $i -eq 30 ]; then
        echo -e "${YELLOW}⚠ Tiempo de espera agotado${NC}"
    fi
done

# Esperar al Backend
echo -n "   Backend: "
for i in {1..30}; do
    if curl -s http://localhost:9090/actuator/health > /dev/null 2>&1 || curl -s http://localhost:9090 > /dev/null 2>&1; then
        echo -e "${GREEN}✓ Listo${NC}"
        break
    fi
    echo -n "."
    sleep 2
    if [ $i -eq 30 ]; then
        echo -e "${YELLOW}⚠ Tiempo de espera agotado${NC}"
    fi
done

echo ""
echo -e "${GREEN}✨ ¡Sistema listo!${NC}"
echo ""
echo "📌 URLs de acceso:"
echo "   Backend API: http://localhost:9090"
echo "   MySQL:       localhost:3307"
echo ""
echo "📝 Comandos útiles:"
echo "   Ver logs:        docker-compose logs -f"
echo "   Detener:         docker-compose down"
echo "   Reiniciar:       docker-compose restart"
echo ""
