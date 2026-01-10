# 📊 Análisis de Coherencia: Servicios vs Script SQL (v3)

**Fecha de Análisis:** 10/01/2026  
**Script de Referencia:** `src/main/resources/liquidBase/v3/SIEGSI_DB.sql`  
**Base de Datos:** MySQL 5.0+

---

## ⚠️ PROBLEMAS CRÍTICOS IDENTIFICADOS

### 1. 🔴 INCONSISTENCIA EN NOMBRE DE TABLA: PHASES vs PHASE_CUSTOM

**Script v3 (SIEGSI_DB.sql):**
```sql
create table PHASE_CUSTOM
(
   ID_PHASE             VARCHAR(36) DEFAULT (UUID()) not null,
   ID_PROCESS           VARCHAR(36) DEFAULT (UUID()) not null,
   QUESTIONARY_CODE     varchar(50) not null,
   RESPONSIBLES         varchar(500) not null,
   STATUS               varchar(50) not null,
   primary key (ID_PHASE)
);
```

**Entidad JPA actual:**
```java
@Entity
@Table(name = "PHASES")  // ❌ INCORRECTO - Debería ser "PHASE_CUSTOM"
public class Phase {
    // ...
}
```

**⚠️ IMPACTO:** La aplicación intentará acceder a la tabla `PHASES` que NO EXISTE en el script v3.

**✅ SOLUCIÓN:** Cambiar el nombre de la tabla en la entidad Phase:
```java
@Table(name = "PHASE_CUSTOM")
```

---

### 2. 🔴 INCONSISTENCIA EN TIPOS DE DATOS: ID_QUESTION

**Script v3:**
```sql
create table QUESTION
(
   ID_QUESTION          decimal not null,  -- ❌ DECIMAL, no UUID
   ID_QUESTIONARY       varchar(20) not null,
   DESCRIPTION          varchar(1000),
   QUESTION_TYPE        varchar(50),
   QUESTION_JSON        varchar(1000),
   primary key (ID_QUESTION)
);
```

**Entidad JPA actual:**
```java
@Entity
@Table(name = "QUESTION")
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID_QUESTION", nullable = false)
    @JdbcTypeCode(SqlTypes.CHAR)
    private UUID idQuestion;  // ❌ UUID en lugar de DECIMAL/Long
}
```

**⚠️ IMPACTO:** Error de mapeo de tipos. La base de datos espera un DECIMAL pero la aplicación usa UUID.

**✅ SOLUCIÓN:** 
```java
@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
@Column(name = "ID_QUESTION", nullable = false)
private Long idQuestion;  // Cambiar de UUID a Long
```

---

### 3. 🔴 INCONSISTENCIA EN TIPOS DE DATOS: ID_QUESTIONARY

**Script v3:**
```sql
create table QUESTIONARY
(
   ID_QUESTIONARY       varchar(20) not null,  -- ✅ VARCHAR(20)
   QUESTIONARY_NAME     varchar(50) not null,
   DESCRIPTION          varchar(1000) not null,
   PHASE                varchar(10) not null,
   primary key (ID_QUESTIONARY)
);
```

**Entidad JPA actual:**
```java
@Entity
@Table(name = "QUESTIONARY")
public class Questionary {
    @Id
    @Size(max = 36)  // ❌ Tamaño 36, pero SQL define 20
    @Column(name = "ID_QUESTIONARY", nullable = false)
    private String idQuestionary;  // ✅ String es correcto
}
```

**⚠️ IMPACTO:** Diferencia en tamaño máximo del ID (36 vs 20).

**✅ SOLUCIÓN:**
```java
@Size(max = 20)  // Ajustar a lo definido en SQL
@Column(name = "ID_QUESTIONARY", nullable = false, length = 20)
private String idQuestionary;
```

---

### 4. 🟡 CAMPOS OPCIONALES vs REQUERIDOS

#### 4.1 Tabla PROCESS - Campo CURRENT_PHASE

**Script v3:**
```sql
create table PROCESS
(
   ID_PROCESS           VARCHAR(36) DEFAULT (UUID()) not null,
   NAME                 varchar(50) not null,
   DESCRIPTION          varchar(1000) not null,
   DATE_BEGIN           varchar(20) not null,
   DATE_END             varchar(20) not null,
   ACTIVE               varchar(20) not null,
   CURRENT_PHASE        varchar(10),  -- ✅ OPCIONAL (nullable)
   primary key (ID_PROCESS)
);
```

