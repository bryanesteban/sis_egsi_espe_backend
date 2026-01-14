# API de Fases EGSI - Documentación v3

## Base URL
```
http://localhost:9090/api/v3/egsi/phases
```

## Endpoints Disponibles

### 1. Obtener todas las fases
**GET** `/api/v3/egsi/phases`

Retorna todas las fases EGSI con sus secciones y preguntas.

**Response:**
```json
{
  "totalPhases": 5,
  "activePhases": 5,
  "totalSections": 5,
  "totalQuestions": 9,
  "egsiPhases": [
    {
      "idPhase": "phase-001",
      "title": "Fase 1: Diagnóstico Inicial",
      "description": "Evaluación del estado actual de la seguridad de la información en la institución.",
      "order": 1,
      "isActive": true,
      "sections": [
        {
          "idSection": "sec-001",
          "title": "1.1 Información General de la Institución",
          "description": "Complete la información básica de identificación institucional.",
          "order": 1,
          "questions": [
            {
              "idQuestion": "q-001",
              "title": "Nombre de la Institución",
              "description": "Ingrese el nombre oficial completo de la institución.",
              "inputType": "TEXTO",
              "required": true,
              "placeholder": "Ej: Universidad de las Fuerzas Armadas ESPE",
              "maxLength": 200,
              "order": 1
            },
            {
              "idQuestion": "q-002",
              "title": "Fecha de Inicio del Diagnóstico",
              "description": "Seleccione la fecha en que inicia el proceso de diagnóstico.",
              "inputType": "DATE",
              "required": true,
              "order": 2
            },
            {
              "idQuestion": "q-005",
              "title": "Ubicaciones Físicas",
              "description": "Liste todas las ubicaciones físicas que están dentro del alcance del SGSI.",
              "inputType": "TABLA",
              "required": true,
              "tableConfig": {
                "columns": [
                  { "key": "ubicacion", "header": "Ubicación", "width": "30%" },
                  { "key": "direccion", "header": "Dirección", "width": "40%" },
                  { "key": "responsable", "header": "Responsable", "width": "30%" }
                ],
                "minRows": 1,
                "maxRows": 20
              },
              "order": 3
            }
          ]
        }
      ]
    }
  ]
}
```

---

### 2. Obtener solo fases activas
**GET** `/api/v3/egsi/phases/active`

Retorna solo las fases activas (para uso en procesos de implementación).

---

### 3. Obtener fase por ID
**GET** `/api/v3/egsi/phases/{idPhase}`

**Response:**
```json
{
  "idPhase": "phase-001",
  "title": "Fase 1: Diagnóstico Inicial",
  "description": "...",
  "order": 1,
  "isActive": true,
  "sections": [...]
}
```

---

### 4. Obtener estadísticas
**GET** `/api/v3/egsi/phases/statistics`

**Response:**
```json
{
  "totalPhases": 5,
  "activePhases": 5,
  "totalSections": 5,
  "totalQuestions": 9
}
```

---

### 5. Crear nueva fase
**POST** `/api/v3/egsi/phases`

**Headers:**
```
Authorization: Bearer {token}
Content-Type: application/json
```

**Body:**
```json
{
  "title": "Fase 6: Nueva Fase",
  "description": "Descripción de la nueva fase",
  "order": 6,
  "isActive": true,
  "sections": [
    {
      "title": "6.1 Primera Sección",
      "description": "Descripción de la sección",
      "order": 1,
      "questions": [
        {
          "title": "Primera pregunta",
          "description": "Instrucciones para el usuario",
          "inputType": "TEXTO",
          "required": true,
          "placeholder": "Ingrese su respuesta",
          "maxLength": 1000,
          "order": 1
        }
      ]
    }
  ]
}
```

---

### 6. Guardar todas las fases (REEMPLAZAR)
**POST** `/api/v3/egsi/phases/save-all`

Este endpoint elimina todas las fases existentes y las reemplaza con las nuevas.

**Headers:**
```
Authorization: Bearer {token}
Content-Type: application/json
```

**Body:**
```json
{
  "phases": [
    {
      "title": "Fase 1: Diagnóstico Inicial",
      "description": "...",
      "order": 1,
      "isActive": true,
      "sections": [...]
    },
    {
      "title": "Fase 2: Análisis de Riesgos",
      "description": "...",
      "order": 2,
      "isActive": true,
      "sections": [...]
    }
  ]
}
```

---

### 7. Actualizar fase
**PUT** `/api/v3/egsi/phases/{idPhase}`

**Body:** (mismo formato que crear)

---

### 8. Activar/Desactivar fase
**PATCH** `/api/v3/egsi/phases/{idPhase}/toggle-active`

