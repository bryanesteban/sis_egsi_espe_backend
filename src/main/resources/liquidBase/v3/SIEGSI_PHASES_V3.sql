/*==============================================================*/
/* DBMS name:      MySQL 8.0                                    */
/* Created on:     13/1/2026                                    */
/* Description:    Tablas para Fases EGSI Estándar              */
/*==============================================================*/

-- ============================================================
-- EGSI_PHASE: Fases estándar que se aplican a todos los procesos
-- ============================================================
CREATE TABLE IF NOT EXISTS EGSI_PHASE (
    ID_PHASE VARCHAR(36) NOT NULL DEFAULT (UUID()),
    TITLE VARCHAR(200) NOT NULL,
    DESCRIPTION VARCHAR(1000),
    PHASE_ORDER INT NOT NULL,
    IS_ACTIVE BOOLEAN NOT NULL DEFAULT TRUE,
    CREATED_AT TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UPDATED_AT TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (ID_PHASE)
);

-- ============================================================
-- EGSI_SECTION: Secciones dentro de cada fase
-- ============================================================
CREATE TABLE IF NOT EXISTS EGSI_SECTION (
    ID_SECTION VARCHAR(36) NOT NULL DEFAULT (UUID()),
    ID_PHASE VARCHAR(36) NOT NULL,
    TITLE VARCHAR(200) NOT NULL,
    DESCRIPTION VARCHAR(1000),
    SECTION_ORDER INT NOT NULL,
    CREATED_AT TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UPDATED_AT TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (ID_SECTION),
    CONSTRAINT FK_SECTION_PHASE FOREIGN KEY (ID_PHASE) 
        REFERENCES EGSI_PHASE(ID_PHASE) ON DELETE CASCADE ON UPDATE CASCADE
);

-- ============================================================
-- EGSI_QUESTION: Preguntas dentro de cada sección
-- ============================================================
CREATE TABLE IF NOT EXISTS EGSI_QUESTION (
    ID_QUESTION VARCHAR(36) NOT NULL DEFAULT (UUID()),
    ID_SECTION VARCHAR(36) NOT NULL,
    TITLE VARCHAR(500) NOT NULL,
    DESCRIPTION VARCHAR(1000),
    INPUT_TYPE VARCHAR(20) NOT NULL DEFAULT 'TEXTO',
    IS_REQUIRED BOOLEAN NOT NULL DEFAULT TRUE,
    PLACEHOLDER VARCHAR(500),
    MAX_LENGTH INT DEFAULT 1000,
    TABLE_CONFIG JSON,
    QUESTION_ORDER INT NOT NULL,
    CREATED_AT TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UPDATED_AT TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (ID_QUESTION),
    CONSTRAINT FK_QUESTION_SECTION FOREIGN KEY (ID_SECTION) 
        REFERENCES EGSI_SECTION(ID_SECTION) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT CHK_INPUT_TYPE CHECK (INPUT_TYPE IN ('TEXTO', 'DATE', 'TABLA'))
);

-- ============================================================
-- EGSI_PROCESS_PHASE: Instancia de fase para un proceso específico
-- ============================================================
CREATE TABLE IF NOT EXISTS EGSI_PROCESS_PHASE (
    ID_PROCESS_PHASE VARCHAR(36) NOT NULL DEFAULT (UUID()),
    ID_PROCESS VARCHAR(36) NOT NULL,
    ID_PHASE VARCHAR(36) NOT NULL,
    STATUS VARCHAR(20) NOT NULL DEFAULT 'LOCKED',
    PROGRESS_PERCENTAGE DECIMAL(5,2) DEFAULT 0.00,
    STARTED_AT TIMESTAMP NULL,
    COMPLETED_AT TIMESTAMP NULL,
    CREATED_AT TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UPDATED_AT TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (ID_PROCESS_PHASE),
    CONSTRAINT FK_PROCESS_PHASE_PROCESS FOREIGN KEY (ID_PROCESS) 
        REFERENCES PROCESS(ID_PROCESS) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT FK_PROCESS_PHASE_PHASE FOREIGN KEY (ID_PHASE) 
        REFERENCES EGSI_PHASE(ID_PHASE) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT CHK_PHASE_STATUS CHECK (STATUS IN ('LOCKED', 'ACTIVE', 'COMPLETED')),
    UNIQUE KEY UK_PROCESS_PHASE (ID_PROCESS, ID_PHASE)
);

