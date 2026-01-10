/*==============================================================*/
/* DBMS name:      MySQL 5.0                                    */
/* Created on:     1/1/2026 20:25:39                            */
/*==============================================================*/


drop table if exists ANEXOS_PLAN;

drop table if exists ANEXO_EGSI;

drop table if exists CONTROL_VERSION;

drop table if exists DECLARATION_APLICATION_SOA;

drop table if exists METHODOLOGY_EVALUATION;

drop table if exists PERFORMANCE_MONITORING_REPORT;

drop table if exists PLAN_FOR_INTERNAL_EVALUATION;

drop table if exists PLAN_OF_COMUNICATION;

drop table if exists PROCESS;

drop table if exists REPORT_EVALUATION_AND_TREATMENT_RISK;

drop table if exists RESPONSIBLE_FIRMS;

drop table if exists RISK_ITEM;

drop table if exists RISK_TREATAMENT_PLAN;

drop table if exists SCHEDULE;

drop table if exists SCOPE;

drop table if exists SECURITY_POLICIES;

drop table if exists USERS;

/*==============================================================*/
/* Table: ANEXOS_PLAN                                           */
/*==============================================================*/
create table ANEXOS_PLAN
(
   ID_ANEXO             VARCHAR(36) DEFAULT (UUID()) not null,
   ID_PLAN_INTERAL_EVAL VARCHAR(36) DEFAULT (UUID()),
   SECCION_HITO         varchar(10),
   DESCRIPTION          varchar(500),
   DESCRIPTION_FINDING_FOUND varchar(200),
   CURRENT              varchar(10),
   FORMALIZED           varchar(10),
   IMPLEMENTED          varchar(10),
   PERCENTAGE_TOTAL_ACHIEVED varchar(10),
   primary key (ID_ANEXO)
);

/*==============================================================*/
/* Table: ANEXO_EGSI                                            */
/*==============================================================*/
create table ANEXO_EGSI
(
   ID_ANEXO_EGSI        VARCHAR(36) DEFAULT (UUID()) not null,
   ID_DECLARATION       VARCHAR(36) DEFAULT (UUID()),
   ITEM                 varchar(10),
   SECTION              varchar(10),
   DESCRIPTION          varchar(500),
   ACTUAL_STATE         varchar(200),
   APPLIES              varchar(200),
   SELECTION_JUSTIFY    varchar(500),
   OBSERVATION          varchar(500),
   primary key (ID_ANEXO_EGSI)
);

/*==============================================================*/
/* Table: CONTROL_VERSION                                       */
/*==============================================================*/
create table CONTROL_VERSION
(
   ID_CONTROL_VERSION   VARCHAR(36) DEFAULT (UUID()) not null,
   ID_DOCUMENT          varchar(20) not null,
   VERSION              varchar(20) not null,
   DATE_VERSION         varchar(20) not null,
   CREATE_FOR           varchar(100) not null,
   APROBE_FOR           varchar(100) not null,
   NIVEL_CONFIDENTIALITY varchar(20) not null,
   primary key (ID_CONTROL_VERSION)
);

/*==============================================================*/
/* Table: DECLARATION_APLICATION_SOA                            */
/*==============================================================*/
create table DECLARATION_APLICATION_SOA
(
   ID_DECLARATION       VARCHAR(36) DEFAULT (UUID()) not null,
   ID_PROCESS           VARCHAR(36) DEFAULT (UUID()) not null,
   INSTITUTE            varchar(50),
   NUMBER_CONTROLLER    varchar(50),
   LEVEL_CONFIDENTIALITY varchar(50),
   DOCUMENT_RESPONSIBLE varchar(100),
   primary key (ID_DECLARATION)
);

/*==============================================================*/
/* Table: METHODOLOGY_EVALUATION                                */
/*==============================================================*/
create table METHODOLOGY_EVALUATION
(
   ID_METHODOLOGY_EVAL  VARCHAR(36) DEFAULT (UUID()) not null,
   ID_PROCESS           VARCHAR(36) DEFAULT (UUID()) not null,
   OBJETIVE             varchar(500),
   SCOPE                varchar(1000),
   USERS                varchar(500),
   PROCESS_METHODOLOGY  varchar(1000),
   ASSETS_METHODOLOGY   varchar(1000),
   IDENTIFICATION_METHODOLOGY varchar(1000),
   IMPACT_PROBABILITY   varchar(1000),
   ACCEPTANCE_CRITERIA_METHODOLOGY varchar(1000),
   RISK_TREATMENT_METHODOLOGY varchar(1000),
   REVISION_PERIOD_METHODOLOGY varchar(1000),
   DECLARATION_APPLYCABILITY_METHODOLOGY varchar(1000),
   REPORTS_METHODOLOGY  varchar(1000),
   VALIDITY_MANAGEMENT_METHODOLOGY varchar(1000),
   NOTES_METHODOLOGY    varchar(1000),
   primary key (ID_METHODOLOGY_EVAL)
);

