/*==============================================================*/
/* DBMS name:      MySQL 5.0                                    */
/* Created on:     10/1/2026 11:28:42                           */
/*==============================================================*/


drop table if exists ANSWERS;

drop table if exists PHASE_CUSTOM;

drop table if exists PROCESS;

drop table if exists QUESTION;

drop table if exists QUESTIONARY;

drop table if exists RESPONSIBLES_SIGNING;

drop table if exists USERS;

/*==============================================================*/
/* Table: ANSWERS                                               */
/*==============================================================*/
create table ANSWERS
(
   ID_ANSWER            VARCHAR(36) DEFAULT (UUID()) not null,
   ID_QUESTION          decimal not null,
   ID_PHASE             VARCHAR(36) DEFAULT (UUID()) not null,
   ANSWER_TEXT          varchar(1000),
   CREATED_AT           varchar(20),
   UPDATED_AT           varchar(20),
   ANSWER_TYPE          varchar(20),
   ANSWER_STATUS        varchar(20),
   primary key (ID_ANSWER)
);

/*==============================================================*/
/* Table: PHASE_CUSTOM                                          */
/*==============================================================*/
create table PHASE_CUSTOM
(
   ID_PHASE             VARCHAR(36) DEFAULT (UUID()) not null,
   ID_PROCESS           VARCHAR(36) DEFAULT (UUID()) not null,
   QUESTIONARY_CODE     varchar(50) not null,
   RESPONSIBLES         varchar(500) not null,
   STATUS               varchar(50) not null,
   primary key (ID_PHASE)
);

/*==============================================================*/
/* Table: PROCESS                                               */
/*==============================================================*/
create table PROCESS
(
   ID_PROCESS           VARCHAR(36) DEFAULT (UUID()) not null,
   NAME                 varchar(50) not null,
   DESCRIPTION          varchar(1000) not null,
   DATE_BEGIN           varchar(20) not null,
   DATE_END             varchar(20) not null,
   ACTIVE               varchar(20) not null,
   CURRENT_PHASE        varchar(10),
   primary key (ID_PROCESS)
);

/*==============================================================*/
/* Table: QUESTION                                              */
/*==============================================================*/
create table QUESTION
(
   ID_QUESTION          decimal not null,
   ID_QUESTIONARY       varchar(20) not null,
   DESCRIPTION          varchar(1000),
   QUESTION_TYPE        varchar(50),
   QUESTION_JSON        varchar(1000),
   primary key (ID_QUESTION)
);

/*==============================================================*/
/* Table: QUESTIONARY                                           */
/*==============================================================*/
create table QUESTIONARY
(
   ID_QUESTIONARY       varchar(20) not null,
   QUESTIONARY_NAME     varchar(50) not null,
   DESCRIPTION          varchar(1000) not null,
   PHASE                varchar(10) not null,
   primary key (ID_QUESTIONARY)
);

/*==============================================================*/
/* Table: RESPONSIBLES_SIGNING                                  */
/*==============================================================*/
create table RESPONSIBLES_SIGNING
(
   ID_RESPONSIBLE       VARCHAR(36) DEFAULT (UUID()) not null,
   ID_PHASE             VARCHAR(36) DEFAULT (UUID()) not null,
   NAME_RESPONSIBLE     varchar(100),
   STATUS_SIGN          varchar(50),
   CREATED_AT           varchar(20),
   UPDATED_AT           varchar(20),
   primary key (ID_RESPONSIBLE)
);

/*==============================================================*/
/* Table: USERS                                                 */
/*==============================================================*/
create table USERS
(
   ID_USER              VARCHAR(36) DEFAULT (UUID()) not null,
   NAME                 varchar(50) not null,
   LASTNAME             varchar(50) not null,
   CI                   varchar(50) not null,
   USERNAME             varchar(50) not null,
   PASSWORD             varchar(200) not null,
   ROL                  varchar(50) not null,
   IS_DELETED           bool not null,
   primary key (ID_USER)
);