-- ============================================================
-- EGSI_ANSWER: Respuestas a las preguntas por proceso
-- ============================================================
CREATE TABLE IF NOT EXISTS EGSI_ANSWER (
    ID_ANSWER VARCHAR(36) NOT NULL DEFAULT (UUID()),
    ID_PROCESS_PHASE VARCHAR(36) NOT NULL,
    ID_QUESTION VARCHAR(36) NOT NULL,
    ANSWER_VALUE TEXT,
    ANSWER_JSON JSON,
    CREATED_AT TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UPDATED_AT TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (ID_ANSWER),
    CONSTRAINT FK_ANSWER_PROCESS_PHASE FOREIGN KEY (ID_PROCESS_PHASE) 
        REFERENCES EGSI_PROCESS_PHASE(ID_PROCESS_PHASE) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT FK_ANSWER_QUESTION FOREIGN KEY (ID_QUESTION) 
        REFERENCES EGSI_QUESTION(ID_QUESTION) ON DELETE CASCADE ON UPDATE CASCADE,
    UNIQUE KEY UK_ANSWER (ID_PROCESS_PHASE, ID_QUESTION)
);

-- ============================================================
-- Índices para mejorar el rendimiento
-- ============================================================
CREATE INDEX IDX_PHASE_ORDER ON EGSI_PHASE(PHASE_ORDER);
CREATE INDEX IDX_PHASE_ACTIVE ON EGSI_PHASE(IS_ACTIVE);
CREATE INDEX IDX_SECTION_PHASE ON EGSI_SECTION(ID_PHASE);
CREATE INDEX IDX_SECTION_ORDER ON EGSI_SECTION(SECTION_ORDER);
CREATE INDEX IDX_QUESTION_SECTION ON EGSI_QUESTION(ID_SECTION);
CREATE INDEX IDX_QUESTION_ORDER ON EGSI_QUESTION(QUESTION_ORDER);
CREATE INDEX IDX_PROCESS_PHASE_PROCESS ON EGSI_PROCESS_PHASE(ID_PROCESS);
CREATE INDEX IDX_PROCESS_PHASE_STATUS ON EGSI_PROCESS_PHASE(STATUS);
CREATE INDEX IDX_ANSWER_PROCESS_PHASE ON EGSI_ANSWER(ID_PROCESS_PHASE);

-- ============================================================
-- DATOS INICIALES: Fases EGSI Estándar
-- ============================================================
INSERT INTO EGSI_PHASE (ID_PHASE, TITLE, DESCRIPTION, PHASE_ORDER, IS_ACTIVE) VALUES
('phase-001', 'Fase 1: Diagnóstico Inicial', 'Evaluación del estado actual de la seguridad de la información en la institución.', 1, TRUE),
('phase-002', 'Fase 2: Análisis de Riesgos', 'Identificar, analizar y evaluar los riesgos de seguridad de la información.', 2, TRUE),
('phase-003', 'Fase 3: Plan de Tratamiento', 'Desarrollar el plan de tratamiento de riesgos.', 3, TRUE),
('phase-004', 'Fase 4: Implementación', 'Implementar los controles de seguridad seleccionados.', 4, TRUE),
('phase-005', 'Fase 5: Monitoreo y Mejora', 'Establecer mecanismos de monitoreo y mejora continua.', 5, TRUE);

-- Secciones para Fase 1
INSERT INTO EGSI_SECTION (ID_SECTION, ID_PHASE, TITLE, DESCRIPTION, SECTION_ORDER) VALUES
('sec-001', 'phase-001', '1.1 Información General de la Institución', 'Complete la información básica de identificación institucional.', 1),
('sec-002', 'phase-001', '1.2 Alcance del Sistema de Gestión', 'Defina el alcance del SGSI.', 2);

-- Secciones para Fase 2
INSERT INTO EGSI_SECTION (ID_SECTION, ID_PHASE, TITLE, DESCRIPTION, SECTION_ORDER) VALUES
('sec-003', 'phase-002', '2.1 Identificación de Activos', 'Identifique los activos de información críticos.', 1),
('sec-004', 'phase-002', '2.2 Evaluación de Riesgos', 'Evalúe los riesgos identificados.', 2);

-- Secciones para Fase 3
INSERT INTO EGSI_SECTION (ID_SECTION, ID_PHASE, TITLE, DESCRIPTION, SECTION_ORDER) VALUES
('sec-005', 'phase-003', '3.1 Selección de Controles', 'Seleccione los controles apropiados del Anexo A de ISO 27001.', 1);

