# API REST SIEGSI - Versión 3.0

## 📋 Descripción General

API REST completa para el Sistema de Implementación y Evaluación de Gestión de Seguridad de la Información (SIEGSI). La versión 3.0 incluye documentación exhaustiva con Swagger/OpenAPI 3.0 y arquitectura organizada por módulos funcionales.

**Fecha de Release:** 10 de Enero, 2026  
**Versión:** 3.0.0  
**Framework:** Spring Boot 3.5.9  
**Java Version:** 17 LTS  
**Base de Datos:** MySQL 8.0

---

## 🏗️ Arquitectura de la API v3

### Estructura de Paquetes

```
com.espe.ListoEgsi.controller.v3/
├── auth/                      # Autenticación y seguridad
│   ├── LoginController        # Login, registro, cambio credenciales
│   └── HashGeneratorController # Utilidad BCrypt (dev/testing)
├── user/                      # Gestión de usuarios
│   └── UserController         # CRUD completo de usuarios
├── process/                   # Gestión de procesos EGSI
│   └── ProcessEgsiController  # CRUD de procesos de implementación
└── questionary/               # Sistema de cuestionarios
    ├── QuestionaryController  # CRUD de plantillas de cuestionarios
    ├── QuestionController     # CRUD de preguntas
    ├── PhaseController        # CRUD de fases de proceso
    ├── AnswerController       # CRUD de respuestas
    └── ResponsibleSigningController # Gestión de aprobaciones y firmas
```

---

## 🔐 Autenticación

### Método de Autenticación: JWT (Bearer Token)

Todos los endpoints (excepto `/api/v3/auth/login` y `/api/v3/auth/register`) requieren autenticación mediante token JWT.

#### Headers Requeridos:
```http
Authorization: Bearer {JWT_TOKEN}
Content-Type: application/json
```

#### Obtener Token:
```bash
POST /api/v3/auth/login
{
  "username": "admin",
  "password": "admin123"
}
```

