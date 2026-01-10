-- ================================================================
-- Script de Base de Datos SIEGSI para MySQL 8.0
-- Basado en SIEGSI_DB.sql
-- Creado: 10/01/2026
-- ================================================================

-- Configuración inicial
SET FOREIGN_KEY_CHECKS = 0;
SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "-05:00";

-- ================================================================
-- Eliminar tablas existentes
-- ================================================================

DROP TABLE IF EXISTS `ANSWERS`;
DROP TABLE IF EXISTS `RESPONSIBLES_SIGNING`;
DROP TABLE IF EXISTS `QUESTION`;
DROP TABLE IF EXISTS `PHASES`;
DROP TABLE IF EXISTS `QUESTIONARY`;
DROP TABLE IF EXISTS `PROCESS`;
DROP TABLE IF EXISTS `USERS`;

-- ================================================================
-- Tabla: USERS
-- ================================================================
CREATE TABLE `USERS` (
    `ID_USER` VARCHAR(36) NOT NULL,
    `NAME` VARCHAR(50) NOT NULL,
    `LASTNAME` VARCHAR(50) NOT NULL,
    `CI` VARCHAR(50) NOT NULL,
    `USERNAME` VARCHAR(50) NOT NULL,
    `PASSWORD` VARCHAR(200) NOT NULL,
    `ROL` VARCHAR(50) NOT NULL,
    `IS_DELETED` TINYINT NOT NULL DEFAULT 0,
    PRIMARY KEY (`ID_USER`),
    UNIQUE KEY `UK_USERNAME` (`USERNAME`),
    INDEX `IDX_USERNAME` (`USERNAME`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ================================================================
-- Tabla: PROCESS
-- ================================================================
CREATE TABLE `PROCESS` (
    `ID_PROCESS` VARCHAR(36) NOT NULL,
    `NAME` VARCHAR(50) NOT NULL,
    `DESCRIPTION` VARCHAR(1000) NOT NULL,
    `DATE_BEGIN` VARCHAR(20) NOT NULL,
    `DATE_END` VARCHAR(20) NOT NULL,
    `ACTIVE` VARCHAR(20) NOT NULL,
    `CURRENT_PHASE` VARCHAR(10) NOT NULL,
    PRIMARY KEY (`ID_PROCESS`),
    INDEX `IDX_ACTIVE` (`ACTIVE`),
    INDEX `IDX_CURRENT_PHASE` (`CURRENT_PHASE`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ================================================================
-- Tabla: QUESTIONARY
-- ================================================================
CREATE TABLE `QUESTIONARY` (
    `ID_QUESTIONARY` VARCHAR(36) NOT NULL,
    `QUESTIONARY_NAME` VARCHAR(50) NOT NULL,
    `DESCRIPTION` VARCHAR(1000) NOT NULL,
    `PHASE` VARCHAR(10) NOT NULL,
    PRIMARY KEY (`ID_QUESTIONARY`),
    INDEX `IDX_PHASE` (`PHASE`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ================================================================
-- Tabla: PHASES
-- ================================================================
CREATE TABLE `PHASES` (
    `ID_PHASE` VARCHAR(36) NOT NULL,
    `ID_PROCESS` VARCHAR(36) NOT NULL,
    `QUESTIONARY_CODE` VARCHAR(50) NOT NULL,
    `RESPONSIBLES` VARCHAR(500) NOT NULL,
    `STATUS` VARCHAR(50) NOT NULL,
    PRIMARY KEY (`ID_PHASE`),
    INDEX `IDX_ID_PROCESS` (`ID_PROCESS`),
    INDEX `IDX_STATUS` (`STATUS`),
    INDEX `IDX_QUESTIONARY_CODE` (`QUESTIONARY_CODE`),
    CONSTRAINT `FK_PHASES_PROCESS` 
        FOREIGN KEY (`ID_PROCESS`) 
        REFERENCES `PROCESS` (`ID_PROCESS`)
        ON DELETE RESTRICT
        ON UPDATE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ================================================================
-- Tabla: QUESTION
-- ================================================================
CREATE TABLE `QUESTION` (
    `ID_QUESTION` VARCHAR(36) NOT NULL,
    `ID_QUESTIONARY` VARCHAR(36) NOT NULL,
    `DESCRIPTION` VARCHAR(1000) NULL,
    `QUESTION_TYPE` VARCHAR(50) NULL,
    `QUESTION_JSON` VARCHAR(1000) NULL,
    PRIMARY KEY (`ID_QUESTION`),
    INDEX `IDX_ID_QUESTIONARY` (`ID_QUESTIONARY`),
    INDEX `IDX_QUESTION_TYPE` (`QUESTION_TYPE`),
    CONSTRAINT `FK_QUESTION_QUESTIONARY` 
        FOREIGN KEY (`ID_QUESTIONARY`) 
        REFERENCES `QUESTIONARY` (`ID_QUESTIONARY`)
        ON DELETE RESTRICT
        ON UPDATE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ================================================================
-- Tabla: ANSWERS
-- ================================================================
CREATE TABLE `ANSWERS` (
    `ID_ANSWER` VARCHAR(36) NOT NULL,
    `ID_QUESTION` VARCHAR(36) NOT NULL,
    `ID_PHASE` VARCHAR(36) NOT NULL,
    `ANSWER_TEXT` VARCHAR(1000) NULL,
    `CREATED_AT` VARCHAR(20) NULL,
    `UPDATED_AT` VARCHAR(20) NULL,
    `ANSWER_TYPE` VARCHAR(20) NULL,
    `ANSWER_STATUS` VARCHAR(20) NULL,
    PRIMARY KEY (`ID_ANSWER`),
    INDEX `IDX_ID_QUESTION` (`ID_QUESTION`),
    INDEX `IDX_ID_PHASE` (`ID_PHASE`),
    INDEX `IDX_ANSWER_STATUS` (`ANSWER_STATUS`),
    CONSTRAINT `FK_ANSWERS_QUESTION` 
        FOREIGN KEY (`ID_QUESTION`) 
        REFERENCES `QUESTION` (`ID_QUESTION`)
        ON DELETE RESTRICT
        ON UPDATE RESTRICT,
    CONSTRAINT `FK_ANSWERS_PHASES` 
        FOREIGN KEY (`ID_PHASE`) 
        REFERENCES `PHASES` (`ID_PHASE`)
        ON DELETE RESTRICT
        ON UPDATE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ================================================================
-- Tabla: RESPONSIBLES_SIGNING
-- ================================================================
CREATE TABLE `RESPONSIBLES_SIGNING` (
    `ID_RESPONSIBLE` VARCHAR(36) NOT NULL,
    `ID_PHASE` VARCHAR(36) NOT NULL,
    `NAME_RESPONSIBLE` VARCHAR(100) NOT NULL,
    `STATUS_SIGN` VARCHAR(50) NOT NULL,
    `CREATED_AT` VARCHAR(20) NOT NULL,
    `UPDATED_AT` VARCHAR(20) NOT NULL,
    PRIMARY KEY (`ID_RESPONSIBLE`),
    INDEX `IDX_ID_PHASE` (`ID_PHASE`),
    INDEX `IDX_STATUS_SIGN` (`STATUS_SIGN`),
    CONSTRAINT `FK_RESPONSIBLES_PHASES` 
        FOREIGN KEY (`ID_PHASE`) 
        REFERENCES `PHASES` (`ID_PHASE`)
        ON DELETE RESTRICT
        ON UPDATE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ================================================================
-- Datos iniciales - Usuario administrador
-- ================================================================
-- Contraseña: admin123 (hasheada con BCrypt)
INSERT INTO `USERS` (`ID_USER`, `NAME`, `LASTNAME`, `CI`, `USERNAME`, `PASSWORD`, `ROL`, `IS_DELETED`)
VALUES (
    UUID(),
    'Administrador',
    'Sistema',
    '0000000000',
    'admin',
    '$2a$10$xXYHZqDWwIe.QgqPfPPxXeVdZp.dFqZp7YrCYqFqYqFqYqFqYqFqO',
    'ADMIN',
    0
);

-- ================================================================
-- Finalización
-- ================================================================
SET FOREIGN_KEY_CHECKS = 1;
COMMIT;
