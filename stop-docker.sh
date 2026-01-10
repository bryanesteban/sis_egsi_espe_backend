#!/bin/bash

# Script para detener el proyecto dockerizado SIEGSI

set -e

echo "🛑 Deteniendo SIEGSI..."
echo "======================"
echo ""

# Colores
YELLOW='\033[1;33m'
GREEN='\033[0;32m'
NC='\033[0m'

echo -e "${YELLOW}Deteniendo contenedores...${NC}"
docker-compose down

echo ""
echo -e "${GREEN}✅ Servicios detenidos${NC}"
echo ""
echo "💡 Opciones:"
echo "   Para eliminar también los volúmenes (datos): docker-compose down -v"
echo "   Para reiniciar: ./start-docker.sh"
echo ""