/*==============================================================*/
/* Table: PERFORMANCE_MONITORING_REPORT                         */
/*==============================================================*/
create table PERFORMANCE_MONITORING_REPORT
(
   ID_MONITORING_REPORT VARCHAR(36) DEFAULT (UUID()) not null,
   ID_PROCESS           VARCHAR(36) DEFAULT (UUID()) not null,
   SCOPE                varchar(1000),
   DOCUMENT_OBJETIVE    varchar(500),
   METHODOLOGY_MONITORING varchar(500),
   OBJETIVES            varchar(500),
   INDICATORS_DEFINITIONS varchar(1000),
   KEY_INDICATORS       varchar(1000),
   MONITORING_RESULT    varchar(1000),
   EVALUATION_VALIDATION varchar(1000),
   CONCLUSION_RECOMENDATIONS varchar(1000),
   REFERENCES_TEXT      varchar(500),
   primary key (ID_MONITORING_REPORT)
);

/*==============================================================*/
/* Table: PLAN_FOR_INTERNAL_EVALUATION                          */
/*==============================================================*/
create table PLAN_FOR_INTERNAL_EVALUATION
(
   ID_PLAN_INTERAL_EVAL VARCHAR(36) DEFAULT (UUID()) not null,
   ID_PROCESS           VARCHAR(36) DEFAULT (UUID()) not null,
   BACKGROUND           varchar(1000),
   DOCUMENT_OBJETIVE    varchar(500),
   SCOPE_PLAN           varchar(1000),
   CRITERION_PLAN       varchar(1000),
   METHODOLOGY_PLAN     varchar(1000),
   CRONOGRAM_PLAN       varchar(1000),
   RESOURCE_PLAN        varchar(1000),
   DOCUMENTATION_FINDINGS_PLAN varchar(1000),
   DOCUMENTATION_FINDINGS_PLAN_TABLE varchar(1000),
   RESULTS_PLAN         varchar(1000),
   GLOSARY_TERMS_TABLE  varchar(1000),
   DOCUMENTS_REFERENCE  varchar(1000),
   CONTROL_VERSION      varchar(1000),
   CHANGE_HISTORIAL     varchar(1000),
   SCHEDULE_WORKING     varchar(1000),
   primary key (ID_PLAN_INTERAL_EVAL)
);

/*==============================================================*/
/* Table: PLAN_OF_COMUNICATION                                  */
/*==============================================================*/
create table PLAN_OF_COMUNICATION
(
   ID_PLAN_COMU         VARCHAR(36) DEFAULT (UUID()) not null,
   ID_PROCESS           VARCHAR(36) DEFAULT (UUID()) not null,
   BACKGROUND           varchar(1000) not null,
   OBJETIVE_OF_PLAN     varchar(1000) not null,
   SPECIFIC_OBJETIVES   varchar(1000) not null,
   PLAN_DESCRIPTION     varchar(1000) not null,
   SCOPE_PLAN           varchar(1000) not null,
   DESING_PLAN          varchar(1000) not null,
   MATERIALS_AND_CHANNELS varchar(1000) not null,
   STRATEGIES_PLAN      varchar(1000) not null,
   NEEDS_IDENTIFICATIONS varchar(1000) not null,
   ROLES_AND_RESPONSABILITIES varchar(1000) not null,
   RESULTS_AND_ACHIEVEMENTS varchar(1000) not null,
   SCHEDULE_ACTIVITIES  varchar(1000) not null,
   GLOSARY_TERMS        varchar(1000) not null,
   DOCUMENTS_OF_REFERENCE varchar(1000) not null,
   STATE                varchar(20) not null,
   primary key (ID_PLAN_COMU)
);

/*==============================================================*/
/* Table: PROCESS                                               */
/*==============================================================*/
create table PROCESS
(
   ID_PROCESS           VARCHAR(36) DEFAULT (UUID()) not null,
   RELATED_PROGRAM      varchar(200) not null,
   PROJECT_LEADER       varchar(200) not null,
   DATE_BEGIN           varchar(20) not null,
   DATE_END             varchar(20) not null,
   PROBLEM              varchar(1000) not null,
   JUSTIFICATION        varchar(1000) not null,
   PROJECT_DESCRIPTION  varchar(1000) not null,
   BENEFITS             varchar(500) not null,
   GENERAL_OBJECTIVE    varchar(500) not null,
   BENEFICIARIES        varchar(500) not null,
   IMPLETENTATION_PERIOD varchar(200) not null,
   primary key (ID_PROCESS)
);