#### Respuesta:
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "username": "admin",
  "role": "ADMIN",
  "expiresIn": 86400
}
```

**Duración del Token:** 24 horas  
**Algoritmo:** HS256 (HMAC with SHA-256)

---

## 📚 Módulos de la API

### 1. 🔑 Módulo de Autenticación (`/api/v3/auth`)

| Endpoint | Método | Descripción | Auth |
|----------|--------|-------------|------|
| `/login` | POST | Autenticar usuario y obtener JWT | ❌ |
| `/register` | POST | Registrar nuevo usuario | ❌ |
| `/refresh` | POST | Refrescar token JWT | ❌ |
| `/change-password` | POST | Cambiar contraseña del usuario | ✅ |
| `/change-username` | POST | Cambiar nombre de usuario | ✅ |

#### Utilidad de Desarrollo:

| Endpoint | Método | Descripción | Auth |
|----------|--------|-------------|------|
| `/hash/generate` | POST | Generar hash BCrypt de contraseña | ❌ |
| `/hash/verify` | POST | Verificar contraseña contra hash | ❌ |

---

### 2. 👥 Módulo de Usuarios (`/api/v3/users`)

| Endpoint | Método | Descripción | Auth |
|----------|--------|-------------|------|
| `/` | GET | Listar todos los usuarios | ✅ |
| `/{cedula}` | GET | Obtener usuario por cédula | ✅ |
| `/` | POST | Crear nuevo usuario | ✅ |
| `/{cedula}` | PUT | Actualizar usuario existente | ✅ |
| `/{cedula}` | DELETE | Eliminar usuario (lógico) | ✅ |

**Campos Principales:**
- `cedula` (PK): Identificador único (10 dígitos)
- `username`: Nombre de usuario (único)
- `nombre`, `apellido`: Datos personales
- `email`: Correo electrónico
- `password`: Contraseña encriptada (BCrypt)
- `rol`: ADMIN, USER, EVALUATOR
- `activo`: Estado del usuario

---

### 3. 📊 Módulo de Procesos EGSI (`/api/v3/processes`)

| Endpoint | Método | Descripción | Auth |
|----------|--------|-------------|------|
| `/` | GET | Listar todos los procesos | ✅ |
| `/{idProcess}` | GET | Obtener proceso por ID | ✅ |
| `/` | POST | Crear nuevo proceso | ✅ |
| `/{idProcess}` | PUT | Actualizar proceso existente | ✅ |
| `/{idProcess}` | DELETE | Eliminar proceso (lógico) | ✅ |

**Campos Principales:**
- `idProcess` (PK, UUID): Identificador único
- `name`: Nombre del proceso
- `description`: Descripción detallada
- `dateBegin`, `dateEnd`: Fechas de ejecución
- `currentPhase`: Fase actual del proceso
- `active`: Estado de activación

---

### 4. 📝 Módulo de Cuestionarios (`/api/v3/questionaries`)

#### 4.1 Cuestionarios (Plantillas)

| Endpoint | Método | Descripción | Auth |
|----------|--------|-------------|------|
| `/` | GET | Listar todos los cuestionarios | ✅ |
| `/{idQuestionary}` | GET | Obtener cuestionario por ID | ✅ |
| `/` | POST | Crear nuevo cuestionario | ✅ |
| `/{idQuestionary}` | PUT | Actualizar cuestionario | ✅ |
| `/{idQuestionary}` | DELETE | Eliminar cuestionario (lógico) | ✅ |

**Tipos de Cuestionarios:**
- `INITIAL`: Evaluación inicial
- `FOLLOWUP`: Seguimiento
- `FINAL`: Evaluación final
- `AUDIT`: Auditoría

#### 4.2 Preguntas (`/api/v3/questions`)

| Endpoint | Método | Descripción | Auth |
|----------|--------|-------------|------|
| `/` | GET | Listar todas las preguntas | ✅ |
| `/{idQuestion}` | GET | Obtener pregunta por ID | ✅ |
| `/questionary/{idQuestionary}` | GET | Preguntas por cuestionario | ✅ |
| `/` | POST | Crear nueva pregunta | ✅ |
| `/{idQuestion}` | PUT | Actualizar pregunta | ✅ |
| `/{idQuestion}` | DELETE | Eliminar pregunta | ✅ |

**Tipos de Preguntas:**
- `TEXT`: Respuesta de texto libre
- `YES_NO`: Respuesta binaria (Sí/No)
- `MULTIPLE_CHOICE`: Selección múltiple
- `SCALE`: Escala numérica (1-5, 1-10)
- `DATE`: Selección de fecha

#### 4.3 Fases (`/api/v3/phases`)

| Endpoint | Método | Descripción | Auth |
|----------|--------|-------------|------|
| `/` | GET | Listar todas las fases | ✅ |
| `/{idPhase}` | GET | Obtener fase por ID | ✅ |
| `/process/{idProcess}` | GET | Fases por proceso | ✅ |
| `/` | POST | Crear nueva fase | ✅ |
| `/{idPhase}` | PUT | Actualizar fase | ✅ |
| `/{idPhase}` | DELETE | Eliminar fase (lógico) | ✅ |

**Campos Principales:**
- `idPhase` (PK, UUID): Identificador único
- `name`: Nombre de la fase
- `description`: Descripción
- `startDate`, `endDate`: Fechas planificadas
- `idProcess`: Proceso asociado
- `order`: Orden de ejecución
- `completed`: Estado de completitud

#### 4.4 Respuestas (`/api/v3/answers`)

| Endpoint | Método | Descripción | Auth |
|----------|--------|-------------|------|
| `/` | GET | Listar todas las respuestas | ✅ |
| `/{idAnswer}` | GET | Obtener respuesta por ID | ✅ |
| `/phase/{idPhase}` | GET | Respuestas por fase | ✅ |
| `/question/{idQuestion}` | GET | Respuestas por pregunta | ✅ |
| `/` | POST | Crear nueva respuesta | ✅ |
| `/{idAnswer}` | PUT | Actualizar respuesta | ✅ |
| `/{idAnswer}` | DELETE | Eliminar respuesta | ✅ |

**Campos Principales:**
- `idAnswer` (PK, UUID): Identificador único
- `answerText`: Texto de la respuesta
- `idQuestion`: Pregunta respondida
- `idPhase`: Fase del proceso
- `dateAnswer`: Fecha de respuesta
- `evidence`: URL o path de evidencia

#### 4.5 Responsables de Firma (`/api/v3/responsible-signings`)

| Endpoint | Método | Descripción | Auth |
|----------|--------|-------------|------|
| `/` | GET | Listar todos los responsables | ✅ |
| `/{idResponsibleSigning}` | GET | Obtener responsable por ID | ✅ |
| `/phase/{idPhase}` | GET | Responsables por fase | ✅ |
| `/` | POST | Asignar nuevo responsable | ✅ |
| `/{idResponsibleSigning}` | PUT | Actualizar responsable/firma | ✅ |
| `/{idResponsibleSigning}` | DELETE | Eliminar responsable | ✅ |

**Estados de Firma:**
- `PENDING`: Pendiente de firma
- `SIGNED`: Firmado y aprobado
- `REJECTED`: Rechazado con observaciones
- `DELEGATED`: Delegado a otro responsable

**Campos Principales:**
- `idResponsibleSigning` (PK, UUID): Identificador único
- `name`: Nombre del responsable
- `position`: Cargo
- `idPhase`: Fase asignada
- `email`: Correo electrónico
- `status`: Estado de la firma
- `signatureDate`: Fecha de firma
- `digitalSignature`: Firma digital

---

## 🔍 Códigos de Respuesta HTTP

| Código | Significado | Descripción |
|--------|-------------|-------------|
| 200 | OK | Operación exitosa |
| 201 | Created | Recurso creado exitosamente |
| 400 | Bad Request | Datos de entrada inválidos |
| 401 | Unauthorized | No autenticado o token inválido |
| 403 | Forbidden | Sin permisos suficientes |
| 404 | Not Found | Recurso no encontrado |
| 409 | Conflict | Conflicto (ej: registro duplicado) |
| 500 | Internal Server Error | Error interno del servidor |

---

## 📖 Documentación Swagger

### Acceso a Swagger UI

Una vez iniciado el servidor, la documentación interactiva está disponible en:

```
http://localhost:9090/swagger-ui.html
```

### Características de Swagger en v3:

✅ **Documentación completa** de todos los endpoints  
✅ **Ejemplos de request/response** para cada operación  
✅ **Esquemas de validación** detallados  
✅ **Pruebas interactivas** desde el navegador  
✅ **Autenticación JWT** integrada en la UI  
✅ **Descripciones extensas** con casos de uso  
✅ **Códigos de respuesta** documentados  
✅ **Modelos de datos** con todas las propiedades

### Autenticación en Swagger:

1. Ejecutar POST `/api/v3/auth/login` con credenciales
2. Copiar el token JWT de la respuesta
3. Hacer clic en el botón "Authorize" (🔒)
4. Ingresar: `Bearer {TOKEN}`
5. Hacer clic en "Authorize"
6. ¡Todos los endpoints protegidos ahora son accesibles!

---

## 🚀 Ejemplos de Uso

### 1. Crear un Proceso Completo

#### Paso 1: Login
```bash
curl -X POST http://localhost:9090/api/v3/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "admin",
    "password": "admin123"
  }'
