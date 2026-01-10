# 🐳 Arquitectura Docker - SIEGSI

```
┌─────────────────────────────────────────────────────────────┐
│                      HOST MACHINE                            │
│                                                              │
│  ┌────────────────────────────────────────────────────┐    │
│  │         Docker Network: siegsi-network              │    │
│  │                                                      │    │
│  │  ┌──────────────────┐      ┌──────────────────┐   │    │
│  │  │  MySQL Container │      │ Backend Container│   │    │
│  │  │  siegsi-mysql    │      │  siegsi-backend  │   │    │
│  │  │                  │      │                  │   │    │
│  │  │  MySQL 8.0       │◄────►│  Spring Boot    │   │    │
│  │  │  Port: 3306      │      │  Java 17         │   │    │
│  │  │                  │      │  Port: 9090      │   │    │
│  │  │  Database:       │      │                  │   │    │
│  │  │   db_egsi        │      │  WAR: app.war    │   │    │
│  │  │                  │      │                  │   │    │
│  │  │  Volume:         │      │  Health check:   │   │    │
│  │  │   mysql_data     │      │   /actuator      │   │    │
│  │  └────────┬─────────┘      └────────┬─────────┘   │    │
│  │           │                         │              │    │
│  └───────────┼─────────────────────────┼──────────────┘    │
│              │                         │                   │
│         Port 3307                 Port 9090                │
│              │                         │                   │
│              ▼                         ▼                   │
│      localhost:3307           localhost:9090               │
│                                                             │
│  Acceso externo:                                           │
│  - MySQL: mysql -h 127.0.0.1 -P 3307 -u usuario -p        │
│  - API:   http://localhost:9090                           │
│  - Health: http://localhost:9090/actuator/health          │
└─────────────────────────────────────────────────────────────┘
```

## 📁 Archivos Creados

```
sis_egsi_espe_backend/
├── 🐳 Dockerfile                    # Imagen multi-stage del backend
├── 🐳 docker-compose.yml            # Orquestación de servicios
├── 🐳 .dockerignore                 # Archivos excluidos del build
├── 🔧 .env.example                  # Variables de entorno ejemplo
├── 🚀 start-docker.sh               # Script de inicio
├── 🛑 stop-docker.sh                # Script de detención
├── 📖 README-DOCKER.md              # Guía completa
├── 📖 QUICKSTART-DOCKER.md          # Guía rápida
└── src/main/resources/
    └── 🔧 application-docker.properties  # Config para Docker
```

## 🔄 Flujo de Ejecución

1. **Inicio**: `docker-compose up -d`
2. **MySQL**: Se inicia y ejecuta `SIEGSI_MYSQL.sql`
3. **Health Check**: MySQL verifica que está listo
4. **Backend**: Espera a MySQL y luego inicia
5. **Listo**: Sistema funcional en ~60 segundos

## 🎯 Características

### Backend (Spring Boot)
- ✅ Build multi-stage (Maven + Java 17)
- ✅ Imagen optimizada con Alpine Linux
- ✅ Usuario no-root para seguridad
- ✅ Health checks configurados
- ✅ Variables de entorno externalizadas
- ✅ Perfil Docker específico
- ✅ Spring Boot Actuator habilitado

### Base de Datos (MySQL 8)
- ✅ Inicialización automática con script SQL
- ✅ Persistencia con volúmenes Docker
- ✅ Health checks nativos
- ✅ Puerto 3307 para evitar conflictos
- ✅ Credenciales configurables

### Red y Comunicación
- ✅ Red Docker privada
- ✅ CORS configurado
- ✅ Comunicación inter-contenedor por nombres
- ✅ Puertos mapeados al host

## 🔐 Seguridad

- ✅ JWT secret externalizado
- ✅ Contraseñas vía variables de entorno
- ✅ Ejecución con usuario no-root
- ✅ Health checks para monitoreo
- ⚠️ Para producción: cambiar secrets

## 📊 Monitoreo

```bash
# Logs en tiempo real
docker-compose logs -f

# Estado de contenedores
docker-compose ps

# Uso de recursos
docker stats siegsi-backend siegsi-mysql

# Health status
curl http://localhost:9090/actuator/health
```

## 🔧 Mantenimiento

```bash
# Backup de BD
docker-compose exec mysql-db mysqldump -u usuario -psa12345 db_egsi > backup.sql

# Restaurar BD
docker-compose exec -T mysql-db mysql -u usuario -psa12345 db_egsi < backup.sql

# Limpiar volúmenes
docker-compose down -v

# Reconstruir
docker-compose up -d --build
```

## 🚀 Próximos Pasos

1. ✅ Backend y BD dockerizados
2. 🔜 Dockerizar frontend Next.js
3. 🔜 Nginx como reverse proxy
4. 🔜 Docker Compose completo (full stack)
5. 🔜 CI/CD pipeline
6. 🔜 Kubernetes manifests (opcional)
