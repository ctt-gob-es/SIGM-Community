CREATE TABLE IUSERUSERHDR_WS
(
  NAME           VARCHAR(32)                  NULL,
  SIGN_REQUIRED  integer                          NULL,
  ID             VARCHAR(32)                  NULL
);


CREATE TABLE QRTZ_BLOB_TRIGGERS
(
  TRIGGER_NAME   VARCHAR(200 )                 NULL,
  TRIGGER_GROUP  VARCHAR(200 )                 NULL,
  bytea_DATA      bytea                               NULL
);


CREATE TABLE QRTZ_CALENDARS
(
  CALENDAR_NAME  VARCHAR(200 )                 NULL,
  CALENDAR       bytea                               NULL
);


CREATE TABLE QRTZ_CRON_TRIGGERS
(
  TRIGGER_NAME     VARCHAR(200 )               NULL,
  TRIGGER_GROUP    VARCHAR(200 )               NULL,
  CRON_EXPRESSION  VARCHAR(120 )               NULL,  TIME_ZONE_ID     VARCHAR(80 )                NULL
);


CREATE TABLE QRTZ_FIRED_TRIGGERS
(
  ENTRY_ID           VARCHAR(95 )              NULL,
  TRIGGER_NAME       VARCHAR(200 )             NULL,
  TRIGGER_GROUP      VARCHAR(200 )             NULL,
  IS_VOLATILE        boolean               NULL,
  INSTANCE_NAME      VARCHAR(200 )             NULL,
  FIRED_TIME         bigint                     NULL,
  PRIORITY           bigint                     NULL,
  STATE              VARCHAR(16 )              NULL,
  JOB_NAME           VARCHAR(200 )             NULL,
  JOB_GROUP          VARCHAR(200 )             NULL,
  IS_STATEFUL        boolean               NULL,
  REQUESTS_RECOVERY  boolean               NULL
);


CREATE TABLE QRTZ_JOB_DETAILS
(
  JOB_NAME           VARCHAR(200 )             NULL,
  JOB_GROUP          VARCHAR(200 )             NULL,
  DESCRIPTION        VARCHAR(250 )             NULL,
  JOB_CLASS_NAME     VARCHAR(250 )             NULL,
  IS_DURABLE         boolean               NULL,
  IS_VOLATILE        boolean	               NULL,
  IS_STATEFUL        boolean               NULL,
  REQUESTS_RECOVERY  boolean               NULL,
  JOB_DATA           bytea                           NULL
);


CREATE TABLE QRTZ_JOB_LISTENERS
(
  JOB_NAME      VARCHAR(200 )                  NULL,
  JOB_GROUP     VARCHAR(200 )                  NULL,
  JOB_LISTENER  VARCHAR(200 )                  NULL
);


CREATE TABLE QRTZ_LOCKS
(
  LOCK_NAME  VARCHAR(40 )                      NULL
);


CREATE TABLE QRTZ_PAUSED_TRIGGER_GRPS
(
  TRIGGER_GROUP  VARCHAR(200 )                 NULL
);


CREATE TABLE QRTZ_SCHEDULER_STATE
(
  INSTANCE_NAME      VARCHAR(200 )             NULL,
  LAST_CHECKIN_TIME  integer                     NULL,
  CHECKIN_INTERVAL   integer                     NULL
);


CREATE TABLE QRTZ_SIMPLE_TRIGGERS
(
  TRIGGER_NAME     VARCHAR(200 )               NULL,
  TRIGGER_GROUP    VARCHAR(200 )               NULL,
  REPEAT_COUNT     integer                        NULL,
  REPEAT_INTERVAL  integer                       NULL,
  TIMES_TRIGGERED  integer                       NULL
);


