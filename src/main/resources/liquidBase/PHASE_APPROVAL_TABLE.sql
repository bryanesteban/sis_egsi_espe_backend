-- ============================================================================
-- SIEGSI - Sistema de Gestión EGSI
-- Tabla: phase_approval_request
-- Descripción: Almacena las solicitudes de aprobación de fases
-- Fecha: 2026-01-14
-- ============================================================================

-- ============================================================================
-- CREAR TABLA phase_approval_request
-- ============================================================================
-- Esta tabla gestiona el flujo de aprobación de fases. Cuando un usuario
-- completa una fase, debe solicitar aprobación antes de avanzar a la siguiente.

CREATE TABLE IF NOT EXISTS phase_approval_request (
    ID_APPROVAL VARCHAR(36) NOT NULL,
    ID_PROCESS VARCHAR(36) NOT NULL,
    ID_PHASE VARCHAR(36) NOT NULL,
    PHASE_ORDER INT NOT NULL,
    PHASE_TITLE VARCHAR(255) NOT NULL,
    STATUS VARCHAR(20) NOT NULL DEFAULT 'PENDING',
    REQUESTED_BY VARCHAR(255) NOT NULL,
    REQUESTED_AT DATETIME NOT NULL,
    REVIEWED_BY VARCHAR(255),
    REVIEWED_AT DATETIME,
    COMMENTS TEXT,
    REJECTION_REASON TEXT,
    PRIMARY KEY (ID_APPROVAL),
    FOREIGN KEY (ID_PROCESS) REFERENCES process(ID_PROCESS) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================================================
-- ÍNDICES PARA OPTIMIZACIÓN DE CONSULTAS
-- ============================================================================

-- Índice para búsquedas por proceso
CREATE INDEX idx_approval_process 
    ON phase_approval_request(ID_PROCESS);

-- Índice para búsquedas por estado
CREATE INDEX idx_approval_status 
    ON phase_approval_request(STATUS);

-- Índice para búsquedas por proceso y fase
CREATE INDEX idx_approval_process_phase 
    ON phase_approval_request(ID_PROCESS, ID_PHASE);

-- Índice para solicitudes pendientes ordenadas por fecha
CREATE INDEX idx_approval_pending_date 
    ON phase_approval_request(STATUS, REQUESTED_AT);

-- ============================================================================
-- ESTADOS POSIBLES
-- ============================================================================
-- PENDING   - Solicitud pendiente de revisión
-- APPROVED  - Solicitud aprobada, se puede avanzar a la siguiente fase
-- REJECTED  - Solicitud rechazada, debe corregir y volver a solicitar
-- CANCELLED - Solicitud cancelada por el usuario

-- ============================================================================
-- EJEMPLO DE USO
-- ============================================================================
-- 1. Usuario completa fase 1 y solicita aprobación:
--    INSERT INTO phase_approval_request (ID_APPROVAL, ID_PROCESS, ID_PHASE, PHASE_ORDER, 
--                                        PHASE_TITLE, STATUS, REQUESTED_BY, REQUESTED_AT)
--    VALUES (UUID(), 'process-123', 'phase-001', 1, 'Diagnóstico Inicial', 'PENDING', 
--            'usuario@espe.edu.ec', NOW());
--
-- 2. Aprobador revisa y aprueba:
--    UPDATE phase_approval_request 
--    SET STATUS = 'APPROVED', REVIEWED_BY = 'aprobador@espe.edu.ec', REVIEWED_AT = NOW()
--    WHERE ID_APPROVAL = 'approval-123';
--
-- 3. Si es rechazada:
--    UPDATE phase_approval_request 
--    SET STATUS = 'REJECTED', REVIEWED_BY = 'aprobador@espe.edu.ec', REVIEWED_AT = NOW(),
--        REJECTION_REASON = 'Faltan documentos de soporte'
--    WHERE ID_APPROVAL = 'approval-123';
