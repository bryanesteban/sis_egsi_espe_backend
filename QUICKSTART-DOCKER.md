# 🚀 Inicio Rápido - Docker

## ⚡ Comandos Básicos

### Iniciar todo
```bash
./start-docker.sh
```

O manualmente:
```bash
docker-compose up -d
```

### Detener todo
```bash
./stop-docker.sh
```

O manualmente:
```bash
docker-compose down
```

### Ver logs
```bash
# Todos los servicios
docker-compose logs -f

# Solo backend
docker-compose logs -f backend

# Solo MySQL
docker-compose logs -f mysql-db
```

## 🔗 URLs de Acceso

- **API Backend**: http://localhost:9090
- **Health Check**: http://localhost:9090/actuator/health
- **Swagger UI**: http://localhost:9090/swagger-ui.html
- **MySQL**: `localhost:3307`

## 🗄️ Conexión MySQL

### Desde línea de comandos:
```bash
mysql -h 127.0.0.1 -P 3307 -u usuario -psa12345 db_egsi
```

### Desde Docker:
```bash
docker-compose exec mysql-db mysql -u usuario -psa12345 db_egsi
```

### DBeaver/MySQL Workbench:
- Host: `127.0.0.1`
- Puerto: `3307`
- Database: `db_egsi`
- Usuario: `usuario`
- Password: `sa12345`

## 🧪 Probar API

```bash
# Test de login
curl -X POST http://localhost:9090/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "admin",
    "password": "password"
  }'
```

## 🛠️ Comandos Útiles

```bash
# Reconstruir imágenes
docker-compose up -d --build

# Ver estado
docker-compose ps

# Reiniciar un servicio
docker-compose restart backend

# Ver recursos usados
docker stats siegsi-backend siegsi-mysql

# Entrar al contenedor
docker-compose exec backend sh
docker-compose exec mysql-db bash

# Limpiar todo (⚠️ borra datos)
docker-compose down -v
```

## 📋 Primera Ejecución

1. El script SQL se ejecuta automáticamente
2. Espera 30-60 segundos para que todo esté listo
3. Verifica con: `curl http://localhost:9090/actuator/health`
4. ¡Listo para usar! 🎉