CREATE TABLE QRTZ_TRIGGERS
(
  TRIGGER_NAME    VARCHAR(200 )                NULL,
  TRIGGER_GROUP   VARCHAR(200 )                NULL,
  JOB_NAME        VARCHAR(200 )                NULL,
  JOB_GROUP       VARCHAR(200 )                NULL,
  IS_VOLATILE     boolean	               NULL,
  DESCRIPTION     VARCHAR(250 )                NULL,
  NEXT_FIRE_TIME  bigint                        NULL,
  PREV_FIRE_TIME  bigint                        NULL,
  PRIORITY        integer                        NULL,
  TRIGGER_STATE   VARCHAR(16 )                 NULL,
  TRIGGER_TYPE    VARCHAR(8 )                  NULL,
  START_TIME      bigint                        NULL,
  END_TIME        bigint                        NULL,
  CALENDAR_NAME   VARCHAR(200 )                NULL,
  MISFIRE_INSTR   integer                         NULL,
  JOB_DATA        bytea                              NULL
);


CREATE TABLE QRTZ_TRIGGER_LISTENERS
(
  TRIGGER_NAME      VARCHAR(200 )              NULL,
  TRIGGER_GROUP     VARCHAR(200 )              NULL,
  TRIGGER_LISTENER  VARCHAR(200 )              NULL
);


CREATE TABLE SCR_CCAA
(
  ID       VARCHAR(2)                         NULL,
  NAME     VARCHAR(50)                        NULL,
  TMSTAMP  DATE                                     NULL
);


CREATE TABLE XT_FIELD
(
  FLDID        integer                           NULL,
  NAME         VARCHAR(32)                    NULL,
  DESCRIPCION  VARCHAR(256)                   NULL,
  SECCION      VARCHAR(32)                    NULL
);


CREATE TABLE VERSION
(
  VERSION      VARCHAR(20),
  VERSIONDATE  DATE
);


ALTER TABLE SCR_ORGS ADD COLUMN HIERARCHICAL_LEVEL INTEGER;
ALTER TABLE SCR_ORGS ADD COLUMN ADMIN_LEVEL VARCHAR(2);
ALTER TABLE SCR_ORGS ADD COLUMN ENTITY_TYPE VARCHAR(2);
ALTER TABLE SCR_ORGS ADD COLUMN UO_TYPE VARCHAR(3);
ALTER TABLE SCR_ORGS ADD COLUMN ID_ROOT INTEGER;
ALTER TABLE SCR_ORGS ADD COLUMN ID_CCAA VARCHAR(2);
ALTER TABLE SCR_ORGS ADD COLUMN ID_PROV INTEGER;

ALTER TABLE IUSERDEPTHDR ADD IDORG INTEGER;

ALTER TABLE SCR_PAGEREPOSITORY ADD COLUMN DELETEDATE DATE;

ALTER TABLE SCR_PAGEREPOSITORY ADD COLUMN FLAG INTEGER;


ALTER TABLE IUSERUSERHDR_WS ADD CONSTRAINT IUSERUSERHDR_WS_NAME_UQ UNIQUE (NAME);

ALTER TABLE QRTZ_CALENDARS ADD PRIMARY KEY (CALENDAR_NAME);

ALTER TABLE QRTZ_FIRED_TRIGGERS ADD PRIMARY KEY (ENTRY_ID);

ALTER TABLE QRTZ_JOB_DETAILS ADD PRIMARY KEY (JOB_NAME, JOB_GROUP);

ALTER TABLE QRTZ_JOB_LISTENERS ADD PRIMARY KEY (JOB_NAME, JOB_GROUP, JOB_LISTENER);

ALTER TABLE QRTZ_LOCKS ADD PRIMARY KEY (LOCK_NAME);

ALTER TABLE QRTZ_PAUSED_TRIGGER_GRPS ADD PRIMARY KEY (TRIGGER_GROUP);

ALTER TABLE QRTZ_SCHEDULER_STATE ADD PRIMARY KEY (INSTANCE_NAME);