alter table ANSWERS add constraint FK_RELATIONSHIP_3 foreign key (ID_QUESTION)
      references QUESTION (ID_QUESTION) on delete restrict on update restrict;

alter table ANSWERS add constraint FK_RELATIONSHIP_5 foreign key (ID_PHASE)
      references PHASE_CUSTOM (ID_PHASE) on delete restrict on update restrict;

alter table PHASE_CUSTOM add constraint FK_RELATIONSHIP_4 foreign key (ID_PROCESS)
      references PROCESS (ID_PROCESS) on delete restrict on update restrict;

alter table QUESTION add constraint FK_RELATIONSHIP_2 foreign key (ID_QUESTIONARY)
      references QUESTIONARY (ID_QUESTIONARY) on delete restrict on update restrict;

alter table RESPONSIBLES_SIGNING add constraint FK_RELATIONSHIP_6 foreign key (ID_PHASE)
      references PHASE_CUSTOM (ID_PHASE) on delete restrict on update restrict;


/*==============================================================*/
/* INSERT DATA                                                  */
/*==============================================================*/

-- Cuestionario PERFIL
INSERT INTO QUESTIONARY (ID_QUESTIONARY, QUESTIONARY_NAME, DESCRIPTION, PHASE) 
VALUES ('CUEST001', 'PERFIL', 'DATOS INICIALES DEL PROYECTO', 'FASE1');

-- Preguntas del cuestionario PERFIL
INSERT INTO QUESTION (ID_QUESTION, ID_QUESTIONARY, DESCRIPTION, QUESTION_TYPE, QUESTION_JSON) 
VALUES 
    (1, 'CUEST001', '1. NOMBRE DEL PROYECTO:', 'TEXTO', ''),
    (2, 'CUEST001', 'Implementación del Esquema Gubernamental de Seguridad de la información (EGSI V3)', 'TITULO', ''),
    (3, 'CUEST001', 'Programa relacionado:', 'TEXTO', ''),
    (4, 'CUEST001', 'Líder del Proyecto', 'TEXTO', ''),
    (5, 'CUEST001', 'Fecha de Inicio:', 'DATE', ''),
    (6, 'CUEST001', 'Fecha de Fin:', 'DATE', ''),
    (7, 'CUEST001', '2. CONTEXTUALIZACIÓN DEL PROYECTO', 'TITULO', ''),
    (8, 'CUEST001', 'a) Problema:', 'TEXTO', ''),
    (9, 'CUEST001', 'b) Justificación:', 'TEXTO', ''),
    (10, 'CUEST001', '3. OBJETIVOS', 'TITULO', ''),
    (11, 'CUEST001', 'a) Objetivo general:', 'TEXTO', ''),
    (12, 'CUEST001', 'b) Beneficiarios', 'TEXTO', ''),
    (13, 'CUEST001', 'c) Plazo de ejecución', 'TEXTO', ''),
    (14, 'CUEST001', 'd) Cronograma de Ejecución – Actividades', 'TITULO', ''),
    (15, 'CUEST001', 'e) Hitos de control', 'TABLA', '[{"title":"Hitos de control","Type":"TEXTO"},{"title":"Fecha comprometida","Type":"DATE"},{"title":"Fecha estimada","Type":"DATE"},{"title":"% de avance físico","Type":"TEXTO"}]');

-- Usuario inicial de prueba
-- Username: mdavalos
-- Password: password (BCrypt hash)
INSERT INTO USERS (ID_USER, NAME, LASTNAME, CI, USERNAME, PASSWORD, ROL, IS_DELETED) 
VALUES (UUID(), 'Carlos', 'Daroma', '123215432', 'mdavalos', '$2a$10$vdKkWcsR65dXjN.bOk/mnu1vSxX6axWXLXr/dP0fOF4gcCQQwSkFu', 'ADMIN', 0);