/*==============================================================*/
/* Table: REPORT_EVALUATION_AND_TREATMENT_RISK                  */
/*==============================================================*/
create table REPORT_EVALUATION_AND_TREATMENT_RISK
(
   ID_REPORT_TREATMENT  VARCHAR(36) DEFAULT (UUID()) not null,
   ID_PROCESS           VARCHAR(36) DEFAULT (UUID()) not null,
   OBJETIVE             varchar(500),
   DESCRIPTION          varchar(500),
   CONTEXT_EVALUATION   varchar(1000),
   METHODOLOGY_EVALUATION varchar(1000),
   IDENTIFICATION_VALORATION_ASSETS varchar(1000),
   IDENTIFICATION_RISK_AND_VULNERABILITIES varchar(1000),
   IDENTIFICATION_CONTROL varchar(1000),
   EVALUATION_RISK      varchar(1000),
   TREATMENT_RISK       varchar(1000),
   NIVEL_RISK           varchar(1000),
   CLARIFICATION_NOTE   varchar(1000),
   primary key (ID_REPORT_TREATMENT)
);

/*==============================================================*/
/* Table: RESPONSIBLE_FIRMS                                     */
/*==============================================================*/
create table RESPONSIBLE_FIRMS
(
   ID_FIRMS             VARCHAR(36) DEFAULT (UUID()) not null,
   ID_DOCUMENT          varchar(20) not null,
   RESPONSIBLES         varchar(500) not null,
   primary key (ID_FIRMS)
);

/*==============================================================*/
/* Table: RISK_ITEM                                             */
/*==============================================================*/
create table RISK_ITEM
(
   ID_RISK              VARCHAR(36) DEFAULT (UUID()) not null,
   ID_PLAN_TRATAMIENT   VARCHAR(36) DEFAULT (UUID()),
   RISK_ID_LITERAL      varchar(20),
   TYPE_ASSET           varchar(20),
   RISK_LEVEL           varchar(20),
   TREATMENT_OPTION     varchar(20),
   CONTROLS_FOR_IMPLEMENTATION varchar(500),
   DESCRIPTION_ACTIVITIES varchar(500),
   IMPLEMENTATION_RESPONSABILITY varchar(500),
   FUNTIONAL_AREA       varchar(50),
   CML_STATE            varchar(20),
   DATE_BEGIN           varchar(20),
   DATE_END             varchar(20),
   HOUR_DAY             varchar(20),
   MONEY                varchar(20),
   primary key (ID_RISK)
);

/*==============================================================*/
/* Table: RISK_TREATAMENT_PLAN                                  */
/*==============================================================*/
create table RISK_TREATAMENT_PLAN
(
   ID_PLAN_TRATAMIENT   VARCHAR(36) DEFAULT (UUID()) not null,
   ID_PROCESS           VARCHAR(36) DEFAULT (UUID()) not null,
   INSTITUTION          varchar(100),
   CONFIDENTIALITY_LEVEL varchar(100),
   DATE_LAST_MODIFICATION varchar(100),
   DOCUMENT_RESPONSIBLE varchar(100),
   primary key (ID_PLAN_TRATAMIENT)
);

/*==============================================================*/
/* Table: SCHEDULE                                              */
/*==============================================================*/
create table SCHEDULE
(
   ID_SCHEDULE          VARCHAR(36) DEFAULT (UUID()) not null,
   ID_PROCESS           VARCHAR(36) DEFAULT (UUID()) not null,
   HITO_CONTROL         varchar(500),
   COMMITTED_DATE       varchar(20),
   ESTIMATED_DATE       varchar(20),
   PERCENTAGE_PROGRESS  varchar(20),
   primary key (ID_SCHEDULE)
);

/*==============================================================*/
/* Table: SCOPE                                                 */
/*==============================================================*/
create table SCOPE
(
   ID_SCOPE             VARCHAR(36) DEFAULT (UUID()) not null,
   ID_PROCESS           VARCHAR(36) DEFAULT (UUID()) not null,
   SCOPE_DESCRIPTION    varchar(500) not null,
   OBJETIVE_SCOPE       varchar(500) not null,
   USERS_SCOPE          varchar(500) not null,
   REVIEW_AND_UPDATE    varchar(500) not null,
   REFERENCE_DOCUMENTS  varchar(500) not null,
   SCOPE_DEFINITION     varchar(500) not null,
   PROCESS_AND_SERVICES varchar(2000) not null,
   ORGANIZATION_UNITS   varchar(500) not null,
   UBICATION_DESCRIPTION varchar(500) not null,
   UBICATION_TABLE      varchar(2000) not null,
   NETWORKS_AND_INFRAESTRUCTURE_TI varchar(500) not null,
   EXCLUSION            varchar(500) not null,
   STATE                varchar(20) not null,
   primary key (ID_SCOPE)
);

