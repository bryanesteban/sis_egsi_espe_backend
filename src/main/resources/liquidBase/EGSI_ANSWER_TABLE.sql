-- ============================================================================
-- SIEGSI - Sistema de Gestión EGSI
-- Tabla: egsi_answer
-- Descripción: Almacena las respuestas de los usuarios a las preguntas EGSI
-- Fecha: 2026-01-14
-- ============================================================================

-- ============================================================================
-- CREAR TABLA egsi_answer
-- ============================================================================
-- Esta tabla permite guardar el progreso de cada pregunta para que el usuario
-- pueda retomar la edición posteriormente.

CREATE TABLE IF NOT EXISTS egsi_answer (
    ID_ANSWER VARCHAR(36) NOT NULL,
    ID_PROCESS VARCHAR(36) NOT NULL,
    ID_QUESTION VARCHAR(36) NOT NULL,
    ID_PHASE VARCHAR(36) NOT NULL,
    ANSWER_VALUE LONGTEXT,
    STATUS VARCHAR(20) DEFAULT 'PENDING',
    CREATED_AT DATETIME,
    UPDATED_AT DATETIME,
    CREATED_BY VARCHAR(255),
    UPDATED_BY VARCHAR(255),
    PRIMARY KEY (ID_ANSWER)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================================================
-- ÍNDICES PARA OPTIMIZACIÓN DE CONSULTAS
-- ============================================================================

-- Índice único para evitar respuestas duplicadas (un proceso solo puede tener una respuesta por pregunta)
CREATE UNIQUE INDEX idx_answer_process_question 
    ON egsi_answer(ID_PROCESS, ID_QUESTION);

-- Índice para búsquedas por proceso
CREATE INDEX idx_answer_process 
    ON egsi_answer(ID_PROCESS);

-- Índice para búsquedas por proceso y fase
CREATE INDEX idx_answer_process_phase 
    ON egsi_answer(ID_PROCESS, ID_PHASE);

-- Índice para búsquedas por estado
CREATE INDEX idx_answer_status 
    ON egsi_answer(STATUS);

-- ============================================================================
-- NOTAS IMPORTANTES
-- ============================================================================
-- 
-- 1. No se agregaron Foreign Keys debido a incompatibilidad de tipos:
--    - La tabla PROCESS usa UUID con @JdbcTypeCode(SqlTypes.CHAR)
--    - Se mantiene integridad referencial a nivel de aplicación
--
-- 2. Estados posibles para STATUS:
--    - PENDING: Respuesta vacía o no iniciada
--    - IN_PROGRESS: Respuesta con contenido, en edición
--    - COMPLETED: Respuesta finalizada y validada
--
-- 3. El campo ANSWER_VALUE usa LONGTEXT para soportar:
--    - Texto simple
--    - JSON de tablas dinámicas
--    - Contenido de editores de texto enriquecido (BlockNote)
--
-- ============================================================================
-- CONSULTAS ÚTILES
-- ============================================================================

-- Ver todas las respuestas de un proceso específico:
-- SELECT * FROM egsi_answer WHERE ID_PROCESS = 'uuid-del-proceso';

-- Ver progreso de una fase específica:
-- SELECT 
--     COUNT(*) as total,
--     SUM(CASE WHEN STATUS = 'COMPLETED' THEN 1 ELSE 0 END) as completadas,
--     ROUND(SUM(CASE WHEN STATUS = 'COMPLETED' THEN 1 ELSE 0 END) * 100.0 / COUNT(*), 2) as porcentaje
-- FROM egsi_answer 
-- WHERE ID_PROCESS = 'uuid-del-proceso' AND ID_PHASE = 'uuid-de-la-fase';

-- Obtener respuestas como mapa (para cargar en frontend):
-- SELECT ID_QUESTION, ANSWER_VALUE 
-- FROM egsi_answer 
-- WHERE ID_PROCESS = 'uuid-del-proceso' AND ID_PHASE = 'uuid-de-la-fase';
