/*==============================================================*/
/* DBMS name:      MySQL 8.0                                    */
/* Created on:     18/1/2026                                    */
/* Description:    Complete database schema with all entities  */
/*==============================================================*/

-- Eliminar tablas en orden correcto (respetando FK)
drop table if exists EGSI_ANSWER;
drop table if exists EGSI_QUESTION;
drop table if exists EGSI_SECTION;
drop table if exists EGSI_PHASE;
drop table if exists PHASE_APPROVAL_REQUEST;
drop table if exists ANSWERS;
drop table if exists RESPONSIBLES_SIGNING;
drop table if exists PHASE_CUSTOM;
drop table if exists QUESTION;
drop table if exists QUESTIONARY;
drop table if exists PROCESS;
drop table if exists USERS;

/*==============================================================*/
/* Table: USERS                                                 */
/*==============================================================*/
create table USERS
(
   ID_USER              VARCHAR(36) not null,
   NAME                 varchar(50) not null,
   LASTNAME             varchar(50) not null,
   CI                   varchar(50) not null,
   USERNAME             varchar(50) not null,
   PASSWORD             varchar(200) not null,
   ROL                  varchar(50) not null,
   IS_DELETED           bool not null default 0,
   primary key (ID_USER),
   unique key UK_USERNAME (USERNAME),
   unique key UK_CI (CI)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

/*==============================================================*/
/* Table: PROCESS                                               */
/*==============================================================*/
create table PROCESS
(
   ID_PROCESS           VARCHAR(36) not null,
   NAME                 varchar(50) not null,
   DESCRIPTION          varchar(1000) not null,
   DATE_BEGIN           varchar(20) not null,
   DATE_END             varchar(20) not null,
   STATUS               varchar(20) not null,
   CURRENT_PHASE        varchar(20) not null,
   primary key (ID_PROCESS)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

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
   primary key (ID_QUESTION),
   constraint FK_QUESTION_QUESTIONARY foreign key (ID_QUESTIONARY)
      references QUESTIONARY (ID_QUESTIONARY) on delete cascade on update cascade
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

/*==============================================================*/
/* Table: PHASE_CUSTOM                                          */
/*==============================================================*/
create table PHASE_CUSTOM
(
   ID_PHASE             VARCHAR(36) not null,
   ID_PROCESS           VARCHAR(36) not null,
   QUESTIONARY_CODE     varchar(50) not null,
   RESPONSIBLES         varchar(500) not null,
   STATUS               varchar(50) not null,
   primary key (ID_PHASE),
   constraint FK_PHASE_PROCESS foreign key (ID_PROCESS)
      references PROCESS (ID_PROCESS) on delete cascade on update cascade
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

/*==============================================================*/
/* Table: ANSWERS                                               */
/*==============================================================*/
create table ANSWERS
(
   ID_ANSWER            VARCHAR(36) not null,
   ID_QUESTION          decimal not null,
   ID_PHASE             VARCHAR(36) not null,
   ANSWER_TEXT          varchar(1000),
   CREATED_AT           varchar(20),
   UPDATED_AT           varchar(20),
   ANSWER_TYPE          varchar(20),
   ANSWER_STATUS        varchar(20),
   primary key (ID_ANSWER),
   constraint FK_ANSWER_QUESTION foreign key (ID_QUESTION)
      references QUESTION (ID_QUESTION) on delete cascade on update cascade,
   constraint FK_ANSWER_PHASE foreign key (ID_PHASE)
      references PHASE_CUSTOM (ID_PHASE) on delete cascade on update cascade
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

/*==============================================================*/
/* Table: RESPONSIBLES_SIGNING                                  */
/*==============================================================*/
create table RESPONSIBLES_SIGNING
(
   ID_RESPONSIBLE       VARCHAR(36) not null,
   ID_PHASE             VARCHAR(36) not null,
   NAME_RESPONSIBLE     varchar(100),
   STATUS_SIGN          varchar(50),
   CREATED_AT           varchar(20),
   UPDATED_AT           varchar(20),
   primary key (ID_RESPONSIBLE),
   constraint FK_RESPONSIBLE_PHASE foreign key (ID_PHASE)
      references PHASE_CUSTOM (ID_PHASE) on delete cascade on update cascade
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

/*==============================================================*/
/* Table: EGSI_PHASE                                            */
/* Description: Tabla para las fases estándar EGSI             */
/*==============================================================*/
create table EGSI_PHASE
(
   ID_PHASE             VARCHAR(36) not null,
   TITLE                VARCHAR(200) not null,
   DESCRIPTION          VARCHAR(1000),
   PHASE_ORDER          INT not null,
   IS_ACTIVE            BOOLEAN not null default 1,
   CREATED_AT           DATETIME,
   UPDATED_AT           DATETIME,
   primary key (ID_PHASE)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

/*==============================================================*/
/* Table: EGSI_SECTION                                          */
/* Description: Secciones dentro de cada fase EGSI             */
/*==============================================================*/
create table EGSI_SECTION
(
   ID_SECTION           VARCHAR(36) not null,
   ID_PHASE             VARCHAR(36) not null,
   TITLE                VARCHAR(200) not null,
   DESCRIPTION          VARCHAR(1000),
   SECTION_ORDER        INT not null,
   CREATED_AT           DATETIME,
   UPDATED_AT           DATETIME,
   primary key (ID_SECTION),
   constraint FK_SECTION_PHASE foreign key (ID_PHASE)
      references EGSI_PHASE (ID_PHASE) on delete cascade on update cascade
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

/*==============================================================*/
/* Table: EGSI_QUESTION                                         */
/* Description: Preguntas dentro de cada sección EGSI          */
/*==============================================================*/
create table EGSI_QUESTION
(
   ID_QUESTION          VARCHAR(36) not null,
   ID_SECTION           VARCHAR(36) not null,
   TITLE                VARCHAR(500) not null,
   DESCRIPTION          VARCHAR(1000),
   INPUT_TYPE           VARCHAR(20) not null default 'TEXTO',
   IS_REQUIRED          BOOLEAN not null default 1,
   PLACEHOLDER          VARCHAR(500),
   MAX_LENGTH           INT default 1000,
   TABLE_CONFIG         JSON,
   QUESTION_ORDER       INT not null,
   CREATED_AT           DATETIME,
   UPDATED_AT           DATETIME,
   primary key (ID_QUESTION),
   constraint FK_QUESTION_SECTION foreign key (ID_SECTION)
      references EGSI_SECTION (ID_SECTION) on delete cascade on update cascade
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

/*==============================================================*/
/* Table: EGSI_ANSWER                                           */
/* Description: Respuestas a preguntas EGSI por proceso        */
/*==============================================================*/
create table EGSI_ANSWER
(
   ID_ANSWER            VARCHAR(36) not null,
   ID_PROCESS           VARCHAR(36) not null,
   ID_QUESTION          VARCHAR(36) not null,
   ID_PHASE             VARCHAR(36) not null,
   ANSWER_VALUE         LONGTEXT,
   STATUS               VARCHAR(20) default 'PENDING',
   CREATED_AT           DATETIME,
   UPDATED_AT           DATETIME,
   CREATED_BY           VARCHAR(255),
   UPDATED_BY           VARCHAR(255),
   primary key (ID_ANSWER),
   unique key UK_PROCESS_QUESTION (ID_PROCESS, ID_QUESTION),
   constraint FK_EGSI_ANSWER_PROCESS foreign key (ID_PROCESS)
      references PROCESS (ID_PROCESS) on delete cascade on update cascade,
   constraint FK_EGSI_ANSWER_QUESTION foreign key (ID_QUESTION)
      references EGSI_QUESTION (ID_QUESTION) on delete cascade on update cascade
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

/*==============================================================*/
/* Table: PHASE_APPROVAL_REQUEST                                */
/* Description: Solicitudes de aprobación de fases             */
/*==============================================================*/
create table PHASE_APPROVAL_REQUEST
(
   ID_APPROVAL          VARCHAR(36) not null,
   ID_PROCESS           VARCHAR(36) not null,
   ID_PHASE             VARCHAR(36) not null,
   PHASE_ORDER          INT not null,
   PHASE_TITLE          VARCHAR(200) not null,
   STATUS               VARCHAR(50) not null,
   REQUESTED_BY         VARCHAR(255) not null,
   REQUESTED_AT         DATETIME not null,
   REVIEWED_BY          VARCHAR(255),
   REVIEWED_AT          DATETIME,
   COMMENTS             VARCHAR(2000),
   REJECTION_REASON     VARCHAR(2000),
   primary key (ID_APPROVAL),
   constraint FK_APPROVAL_PROCESS foreign key (ID_PROCESS)
      references PROCESS (ID_PROCESS) on delete cascade on update cascade
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Crear índices para mejorar el rendimiento
CREATE INDEX IDX_PROCESS_STATUS ON PROCESS(STATUS);
CREATE INDEX IDX_EGSI_ANSWER_PROCESS ON EGSI_ANSWER(ID_PROCESS);
CREATE INDEX IDX_EGSI_ANSWER_QUESTION ON EGSI_ANSWER(ID_QUESTION);
CREATE INDEX IDX_PHASE_APPROVAL_STATUS ON PHASE_APPROVAL_REQUEST(STATUS);
CREATE INDEX IDX_USERS_USERNAME ON USERS(USERNAME);