ALTER TABLE QRTZ_TRIGGERS ADD PRIMARY KEY (TRIGGER_NAME, TRIGGER_GROUP);

ALTER TABLE QRTZ_TRIGGER_LISTENERS ADD PRIMARY KEY (TRIGGER_NAME, TRIGGER_GROUP, TRIGGER_LISTENER);

ALTER TABLE SCR_CCAA ADD CONSTRAINT SCR_CCAA_PK PRIMARY KEY (ID);

ALTER TABLE XT_FIELD ADD CONSTRAINT XT_FIELD_PK PRIMARY KEY (FLDID);

ALTER TABLE XT_FIELD ADD CONSTRAINT XT_FIELD_NAME_UQ UNIQUE (NAME);

ALTER TABLE QRTZ_BLOB_TRIGGERS ADD PRIMARY KEY (TRIGGER_NAME, TRIGGER_GROUP);

ALTER TABLE QRTZ_CRON_TRIGGERS ADD PRIMARY KEY (TRIGGER_NAME, TRIGGER_GROUP);

ALTER TABLE QRTZ_SIMPLE_TRIGGERS ADD PRIMARY KEY (TRIGGER_NAME, TRIGGER_GROUP);

ALTER TABLE QRTZ_JOB_LISTENERS ADD FOREIGN KEY (JOB_NAME, JOB_GROUP) REFERENCES QRTZ_JOB_DETAILS (JOB_NAME,JOB_GROUP);

ALTER TABLE QRTZ_TRIGGERS ADD FOREIGN KEY (JOB_NAME, JOB_GROUP) REFERENCES QRTZ_JOB_DETAILS (JOB_NAME,JOB_GROUP);

ALTER TABLE QRTZ_TRIGGER_LISTENERS ADD FOREIGN KEY (TRIGGER_NAME, TRIGGER_GROUP) REFERENCES QRTZ_TRIGGERS (TRIGGER_NAME,TRIGGER_GROUP);

ALTER TABLE QRTZ_BLOB_TRIGGERS ADD FOREIGN KEY (TRIGGER_NAME, TRIGGER_GROUP) REFERENCES QRTZ_TRIGGERS (TRIGGER_NAME,TRIGGER_GROUP);

ALTER TABLE QRTZ_CRON_TRIGGERS ADD FOREIGN KEY (TRIGGER_NAME, TRIGGER_GROUP) REFERENCES QRTZ_TRIGGERS (TRIGGER_NAME,TRIGGER_GROUP);

ALTER TABLE QRTZ_SIMPLE_TRIGGERS ADD FOREIGN KEY (TRIGGER_NAME, TRIGGER_GROUP) REFERENCES QRTZ_TRIGGERS (TRIGGER_NAME,TRIGGER_GROUP);

ALTER TABLE SCR_ORGS ADD CONSTRAINT SCR_ORGS_R01 FOREIGN KEY (ID_FATHER) REFERENCES SCR_ORGS (ID);

ALTER TABLE SCR_ORGS ADD CONSTRAINT SCR_ORGS_R02 FOREIGN KEY (ID_ROOT) REFERENCES SCR_ORGS (ID);

ALTER TABLE SCR_ORGS ADD CONSTRAINT SCR_ORGS_R03 FOREIGN KEY (ID_PROV) REFERENCES SCR_PROV (ID);

ALTER TABLE SCR_ORGS ADD CONSTRAINT SCR_ORGS_R04 FOREIGN KEY (ID_CCAA) REFERENCES SCR_CCAA (ID);

ALTER TABLE IUSERDEPTHDR ADD CONSTRAINT IUSERDEPTHDR_R01 FOREIGN KEY (IDORG) REFERENCES SCR_ORGS (ID);



CREATE SEQUENCE IUSERUSERHDR_SEQ
  START WITH 294
  MAXVALUE 9223372036854775807
  MINVALUE 1
  CACHE 20 ;