**Response:**
```json
{
  "idPhase": "phase-001",
  "title": "...",
  "isActive": false,
  ...
}
```

---

### 9. Eliminar fase
**DELETE** `/api/v3/egsi/phases/{idPhase}`

**Response:**
```json
{
  "message": "Fase eliminada exitosamente",
  "idPhase": "phase-001"
}
```

---

### 10. Agregar sección a fase
**POST** `/api/v3/egsi/phases/{idPhase}/sections`

**Body:**
```json
{
  "title": "Nueva Sección",
  "description": "Descripción",
  "order": 1,
  "questions": []
}
```

---

### 11. Agregar pregunta a sección
**POST** `/api/v3/egsi/phases/sections/{idSection}/questions`

**Body:**
```json
{
  "title": "Nueva pregunta",
  "description": "Instrucciones",
  "inputType": "TEXTO",
  "required": true,
  "placeholder": "Placeholder",
  "maxLength": 1000,
  "order": 1
}
```

---

### 12. Eliminar sección
**DELETE** `/api/v3/egsi/phases/sections/{idSection}`

---

### 13. Eliminar pregunta
**DELETE** `/api/v3/egsi/phases/questions/{idQuestion}`

---

## Tipos de Input Soportados

| Tipo    | Descripción                    | Campos adicionales                     |
|---------|--------------------------------|---------------------------------------|
| TEXTO   | Campo de texto libre           | placeholder, maxLength                |
| DATE    | Selector de fecha              | -                                     |
| TABLA   | Tabla dinámica con filas       | tableConfig (columns, minRows, maxRows)|

---

## Estructura de tableConfig
```json
{
  "columns": [
    { "key": "nombre", "header": "Nombre", "width": "30%" },
    { "key": "valor", "header": "Valor", "width": "70%" }
  ],
  "minRows": 1,
  "maxRows": 50
}
```

---

## Roles Requeridos

| Endpoint                     | Roles permitidos |
|------------------------------|------------------|
| GET (todos)                  | Cualquier usuario autenticado |
| POST, PUT, PATCH, DELETE     | ADMIN            |

---

## Ejemplo de uso en Frontend

```typescript
import { egsiPhasesAPI } from '@/lib/api';

// Obtener todas las fases
const response = await egsiPhasesAPI.getAll();
console.log(response.egsiPhases);

// Guardar todas las fases
const phases = convertToAPI(localPhases);
await egsiPhasesAPI.saveAll({ phases });

// Toggle fase activa
await egsiPhasesAPI.toggleActive('phase-001');
```

---

## Tablas de Base de Datos

### EGSI_PHASE
| Campo       | Tipo         | Descripción                |
|-------------|--------------|----------------------------|
| ID_PHASE    | VARCHAR(36)  | UUID de la fase            |
| TITLE       | VARCHAR(200) | Título de la fase          |
| DESCRIPTION | VARCHAR(1000)| Descripción                |
| PHASE_ORDER | INT          | Orden de la fase           |
| IS_ACTIVE   | BOOLEAN      | Si está activa             |
| CREATED_AT  | TIMESTAMP    | Fecha de creación          |
| UPDATED_AT  | TIMESTAMP    | Fecha de actualización     |

### EGSI_SECTION
| Campo        | Tipo         | Descripción                |
|--------------|--------------|----------------------------|
| ID_SECTION   | VARCHAR(36)  | UUID de la sección         |
| ID_PHASE     | VARCHAR(36)  | FK a EGSI_PHASE            |
| TITLE        | VARCHAR(200) | Título de la sección       |
| DESCRIPTION  | VARCHAR(1000)| Descripción                |
| SECTION_ORDER| INT          | Orden dentro de la fase    |

### EGSI_QUESTION
| Campo        | Tipo         | Descripción                |
|--------------|--------------|----------------------------|
| ID_QUESTION  | VARCHAR(36)  | UUID de la pregunta        |
| ID_SECTION   | VARCHAR(36)  | FK a EGSI_SECTION          |
| TITLE        | VARCHAR(500) | Título de la pregunta      |
| DESCRIPTION  | VARCHAR(1000)| Descripción/Instrucciones  |
| INPUT_TYPE   | VARCHAR(20)  | TEXTO, DATE, TABLA         |
| IS_REQUIRED  | BOOLEAN      | Si es obligatoria          |
| PLACEHOLDER  | VARCHAR(500) | Placeholder para TEXTO     |
| MAX_LENGTH   | INT          | Longitud máxima para TEXTO |
| TABLE_CONFIG | JSON         | Configuración para TABLA   |
| QUESTION_ORDER| INT         | Orden dentro de la sección |