/*==============================================================*/
/* Table: SECURITY_POLICIES                                     */
/*==============================================================*/
create table SECURITY_POLICIES
(
   ID_SECURITY_POLICIES VARCHAR(36) DEFAULT (UUID()) not null,
   ID_PROCESS           VARCHAR(36) DEFAULT (UUID()) not null,
   BACKGROUNDS          varchar(1000),
   POLICY_OBJETIVE      varchar(1000),
   DESCRIPTION_POLICY   varchar(1000),
   DECLARATION_OBJETIVE_SECURITY varchar(1000),
   ROLES_AND_RESPONSABILITIES varchar(1000),
   SCOPE_AND_USERS      varchar(1000),
   COMUNICATION_POLICY  varchar(1000),
   EXCEPTION_SANCTIONS  varchar(1000),
   GLOSARY_TERMS_TABLE  varchar(1000),
   REFERENCE_DOCUMENTS  varchar(500),
   primary key (ID_SECURITY_POLICIES)
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

alter table ANEXOS_PLAN add constraint FK_RELATIONSHIP_5 foreign key (ID_PLAN_INTERAL_EVAL)
      references PLAN_FOR_INTERNAL_EVALUATION (ID_PLAN_INTERAL_EVAL) on delete restrict on update restrict;

alter table ANEXO_EGSI add constraint FK_RELATIONSHIP_11 foreign key (ID_DECLARATION)
      references DECLARATION_APLICATION_SOA (ID_DECLARATION) on delete restrict on update restrict;

alter table DECLARATION_APLICATION_SOA add constraint FK_RELATIONSHIP_10 foreign key (ID_PROCESS)
      references PROCESS (ID_PROCESS) on delete restrict on update restrict;

alter table METHODOLOGY_EVALUATION add constraint FK_RELATIONSHIP_8 foreign key (ID_PROCESS)
      references PROCESS (ID_PROCESS) on delete restrict on update restrict;

alter table PERFORMANCE_MONITORING_REPORT add constraint FK_RELATIONSHIP_14 foreign key (ID_PROCESS)
      references PROCESS (ID_PROCESS) on delete restrict on update restrict;

alter table PLAN_FOR_INTERNAL_EVALUATION add constraint FK_RELATIONSHIP_6 foreign key (ID_PROCESS)
      references PROCESS (ID_PROCESS) on delete restrict on update restrict;

alter table PLAN_OF_COMUNICATION add constraint FK_RELATIONSHIP_4 foreign key (ID_PROCESS)
      references PROCESS (ID_PROCESS) on delete restrict on update restrict;

alter table REPORT_EVALUATION_AND_TREATMENT_RISK add constraint FK_RELATIONSHIP_9 foreign key (ID_PROCESS)
      references PROCESS (ID_PROCESS) on delete restrict on update restrict;

alter table RISK_ITEM add constraint FK_RELATIONSHIP_12 foreign key (ID_PLAN_TRATAMIENT)
      references RISK_TREATAMENT_PLAN (ID_PLAN_TRATAMIENT) on delete restrict on update restrict;

alter table RISK_TREATAMENT_PLAN add constraint FK_RELATIONSHIP_13 foreign key (ID_PROCESS)
      references PROCESS (ID_PROCESS) on delete restrict on update restrict;

alter table SCHEDULE add constraint FK_RELATIONSHIP_1 foreign key (ID_PROCESS)
      references PROCESS (ID_PROCESS) on delete restrict on update restrict;

alter table SCOPE add constraint FK_RELATIONSHIP_3 foreign key (ID_PROCESS)
      references PROCESS (ID_PROCESS) on delete restrict on update restrict;

alter table SECURITY_POLICIES add constraint FK_RELATIONSHIP_7 foreign key (ID_PROCESS)
      references PROCESS (ID_PROCESS) on delete restrict on update restrict;

/*==============================================================*/

/**
*  Initial Data for table USERS
*  Username: cDaroma
*  Password: password (BCrypt hash)
*/
INSERT INTO users (id_user, name, lastname, ci, username, password, rol, is_deleted) VALUES (UUID(), 'Carlos', 'Daroma', '123215432', 'cDaroma', '$2a$10$vdKkWcsR65dXjN.bOk/mnu1vSxX6axWXLXr/dP0fOF4gcCQQwSkFu', 'ADMIN', 0);