-- Preguntas para Sección 1.1
INSERT INTO EGSI_QUESTION (ID_QUESTION, ID_SECTION, TITLE, DESCRIPTION, INPUT_TYPE, IS_REQUIRED, PLACEHOLDER, MAX_LENGTH, QUESTION_ORDER) VALUES
('q-001', 'sec-001', 'Nombre de la Institución', 'Ingrese el nombre oficial completo de la institución.', 'TEXTO', TRUE, 'Ej: Universidad de las Fuerzas Armadas ESPE', 200, 1),
('q-002', 'sec-001', 'Fecha de Inicio del Diagnóstico', 'Seleccione la fecha en que inicia el proceso de diagnóstico.', 'DATE', TRUE, NULL, NULL, 2),
('q-003', 'sec-001', 'Responsable del Proceso', 'Nombre completo del funcionario responsable del proceso EGSI.', 'TEXTO', TRUE, 'Ej: Ing. Juan Pérez', 200, 3);

-- Preguntas para Sección 1.2
INSERT INTO EGSI_QUESTION (ID_QUESTION, ID_SECTION, TITLE, DESCRIPTION, INPUT_TYPE, IS_REQUIRED, PLACEHOLDER, MAX_LENGTH, QUESTION_ORDER) VALUES
('q-004', 'sec-002', 'Descripción del Alcance', 'Describa detalladamente el alcance del Sistema de Gestión de Seguridad de la Información.', 'TEXTO', TRUE, 'Describa el alcance incluyendo procesos, ubicaciones y tecnologías...', 2000, 1),
('q-005', 'sec-002', 'Ubicaciones Físicas', 'Liste todas las ubicaciones físicas que están dentro del alcance del SGSI.', 'TABLA', TRUE, NULL, NULL, 2);

-- Configuración de tabla para pregunta q-005
UPDATE EGSI_QUESTION SET TABLE_CONFIG = '{"columns":[{"key":"ubicacion","header":"Ubicación","width":"30%"},{"key":"direccion","header":"Dirección","width":"40%"},{"key":"responsable","header":"Responsable","width":"30%"}],"minRows":1,"maxRows":20}' WHERE ID_QUESTION = 'q-005';

-- Preguntas para Sección 2.1
INSERT INTO EGSI_QUESTION (ID_QUESTION, ID_SECTION, TITLE, DESCRIPTION, INPUT_TYPE, IS_REQUIRED, TABLE_CONFIG, QUESTION_ORDER) VALUES
('q-006', 'sec-003', 'Inventario de Activos Críticos', 'Complete la tabla con los activos de información críticos identificados.', 'TABLA', TRUE, 
'{"columns":[{"key":"codigo","header":"Código","width":"10%"},{"key":"nombre","header":"Nombre","width":"30%"},{"key":"tipo","header":"Tipo","width":"20%"},{"key":"propietario","header":"Propietario","width":"20%"},{"key":"criticidad","header":"Criticidad","width":"20%"}],"minRows":1,"maxRows":50}', 1);

-- Preguntas para Sección 2.2
INSERT INTO EGSI_QUESTION (ID_QUESTION, ID_SECTION, TITLE, DESCRIPTION, INPUT_TYPE, IS_REQUIRED, PLACEHOLDER, MAX_LENGTH, QUESTION_ORDER) VALUES
('q-007', 'sec-004', 'Metodología de Evaluación', 'Describa la metodología utilizada para la evaluación de riesgos.', 'TEXTO', TRUE, 'Ej: Se utilizó la metodología MAGERIT v3...', 2000, 1);

INSERT INTO EGSI_QUESTION (ID_QUESTION, ID_SECTION, TITLE, DESCRIPTION, INPUT_TYPE, IS_REQUIRED, TABLE_CONFIG, QUESTION_ORDER) VALUES
('q-008', 'sec-004', 'Matriz de Riesgos', 'Complete la matriz de riesgos identificados y evaluados.', 'TABLA', TRUE,
'{"columns":[{"key":"riesgo","header":"Riesgo","width":"30%"},{"key":"probabilidad","header":"Prob.","width":"15%"},{"key":"impacto","header":"Impacto","width":"15%"},{"key":"nivel","header":"Nivel","width":"15%"},{"key":"tratamiento","header":"Tratamiento","width":"25%"}],"minRows":1,"maxRows":100}', 2);

-- Preguntas para Sección 3.1
INSERT INTO EGSI_QUESTION (ID_QUESTION, ID_SECTION, TITLE, DESCRIPTION, INPUT_TYPE, IS_REQUIRED, TABLE_CONFIG, QUESTION_ORDER) VALUES
('q-009', 'sec-005', 'Controles Seleccionados', 'Liste los controles de seguridad seleccionados del Anexo A de ISO 27001.', 'TABLA', TRUE,
'{"columns":[{"key":"control","header":"Control","width":"20%"},{"key":"descripcion","header":"Descripción","width":"40%"},{"key":"riesgo","header":"Riesgo Asociado","width":"20%"},{"key":"responsable","header":"Responsable","width":"20%"}],"minRows":1,"maxRows":50}', 1);