**Entidad JPA actual:**
```java
@Entity
@Table(name = "PROCESS")
public class ProcessEgsi {
    @NotBlank  // ❌ INCORRECTO - El campo es nullable en SQL
    @Size(max = 10)
    @Column(name = "CURRENT_PHASE", nullable = false)  // ❌ nullable = false
    private String currentPhase;
}
```

**✅ SOLUCIÓN:**
```java
@Size(max = 10)
@Column(name = "CURRENT_PHASE", nullable = true)  // Permitir null
private String currentPhase;
// Remover @NotBlank
```

---

#### 4.2 Tabla ANSWERS - Campos opcionales

**Script v3:**
```sql
create table ANSWERS
(
   ID_ANSWER            VARCHAR(36) DEFAULT (UUID()) not null,
   ID_QUESTION          decimal not null,
   ID_PHASE             VARCHAR(36) DEFAULT (UUID()) not null,
   ANSWER_TEXT          varchar(1000),      -- ✅ Nullable
   CREATED_AT           varchar(20),        -- ✅ Nullable
   UPDATED_AT           varchar(20),        -- ✅ Nullable
   ANSWER_TYPE          varchar(20),        -- ✅ Nullable
   ANSWER_STATUS        varchar(20),        -- ✅ Nullable
   primary key (ID_ANSWER)
);
```

**Entidad JPA actual:**
```java
@Entity
@Table(name = "ANSWERS")
public class Answer {
    // ✅ Todos los campos son @Size en lugar de @NotBlank - CORRECTO
    @Size(max = 1000)
    @Column(name = "ANSWER_TEXT")
    private String answerText;
    
    @Size(max = 20)
    @Column(name = "CREATED_AT")
    private String createdAt;
    // etc.
}
```

**✅ Estado:** CORRECTO - Los campos opcionales están bien mapeados.

---

#### 4.3 Tabla RESPONSIBLES_SIGNING

**Script v3:**
```sql
create table RESPONSIBLES_SIGNING
(
   ID_RESPONSIBLE       VARCHAR(36) DEFAULT (UUID()) not null,
   ID_PHASE             VARCHAR(36) DEFAULT (UUID()) not null,
   NAME_RESPONSIBLE     varchar(100),       -- ✅ Nullable
   STATUS_SIGN          varchar(50),        -- ✅ Nullable
   CREATED_AT           varchar(20),        -- ✅ Nullable
   UPDATED_AT           varchar(20),        -- ✅ Nullable
   primary key (ID_RESPONSIBLE)
);
```

**Entidad JPA actual:**
```java
@Entity
@Table(name = "RESPONSIBLES_SIGNING")
public class ResponsibleSigning {
    @NotBlank  // ❌ INCORRECTO - Campo nullable en SQL
    @Size(max = 100)
    @Column(name = "NAME_RESPONSIBLE", nullable = false)  // ❌ nullable=false
    private String nameResponsible;

    @NotBlank  // ❌ INCORRECTO
    @Size(max = 50)
    @Column(name = "STATUS_SIGN", nullable = false)  // ❌ nullable=false
    private String statusSign;

    @NotBlank  // ❌ INCORRECTO
    @Size(max = 20)
    @Column(name = "CREATED_AT", nullable = false)  // ❌ nullable=false
    private String createdAt;

    @NotBlank  // ❌ INCORRECTO
    @Size(max = 20)
    @Column(name = "UPDATED_AT", nullable = false)  // ❌ nullable=false
    private String updatedAt;
}
```

**✅ SOLUCIÓN:**
```java
@Size(max = 100)
@Column(name = "NAME_RESPONSIBLE")  // Remover nullable=false
private String nameResponsible;

@Size(max = 50)
@Column(name = "STATUS_SIGN")
private String statusSign;

@Size(max = 20)
@Column(name = "CREATED_AT")
private String createdAt;

@Size(max = 20)
@Column(name = "UPDATED_AT")
private String updatedAt;
// Remover todas las anotaciones @NotBlank
```