CREATE SEQUENCE SCR_IUSERDEPTHDR
  START WITH 285
  MAXVALUE 9223372036854775807
  MINVALUE 1
  CACHE 20;


CREATE SEQUENCE SCR_ORGS_SEQ
  START WITH 60841
  MAXVALUE 9223372036854775807
  MINVALUE 1
  CACHE 20;

CREATE SEQUENCE SCR_SEQSCR_ORGS
  START WITH 60841
  MAXVALUE 9223372036854775807
  MINVALUE 1
  CACHE 20;

CREATE SEQUENCE SCR_TT_SEQ
  START WITH 9
  MAXVALUE 9223372036854775807
  MINVALUE 1
  CACHE 20;

  
CREATE SEQUENCE SCR_USROFIC_SEQ
  START WITH 20
  MAXVALUE 9223372036854775807
  MINVALUE 1
  CACHE 20;

CREATE SEQUENCE SEQSCR_ORGS
  START WITH 60861
  MAXVALUE 9223372036854775807
  MINVALUE 1
  CACHE 20;


CREATE SEQUENCE SEQ_XT_FIELD
  START WITH 1
  MAXVALUE 9223372036854775807
  MINVALUE 1;

  CREATE SEQUENCE WS_REQUEST_RESULTS_SEQ
  START WITH 6243
  MAXVALUE 9223372036854775807
  MINVALUE 1;


/* Formatted on 29/06/2016 11:17:03 (QP5 v5.139.911.3011) */
CREATE OR REPLACE  VIEW SCR_REGDIST
(
   IDBOOK,
   FDRID,
   FLD1,
   FLD2,
   FLD7,
   FLD8,
   FLD9,
   FLD16,
   FLD17
)
AS
   SELECT 1 AS IDBOOK,
          FDRID,
          FLD1,
          FLD2,
          FLD7,
          FLD8,
          FLD9,
          FLD16,
          FLD17
     FROM A1SF INNER JOIN SCR_DISTREG ON FDRID = ID_FDR AND ID_ARCH = 1
   UNION
   SELECT 2 AS IDBOOK,
          FDRID,
          FLD1,
          FLD2,
          FLD7,
          FLD8,
          FLD9,
          FLD12 AS FLD16,
          FLD13 AS FLD17
     FROM A2SF INNER JOIN SCR_DISTREG ON FDRID = ID_FDR AND ID_ARCH = 2;


 --Insert into QRTZ_JOB_DETAILS (JOB_NAME, JOB_GROUP, DESCRIPTION, JOB_CLASS_NAME, IS_DURABLE, IS_VOLATILE, IS_STATEFUL, REQUESTS_RECOVERY, JOB_DATA)
 --Values ('updateStatesSIRJobSigemImpl', 'DEFAULT', NULL, 'es.msssi.sgm.registropresencial.jobs.UpdateStatesSIRJobSigem', 'false', 'false', 'false', 'false', null);
 --Insert into QRTZ_JOB_DETAILS (JOB_NAME, JOB_GROUP, DESCRIPTION, JOB_CLASS_NAME, IS_DURABLE, IS_VOLATILE, IS_STATEFUL, REQUESTS_RECOVERY, JOB_DATA)
 --Values ('unlockRegisterJobSigemImpl', 'DEFAULT', NULL, 'es.msssi.sgm.registropresencial.jobs.UnlockRegisterJobSigem', 'false', 'false', 'false', 'false', null);

 --Insert into QRTZ_TRIGGERS (TRIGGER_NAME, TRIGGER_GROUP, JOB_NAME, JOB_GROUP, IS_VOLATILE, DESCRIPTION, NEXT_FIRE_TIME, PREV_FIRE_TIME, PRIORITY, TRIGGER_STATE, TRIGGER_TYPE, START_TIME, END_TIME, CALENDAR_NAME, MISFIRE_INSTR, JOB_DATA)
