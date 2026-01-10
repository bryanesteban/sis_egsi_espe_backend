# 🐳 Guía de Dockerización - SIEGSI Backend

## Prerequisitos

- Docker instalado (versión 20.10+)
- Docker Compose instalado (versión 2.0+)

## 📁 Archivos Docker Creados

- `Dockerfile` - Imagen multi-stage del backend
- `docker-compose.yml` - Orquestación de servicios
- `.dockerignore` - Archivos excluidos del build
- `application-docker.properties` - Configuración para Docker

## 🚀 Comandos Básicos

### Iniciar todos los servicios

```bash
docker-compose up -d
```

### Ver logs en tiempo real

```bash
# Todos los servicios
docker-compose logs -f

# Solo backend
docker-compose logs -f backend

# Solo base de datos
docker-compose logs -f mysql-db
```

### Detener servicios

```bash
docker-compose down
```

### Detener y eliminar volúmenes (⚠️ borra los datos)

```bash
docker-compose down -v
```

### Reconstruir imágenes

```bash
docker-compose up -d --build
```

### Ver estado de servicios

```bash
docker-compose ps
```

## 🔧 Configuración

### Puertos Expuestos

- **Backend**: `9090` → `http://localhost:9090`
- **MySQL**: `3307` → `localhost:3307` (para evitar conflictos con MySQL local)

### Acceso a MySQL desde host

```bash
mysql -h 127.0.0.1 -P 3307 -u usuario -psa12345 db_egsi
```

O con Docker:

```bash
docker-compose exec mysql-db mysql -u usuario -psa12345 db_egsi
```

### Variables de Entorno

Puedes modificar las variables en `docker-compose.yml`:

- `DB_HOST`, `DB_PORT`, `DB_NAME` - Configuración de base de datos
- `DB_USERNAME`, `DB_PASSWORD` - Credenciales
- `JWT_SECRET` - Clave secreta JWT (⚠️ cambiar en producción)
- `CORS_ALLOWED_ORIGINS` - Orígenes permitidos para CORS

## 📊 Base de Datos

### Inicialización Automática

El script `SIEGSI_MYSQL.sql` se ejecuta automáticamente la primera vez que se crea el contenedor MySQL, creando todas las tablas necesarias.

### Persistencia

Los datos de MySQL se almacenan en el volumen Docker `mysql_data`, por lo que persisten entre reinicios del contenedor.

### Backup Manual

```bash
# Exportar base de datos
docker-compose exec mysql-db mysqldump -u usuario -psa12345 db_egsi > backup.sql

# Importar base de datos
docker-compose exec -T mysql-db mysql -u usuario -psa12345 db_egsi < backup.sql
```

## 🔍 Troubleshooting

### Ver estado de salud (health checks)

```bash
docker inspect siegsi-backend | grep -A 10 Health
docker inspect siegsi-mysql | grep -A 10 Health
```

### Entrar al contenedor del backend

```bash
docker-compose exec backend sh
```

### Entrar al contenedor de MySQL

```bash
docker-compose exec mysql-db bash
```

### Reiniciar solo un servicio

```bash
docker-compose restart backend
```

### Ver uso de recursos

```bash
docker stats siegsi-backend siegsi-mysql
```

## 🏗️ Desarrollo

### Build local sin Docker Compose

```bash
# Construir imagen
docker build -t siegsi-backend:latest .

# Ejecutar contenedor
docker run -p 9090:9090 \
  -e DB_HOST=host.docker.internal \
  -e DB_PORT=3306 \
  siegsi-backend:latest
```

### Modo desarrollo con hot-reload

Para desarrollo, es mejor ejecutar Spring Boot directamente sin Docker:

```bash
./mvnw spring-boot:run
```

## 🔒 Producción

### Cambios recomendados para producción:

1. **Cambiar JWT_SECRET** en `docker-compose.yml`
2. **Usar variables de entorno externas** (archivo `.env`)
3. **Configurar HTTPS** con nginx/traefik
4. **Limitar recursos** de contenedores
5. **Usar imágenes específicas** (no `latest`)
6. **Backup automático** de base de datos
7. **Revisar headers de seguridad** (ver [SECURITY-HEADERS.md](SECURITY-HEADERS.md))

### Headers de Seguridad Implementados

✅ **X-XSS-Protection**: Protección contra Cross-Site Scripting  
✅ **X-Frame-Options**: Prevención de Clickjacking  
✅ **Content-Security-Policy**: Política de seguridad de contenido  
✅ **Referrer-Policy**: Control de información del referrer  
✅ **Cache-Control**: Gestión de caché para datos sensibles

**Documentación completa**: Ver [SECURITY-HEADERS.md](SECURITY-HEADERS.md)

### Ejemplo de archivo `.env`:

```env
DB_PASSWORD=password_seguro_produccion
JWT_SECRET=clave_jwt_muy_segura_y_larga_para_produccion
CORS_ALLOWED_ORIGINS=https://mi-dominio.com
```

Luego usar en `docker-compose.yml`:

```yaml
environment:
  DB_PASSWORD: ${DB_PASSWORD}
  JWT_SECRET: ${JWT_SECRET}
```

## 📝 Notas

- El backend espera a que MySQL esté completamente listo (healthcheck)
- Los logs de SQL están activados (`show-sql=true`)
- La zona horaria está configurada a `America/Guayaquil`
- El contenedor ejecuta la aplicación con usuario no-root por seguridad