```

#### Paso 2: Crear Proceso
```bash
curl -X POST http://localhost:9090/api/v3/processes \
  -H "Authorization: Bearer {TOKEN}" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Implementación EGSI 2026",
    "description": "Proceso completo de implementación",
    "dateBegin": "2026-01-10",
    "dateEnd": "2026-12-31",
    "currentPhase": "Fase 1",
    "active": "true"
  }'
```

#### Paso 3: Crear Fase
```bash
curl -X POST http://localhost:9090/api/v3/phases \
  -H "Authorization: Bearer {TOKEN}" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Evaluación Inicial",
    "description": "Primera fase de evaluación",
    "startDate": "2026-01-10",
    "endDate": "2026-03-31",
    "idProcess": "{PROCESS_UUID}",
    "order": 1
  }'
```

#### Paso 4: Crear Cuestionario
```bash
curl -X POST http://localhost:9090/api/v3/questionaries \
  -H "Authorization: Bearer {TOKEN}" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Cuestionario de Evaluación Inicial",
    "description": "Evaluación del estado actual",
    "type": "INITIAL",
    "active": "true"
  }'
```

#### Paso 5: Crear Pregunta
```bash
curl -X POST http://localhost:9090/api/v3/questions \
  -H "Authorization: Bearer {TOKEN}" \
  -H "Content-Type: application/json" \
  -d '{
    "text": "¿Cuenta con política de seguridad?",
    "type": "YES_NO",
    "idQuestionary": "{QUESTIONARY_UUID}",
    "order": 1,
    "required": true
  }'