--Values ('unlockRegisterJobSigemTrigger', 'DEFAULT', 'unlockRegisterJobSigemImpl', 'DEFAULT', 'false', NULL, 2147483647, 2147483647, 5, 'WAITING', 'CRON', 2147483647, 0, NULL, 0, NULL);
--Insert into QRTZ_TRIGGERS (TRIGGER_NAME, TRIGGER_GROUP, JOB_NAME, JOB_GROUP, IS_VOLATILE, DESCRIPTION, NEXT_FIRE_TIME, PREV_FIRE_TIME, PRIORITY, TRIGGER_STATE, TRIGGER_TYPE, START_TIME, END_TIME, CALENDAR_NAME, MISFIRE_INSTR, JOB_DATA)
 --Values ('updateStatesSIRJobSigemTrigger', 'DEFAULT', 'updateStatesSIRJobSigemImpl', 'DEFAULT', 'false', NULL, 2147483647, 2147483647, 5, 'WAITING', 'CRON', 2147483647, 0, NULL, 0, NULL);

Insert into QRTZ_CRON_TRIGGERS (TRIGGER_NAME, TRIGGER_GROUP, CRON_EXPRESSION, TIME_ZONE_ID)
 Values ('unlockRegisterJobSigemTrigger', 'DEFAULT', '0 0/5 * * * ?', 'Europe/Paris');
Insert into QRTZ_CRON_TRIGGERS (TRIGGER_NAME, TRIGGER_GROUP, CRON_EXPRESSION, TIME_ZONE_ID)
 Values ('updateStatesSIRJobSigemTrigger', 'DEFAULT', '0 0/3 * * * ?', 'Europe/Paris');

Insert into QRTZ_LOCKS (LOCK_NAME) Values ('CALENDAR_ACCESS');
Insert into QRTZ_LOCKS (LOCK_NAME) Values ('JOB_ACCESS');
Insert into QRTZ_LOCKS (LOCK_NAME) Values ('MISFIRE_ACCESS');
Insert into QRTZ_LOCKS (LOCK_NAME) Values ('STATE_ACCESS');
Insert into QRTZ_LOCKS (LOCK_NAME) Values ('TRIGGER_ACCESS');

