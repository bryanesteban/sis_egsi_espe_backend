/*==============================================================*/
/* DBMS name:      SAP SQL Anywhere 16                          */
/* Created on:     9/1/2026 23:09:37                            */
/*==============================================================*/


if exists(select 1 from sys.sysforeignkey where role='FK_ANSWERS_RELATIONS_QUESTION') then
    alter table ANSWERS
       delete foreign key FK_ANSWERS_RELATIONS_QUESTION
end if;

if exists(select 1 from sys.sysforeignkey where role='FK_ANSWERS_RELATIONS_PHASES') then
    alter table ANSWERS
       delete foreign key FK_ANSWERS_RELATIONS_PHASES
end if;

if exists(select 1 from sys.sysforeignkey where role='FK_PHASES_RELATIONS_PROCESS') then
    alter table PHASES
       delete foreign key FK_PHASES_RELATIONS_PROCESS
end if;

if exists(select 1 from sys.sysforeignkey where role='FK_QUESTION_RELATIONS_QUESTION') then
    alter table QUESTION
       delete foreign key FK_QUESTION_RELATIONS_QUESTION
end if;

if exists(select 1 from sys.sysforeignkey where role='FK_RESPONSI_RELATIONS_PHASES') then
    alter table RESPONSIBLES_SIGNING
       delete foreign key FK_RESPONSI_RELATIONS_PHASES
end if;

drop index if exists ANSWERS.RELATIONSHIP_4_FK;

drop index if exists ANSWERS.RELATIONSHIP_3_FK;

drop index if exists ANSWERS.ANSWERS_PK;

drop table if exists ANSWERS;

drop index if exists PHASES.RELATIONSHIP_5_FK;

drop index if exists PHASES.PHASES_PK;

drop table if exists PHASES;

drop index if exists PROCESS.PROCESS_PK;

drop table if exists PROCESS;

drop index if exists QUESTION.RELATIONSHIP_2_FK;

drop index if exists QUESTION.QUESTION_PK;

drop table if exists QUESTION;

drop index if exists QUESTIONARY.QUESTIONARY_PK;

drop table if exists QUESTIONARY;

drop index if exists RESPONSIBLES_SIGNING.RELATIONSHIP_6_FK;

drop index if exists RESPONSIBLES_SIGNING.RESPONSIBLES_SIGNING_PK;

drop table if exists RESPONSIBLES_SIGNING;

drop index if exists USERS.USERS_PK;

drop table if exists USERS;

if exists(select 1 from sys.sysusertype where type_name='UUID') then
   drop domain UUID
end if;

/*==============================================================*/
/* Domain: UUID                                                 */
/*==============================================================*/
create domain UUID as VARCHAR(36) DEFAULT (UUID());

/*==============================================================*/
/* Table: ANSWERS                                               */
/*==============================================================*/
create table ANSWERS 
(
   ID_ANSWER            UUID                           not null,
   ID_QUESTION          UUID                           not null,
   ID_PHASE             UUID                           not null,
   ANSWER_TEXT          varchar(1000)                  null,
   CREATED_AT           varchar(20)                    null,
   UPDATED_AT           varchar(20)                    null,
   ANSWER_TYPE          varchar(20)                    null,
   ANSWER_STATUS        varchar(20)                    null,
   constraint PK_ANSWERS primary key clustered (ID_ANSWER)
);

/*==============================================================*/
/* Index: ANSWERS_PK                                            */
/*==============================================================*/
create unique clustered index ANSWERS_PK on ANSWERS (
ID_ANSWER ASC
);

/*==============================================================*/
/* Index: RELATIONSHIP_3_FK                                     */
/*==============================================================*/
create index RELATIONSHIP_3_FK on ANSWERS (
ID_QUESTION ASC
);

/*==============================================================*/
/* Index: RELATIONSHIP_4_FK                                     */
/*==============================================================*/
create index RELATIONSHIP_4_FK on ANSWERS (
ID_PHASE ASC
);

/*==============================================================*/
/* Table: PHASES                                                */
/*==============================================================*/
create table PHASES 
(
   ID_PHASE             UUID                           not null,
   ID_PROCESS           UUID                           not null,
   QUESTIONARY_CODE     varchar(50)                    not null,
   RESPONSIBLES         varchar(500)                   not null,
   STATUS               varchar(50)                    not null,
   constraint PK_PHASES primary key clustered (ID_PHASE)
);

/*==============================================================*/
/* Index: PHASES_PK                                             */
/*==============================================================*/
create unique clustered index PHASES_PK on PHASES (
ID_PHASE ASC
);

/*==============================================================*/
/* Index: RELATIONSHIP_5_FK                                     */
/*==============================================================*/
create index RELATIONSHIP_5_FK on PHASES (
ID_PROCESS ASC
);