```

#### Paso 6: Registrar Respuesta
```bash
curl -X POST http://localhost:9090/api/v3/answers \
  -H "Authorization: Bearer {TOKEN}" \
  -H "Content-Type: application/json" \
  -d '{
    "answerText": "Sí, contamos con ISO 27001",
    "idQuestion": "{QUESTION_UUID}",
    "idPhase": "{PHASE_UUID}",
    "dateAnswer": "2026-01-10",
    "evidence": "/uploads/iso27001.pdf"
  }'
```

#### Paso 7: Asignar Responsable de Firma
```bash
curl -X POST http://localhost:9090/api/v3/responsible-signings \
  -H "Authorization: Bearer {TOKEN}" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Juan Pérez",
    "position": "Director de TI",
    "idPhase": "{PHASE_UUID}",
    "email": "juan.perez@example.com",
    "order": 1,
    "required": true
  }'
```

---

## 🛠️ Mejoras en la Versión 3.0

### ✨ Nuevas Características

1. **Documentación Swagger Completa**
   - Descripciones detalladas de cada endpoint
   - Ejemplos de request/response
   - Esquemas de validación
   - Casos de uso documentados

2. **Arquitectura Organizada**
   - Estructura modular por funcionalidad
   - Paquetes claramente definidos
   - Separación de responsabilidades

3. **Mejoras en Seguridad**
   - Autenticación JWT robusta
   - Validaciones exhaustivas
   - Auditoría de cambios
   - Control de permisos por rol

4. **Manejo de Errores Mejorado**
   - Respuestas de error consistentes
   - Mensajes descriptivos
   - Códigos HTTP apropiados
   - Logging detallado

5. **Validaciones Reforzadas**
   - Bean Validation en DTOs
   - Validaciones de negocio
   - Verificación de integridad referencial
   - Prevención de duplicados

---

## 📝 Notas de Migración

### Desde versión 2.x a 3.0

#### Cambios en Rutas:
- **Antes:** `/login` → **Ahora:** `/api/v3/auth/login`
- **Antes:** `/users` → **Ahora:** `/api/v3/users`
- **Antes:** `/processEgsi` → **Ahora:** `/api/v3/processes`
- **Antes:** `/api/answers` → **Ahora:** `/api/v3/answers`

#### Cambios en Estructuras:
- Todos los IDs ahora son UUID en lugar de Long
- Campos de fecha en formato ISO-8601
- Validaciones más estrictas en campos

#### Retrocompatibilidad:
- Los controladores v2 permanecen activos
- Migración gradual recomendada
- Endpoints v2 deprecados pero funcionales

---

## 🔧 Configuración

### application.properties (Docker)
```properties
# Server Configuration
server.port=9090

# Database Configuration
spring.datasource.url=jdbc:mysql://mysql-db:3306/db_egsi
spring.datasource.username=usuario
spring.datasource.password=sa12345

# JWT Configuration
jwt.secret=your-secret-key-here
jwt.expiration=86400000

# Swagger Configuration
springdoc.api-docs.path=/api-docs
springdoc.swagger-ui.path=/swagger-ui.html
```

---

## 👥 Roles y Permisos

| Rol | Descripción | Permisos |
|-----|-------------|----------|
| **ADMIN** | Administrador del sistema | Acceso completo a todas las operaciones |
| **USER** | Usuario estándar | Lectura y creación limitada |
| **EVALUATOR** | Evaluador de procesos | Gestión de respuestas y cuestionarios |

---

## 📞 Soporte

Para preguntas, reportes de bugs o solicitudes de características:

- **Documentación Swagger:** http://localhost:9090/swagger-ui.html
- **API Docs JSON:** http://localhost:9090/api-docs
- **Repositorio:** [GitHub Repository]
- **Email:** soporte@siegsi.espe.edu.ec

---

## 📄 Licencia

Copyright © 2026 ESPE - Escuela Politécnica del Ejército  
Sistema SIEGSI - Todos los derechos reservados

---

**Última Actualización:** 10 de Enero, 2026  
**Versión API:** 3.0.0  
**Estado:** ✅ Producción