Insert into SCR_CCAA (ID, NAME, TMSTAMP) Values ('01', 'Andalucía', TO_DATE('01/12/2015 12:58:12', 'MM/DD/YYYY HH24:MI:SS'));
Insert into SCR_CCAA (ID, NAME, TMSTAMP) Values ('02', 'Aragón', TO_DATE('01/12/2015 12:58:13', 'MM/DD/YYYY HH24:MI:SS'));
Insert into SCR_CCAA (ID, NAME, TMSTAMP) Values ('03', 'Principado de Asturias', TO_DATE('01/12/2015 12:58:13', 'MM/DD/YYYY HH24:MI:SS'));
Insert into SCR_CCAA (ID, NAME, TMSTAMP) Values ('04', 'Illes Balears', TO_DATE('01/12/2015 12:58:13', 'MM/DD/YYYY HH24:MI:SS'));
Insert into SCR_CCAA (ID, NAME, TMSTAMP) Values ('05', 'Canarias', TO_DATE('01/12/2015 12:58:13', 'MM/DD/YYYY HH24:MI:SS'));
Insert into SCR_CCAA (ID, NAME, TMSTAMP) Values ('06', 'Cantabria', TO_DATE('01/12/2015 12:58:13', 'MM/DD/YYYY HH24:MI:SS'));
Insert into SCR_CCAA (ID, NAME, TMSTAMP) Values ('07', 'Castilla y León', TO_DATE('01/12/2015 12:58:13', 'MM/DD/YYYY HH24:MI:SS'));
Insert into SCR_CCAA (ID, NAME, TMSTAMP) Values ('08', 'Castilla-La Mancha', TO_DATE('01/12/2015 12:58:13', 'MM/DD/YYYY HH24:MI:SS'));
Insert into SCR_CCAA (ID, NAME, TMSTAMP) Values ('09', 'Cataluña', TO_DATE('01/12/2015 12:58:13', 'MM/DD/YYYY HH24:MI:SS'));
Insert into SCR_CCAA (ID, NAME, TMSTAMP) Values ('10', 'Comunitat Valenciana', TO_DATE('01/12/2015 12:58:13', 'MM/DD/YYYY HH24:MI:SS'));
Insert into SCR_CCAA (ID, NAME, TMSTAMP) Values ('11', 'Extremadura', TO_DATE('01/12/2015 12:58:13', 'MM/DD/YYYY HH24:MI:SS'));
Insert into SCR_CCAA (ID, NAME, TMSTAMP) Values ('12', 'Galicia', TO_DATE('01/12/2015 12:58:13', 'MM/DD/YYYY HH24:MI:SS'));
Insert into SCR_CCAA (ID, NAME, TMSTAMP) Values ('13', 'Comunidad de Madrid', TO_DATE('01/12/2015 12:58:13', 'MM/DD/YYYY HH24:MI:SS'));
Insert into SCR_CCAA (ID, NAME, TMSTAMP) Values ('14', 'Región de Murcia', TO_DATE('01/12/2015 12:58:13', 'MM/DD/YYYY HH24:MI:SS'));
Insert into SCR_CCAA (ID, NAME, TMSTAMP) Values ('15', 'Comunidad Foral de Navarra', TO_DATE('01/12/2015 12:58:13', 'MM/DD/YYYY HH24:MI:SS'));
Insert into SCR_CCAA (ID, NAME, TMSTAMP) Values ('16', 'País Vasco', TO_DATE('01/12/2015 12:58:13', 'MM/DD/YYYY HH24:MI:SS'));
Insert into SCR_CCAA (ID, NAME, TMSTAMP) Values ('17', 'La Rioja', TO_DATE('01/12/2015 12:58:13', 'MM/DD/YYYY HH24:MI:SS'));
Insert into SCR_CCAA (ID, NAME, TMSTAMP) Values ('18', 'Ciudad Autónoma de Ceuta', TO_DATE('01/12/2015 12:58:13', 'MM/DD/YYYY HH24:MI:SS'));
Insert into SCR_CCAA (ID, NAME, TMSTAMP) Values ('19', 'Ciudad Autónoma de Melilla', TO_DATE('01/12/2015 12:58:13', 'MM/DD/YYYY HH24:MI:SS'));