/*==============================================================*/
/* Table: PROCESS                                               */
/*==============================================================*/
create table PROCESS 
(
   ID_PROCESS           UUID                           not null,
   NAME                 varchar(50)                    not null,
   DESCRIPTION          varchar(1000)                  not null,
   DATE_BEGIN           varchar(20)                    not null,
   DATE_END             varchar(20)                    not null,
   ACTIVE               varchar(20)                    not null,
   CURRENT_PHASE        varchar(10)                    not null,
   constraint PK_PROCESS primary key clustered (ID_PROCESS)
);

/*==============================================================*/
/* Index: PROCESS_PK                                            */
/*==============================================================*/
create unique clustered index PROCESS_PK on PROCESS (
ID_PROCESS ASC
);

/*==============================================================*/
/* Table: QUESTION                                              */
/*==============================================================*/
create table QUESTION 
(
   ID_QUESTION          UUID                           not null,
   ID_QUESTIONARY       varchar(36)                    not null,
   DESCRIPTION          varchar(1000)                  null,
   QUESTION_TYPE        varchar(50)                    null,
   QUESTION_JSON        varchar(1000)                  null,
   constraint PK_QUESTION primary key clustered (ID_QUESTION)
);

/*==============================================================*/
/* Index: QUESTION_PK                                           */
/*==============================================================*/
create unique clustered index QUESTION_PK on QUESTION (
ID_QUESTION ASC
);

/*==============================================================*/
/* Index: RELATIONSHIP_2_FK                                     */
/*==============================================================*/
create index RELATIONSHIP_2_FK on QUESTION (
ID_QUESTIONARY ASC
);

/*==============================================================*/
/* Table: QUESTIONARY                                           */
/*==============================================================*/
create table QUESTIONARY 
(
   ID_QUESTIONARY       varchar(36)                    not null,
   QUESTIONARY_NAME     varchar(50)                    not null,
   DESCRIPTION          varchar(1000)                  not null,
   PHASE                varchar(10)                    not null,
   constraint PK_QUESTIONARY primary key clustered (ID_QUESTIONARY)
);

/*==============================================================*/
/* Index: QUESTIONARY_PK                                        */
/*==============================================================*/
create unique clustered index QUESTIONARY_PK on QUESTIONARY (
ID_QUESTIONARY ASC
);

/*==============================================================*/
/* Table: RESPONSIBLES_SIGNING                                  */
/*==============================================================*/
create table RESPONSIBLES_SIGNING 
(
   ID_RESPONSIBLE       UUID                           not null,
   ID_PHASE             UUID                           not null,
   NAME_RESPONSIBLE     varchar(100)                   not null,
   STATUS_SIGN          varchar(50)                    not null,
   CREATED_AT           varchar(20)                    not null,
   UPDATED_AT           varchar(20)                    not null,
   constraint PK_RESPONSIBLES_SIGNING primary key clustered (ID_RESPONSIBLE)
);

/*==============================================================*/
/* Index: RESPONSIBLES_SIGNING_PK                               */
/*==============================================================*/
create unique clustered index RESPONSIBLES_SIGNING_PK on RESPONSIBLES_SIGNING (
ID_RESPONSIBLE ASC
);

/*==============================================================*/
/* Index: RELATIONSHIP_6_FK                                     */
/*==============================================================*/
create index RELATIONSHIP_6_FK on RESPONSIBLES_SIGNING (
ID_PHASE ASC
);

/*==============================================================*/
/* Table: USERS                                                 */
/*==============================================================*/
create table USERS 
(
   ID_USER              UUID                           not null,
   NAME                 varchar(50)                    not null,
   LASTNAME             varchar(50)                    not null,
   CI                   varchar(50)                    not null,
   USERNAME             varchar(50)                    not null,
   PASSWORD             varchar(200)                   not null,
   ROL                  varchar(50)                    not null,
   IS_DELETED           smallint                       not null,
   constraint PK_USERS primary key clustered (ID_USER)
);

/*==============================================================*/
/* Index: USERS_PK                                              */
/*==============================================================*/
create unique clustered index USERS_PK on USERS (
ID_USER ASC
);

alter table ANSWERS
   add constraint FK_ANSWERS_RELATIONS_QUESTION foreign key (ID_QUESTION)
      references QUESTION (ID_QUESTION)
      on update restrict
      on delete restrict;

alter table ANSWERS
   add constraint FK_ANSWERS_RELATIONS_PHASES foreign key (ID_PHASE)
      references PHASES (ID_PHASE)
      on update restrict
      on delete restrict;

alter table PHASES
   add constraint FK_PHASES_RELATIONS_PROCESS foreign key (ID_PROCESS)
      references PROCESS (ID_PROCESS)
      on update restrict
      on delete restrict;

alter table QUESTION
   add constraint FK_QUESTION_RELATIONS_QUESTION foreign key (ID_QUESTIONARY)
      references QUESTIONARY (ID_QUESTIONARY)
      on update restrict
      on delete restrict;

alter table RESPONSIBLES_SIGNING
   add constraint FK_RESPONSI_RELATIONS_PHASES foreign key (ID_PHASE)
      references PHASES (ID_PHASE)
      on update restrict
      on delete restrict;