---

### 5. 🟡 RELACIONES ENTRE TABLAS

#### 5.1 Relación ANSWERS - QUESTION (Tipo de dato incompatible)

**Script v3:**
```sql
alter table ANSWERS add constraint FK_RELATIONSHIP_3 foreign key (ID_QUESTION)
      references QUESTION (ID_QUESTION) on delete restrict on update restrict;
```

**Problema:** 
- ANSWERS.ID_QUESTION es `decimal`
- QUESTION.ID_QUESTION es `decimal`
- ✅ Coherente en SQL

**Entidad JPA:**
```java
// Answer.java
@ManyToOne(fetch = FetchType.LAZY)
@JoinColumn(name = "ID_QUESTION", nullable = false)
private Question question;  // ❌ Referencia a Question con UUID

// Question.java
@Id
@Column(name = "ID_QUESTION", nullable = false)
private UUID idQuestion;  // ❌ Debería ser Long
```

**⚠️ IMPACTO:** La relación no funcionará correctamente debido a tipos incompatibles.

---

## 📋 RESUMEN DE CORRECCIONES NECESARIAS

### Prioridad ALTA (Errores que impiden funcionamiento)

| # | Entidad | Problema | Corrección Requerida |
|---|---------|----------|---------------------|
| 1 | Phase | Nombre de tabla incorrecto | `@Table(name = "PHASE_CUSTOM")` |
| 2 | Question | Tipo de ID incorrecto | Cambiar `UUID` a `Long` |
| 3 | Answer | Relación con tipo incompatible | Actualizar después de corregir Question |

### Prioridad MEDIA (Inconsistencias de validación)

| # | Entidad | Problema | Corrección Requerida |
|---|---------|----------|---------------------|
| 4 | ProcessEgsi | Campo CURRENT_PHASE no debe ser @NotBlank | Remover @NotBlank, cambiar nullable=true |
| 5 | ResponsibleSigning | Campos opcionales marcados como @NotBlank | Remover @NotBlank de todos los campos |
| 6 | Questionary | Tamaño de ID inconsistente | Cambiar @Size(max=36) a @Size(max=20) |

### Prioridad BAJA (Optimizaciones)

| # | Área | Recomendación |
|---|------|---------------|
| 7 | Todas las entidades | Usar LocalDateTime en lugar de String para fechas |
| 8 | ProcessEgsi | ACTIVE debería ser Boolean en lugar de String |
| 9 | Índices | Agregar índices en claves foráneas para mejor rendimiento |

---

## 🔧 IMPACTO EN SERVICIOS

### Servicios afectados por cambios de tipos:

1. **QuestionService** - Cambiar todos los métodos de `UUID` a `Long`:
   ```java
   // Antes:
   QuestionDTO getQuestionById(UUID id);
   
   // Después:
   QuestionDTO getQuestionById(Long id);
   ```

2. **AnswerService** - Método `getAnswersByQuestion` debe usar Long:
   ```java
   // Antes:
   List<AnswerDTO> getAnswersByQuestion(UUID idQuestion);
   
   // Después:
   List<AnswerDTO> getAnswersByQuestion(Long idQuestion);
   ```

3. **DTOs** - Actualizar tipos en QuestionDTO y AnswerDTO

---

## ⚠️ RECOMENDACIONES ADICIONALES

1. **Migración de Datos**: Si ya existe data en producción, crear script de migración
2. **Tests**: Actualizar tests unitarios después de cambios de tipo
3. **API Documentation**: Actualizar Swagger con los nuevos tipos
4. **Validación**: Revisar reglas de validación en DTOs para que coincidan con SQL
5. **Scripts SQL**: Considerar unificar scripts (v3 vs principal) para evitar confusión

---

## 📝 PRÓXIMOS PASOS

1. ✅ **Revisar y aprobar** este análisis
2. 🔄 **Aplicar correcciones** en orden de prioridad
3. 🧪 **Ejecutar tests** después de cada cambio
4. 📚 **Actualizar documentación** Swagger/OpenAPI
5. 🚀 **Desplegar y verificar** en ambiente de desarrollo

---

**Generado automáticamente por GitHub Copilot**  
**Última actualización:** 10/01/2026