-- Libro de entrada
update idocarchdet set 
detval = E'"01.00"|31|1,"Número de registro",1,20,1,"Fld1",0,0,""|2,"Fecha de registro",9,0,1,"Fld2",0,0,""|3,"Usuario",1,32,1,"Fld3",0,0,""|4,"Fecha de trabajo",7,0,1,"Fld4",0,0,""|5,"Oficina de registro",4,0,1,"Fld5",0,0,""|6,"Estado",4,0,1,"Fld6",0,0,""|7,"Origen",4,0,1,"Fld7",0,0,""|8,"Destino",4,0,1,"Fld8",0,0,""|9,"Remitentes",1,80,1,"Fld9",0,0,""|10,"Nº. registro original",1,20,1,"Fld10",0,0,""|11,"Tipo de registro original",4,0,1,"Fld11",0,0,""|12,"Fecha de registro original",7,0,1,"Fld12",0,0,""|13,"Registro original",4,0,1,"Fld13",0,0,""|14,"Tipo de transporte",1,31,1,"Fld14",0,0,""|15,"Número de transporte",1,30,1,"Fld15",0,0,""|16,"Tipo de asunto",4,0,1,"Fld16",0,0,""|17,"Resumen",1,240,1,"Fld17",0,0,""|18,"Comentario",2,65535,1,"Fld18",0,0,""|19,"Referencia de Expediente",1,50,1,"Fld19",0,0,""|20,"Fecha del documento",7,0,1,"Fld20",0,0,""|1002,"Campo que no sé qué hace",4,0,1,"Fld1002",0,0,""|1003,"Obligatorio el registro original",4,0,1,"Fld1003",0,0,""|1004,"Último acuse generado",2,65535,1,"Fld1004",0,0,""|500,"LimiteInferiorReserva",2,65535,1,"Fld500",0,0,""|1000,"LimiteSuperiorReserva",2,65535,1,"Fld1000",0,0,""|501,"Expone",2,65535,1,"Fld501",0,0,""|502,"Solicita",2,65535,1,"Fld502",0,0,""|503,"Involucrado en Interc. Registral",4,0,1,"Fld503",0,0,""|504,"Acompaña doc. física requerida",4,0,1,"Fld504",0,0,""|505,"Acompaña doc. física complementaria",4,0,1,"Fld505",0,0,""|506,"No acompaña doc. física ni otros soportes",4,0,1,"Fld506",0,0,""|8|1,"EREG1",1,1,1|2,"EREG2",0,1,2|3,"EREG3",0,1,4|4,"EREG4",0,1,6|5,"EREG5",0,1,7|6,"EREG6",0,1,8|7,"EREG7",0,1,9|8,"EREG8",0,1,5'
where dettype=1 and archid in (select id from idocarchhdr where type=1);


--- Libros de Salida
update idocarchdet set 
detval = E'"01.00"|26|1,"Número de registro",1,20,1,"Fld1",0,0,""|2,"Fecha de registro",9,0,1,"Fld2",0,0,""|3,"Usuario",1,32,1,"Fld3",0,0,""|4,"Fecha de trabajo",7,0,1,"Fld4",0,0,""|5,"Oficina de registro",4,0,1,"Fld5",0,0,""|6,"Estado",4,0,1,"Fld6",0,0,""|7,"Origen",4,0,1,"Fld7",0,0,""|8,"Destino",4,0,1,"Fld8",0,0,""|9,"Destinatarios",1,80,1,"Fld9",0,0,""|10,"Tipo de transporte",1,31,1,"Fld10",0,0,""|11,"Número de transporte",1,30,1,"Fld11",0,0,""|12,"Tipo de asunto",4,0,1,"Fld12",0,0,""|13,"Resumen",1,240,1,"Fld13",0,0,""|14,"Comentario",2,65535,1,"Fld14",0,0,""|15,"Fecha del documento",7,0,1,"Fld15",0,0,""|1002,"Campo que no sé qué hace",4,0,1,"Fld1002",0,0,""|1003,"Obligatorio el registro original",4,0,1,"Fld1003",0,0,""|1004,"Último acuse generado",2,65535,1,"Fld1004",0,0,""|500,"LimiteInferiorReserva",2,65535,1,"Fld500",0,0,""|1000,"LimiteSuperiorReserva",2,65535,1,"Fld1000",0,0,""|501,"Expone",2,65535,1,"Fld501",0,0,""|502,"Solicita",2,65535,1,"Fld502",0,0,""|503,"Involucrado en Interc. Registral",4,0,1,"Fld503",0,0,""|504,"Acompaña doc. física requerida",4,0,1,"Fld504",0,0,""|505,"Acompaña doc. física complementaria",4,0,1,"Fld505",0,0,""|506,"No acompaña doc. física ni otros soportes",4,0,1,"Fld506",0,0,""|8|1,"EREG1",1,1,1|2,"EREG2",0,1,2|3,"EREG3",0,1,4|4,"EREG4",0,1,6|5,"EREG5",0,1,7|6,"EREG6",0,1,8|7,"EREG7",0,1,9|8,"EREG8",0,1,5'
where dettype=1 and archid in (select id from idocarchhdr where type=2);