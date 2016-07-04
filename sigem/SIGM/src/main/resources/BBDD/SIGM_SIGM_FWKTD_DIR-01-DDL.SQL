CREATE TABLE CAT_AMBITO_TERRITORIAL
(
  C_ID_AMBITO                VARCHAR2(2 CHAR)       NULL,
  C_DENOMINACION_AMBITO      VARCHAR2(30 CHAR)      NULL,
  C_ID_ESTADO                VARCHAR2(2 CHAR)       NULL,
  D_FECHA_MODIF              DATE                   NULL,
  C_ID_NIVEL_ADMIN           VARCHAR2(2 CHAR)       NULL,
  C_ID_AMBITO_GEOGRAFICO     VARCHAR2(2 CHAR)       NULL,
  C_TIPO_ENTIDAD_GEOGRAFICA  VARCHAR2(100 CHAR)     NULL,
  D_INICIO_VIGENCIA          DATE                   NULL,
  D_FIN_VIGENCIA             DATE                   NULL,
  D_FECHA_ALTA               DATE                   NULL,
  N_ID_AMBITO_DIR2           NUMBER(2)              NULL
);


CREATE TABLE CAT_COMUNIDAD_AUTONOMA
(
  C_ID_COMUNIDAD     VARCHAR2(2 CHAR)               NULL,
  C_DESC_COMUNIDAD   VARCHAR2(50 CHAR)              NULL,
  C_ID_ESTADO        VARCHAR2(2 CHAR)               NULL,
  C_ID_PAIS          VARCHAR2(3 CHAR)               NULL,
  D_INICIO_VIGENCIA  DATE                           NULL,
  D_FIN_VIGENCIA     DATE                           NULL,
  C_COMUNIDAD_RCP    VARCHAR2(2 CHAR)               NULL,
  C_CODIGO_DIR2      VARCHAR2(2 CHAR)               NULL,
  D_FECHA_MODIF      DATE                           NULL,
  D_FECHA_ALTA       DATE                           NULL
);


CREATE TABLE CAT_ENTIDAD_GEOGRAFICA
(
  C_ID_ENTIDAD_GEOGRAFICA  VARCHAR2(2 CHAR)         NULL,
  C_DESC_ENT_GEOGRAFICA    VARCHAR2(50 CHAR)        NULL,
  C_ID_ESTADO              VARCHAR2(2 CHAR)         NULL,
  D_FECHA_MODIF            DATE                     NULL,
  D_INICIO_VIGENCIA        DATE                     NULL,
  D_FIN_VIGENCIA           DATE                     NULL,
  D_FECHA_ALTA             DATE                     NULL,
  N_ID_AMBITO_DIR2         NUMBER(2)                NULL
);


CREATE TABLE CAT_ESTADO_ENTIDAD
(
  C_ID_ESTADO_ENTIDAD  VARCHAR2(2 CHAR)             NULL,
  C_DESC_ESTADO        VARCHAR2(50 CHAR)            NULL,
  C_ID_ESTADO          VARCHAR2(2 CHAR)             NULL,
  D_FECHA_MODIF        DATE                         NULL,
  D_INICIO_VIGENCIA    DATE                         NULL,
  D_FIN_VIGENCIA       DATE                         NULL,
  D_FECHA_ALTA         DATE                         NULL
);


CREATE TABLE CAT_ESTADO_VALIDACION
(
  N_ID_ESTADO_VAL    NUMBER(3)                      NULL,
  C_DESC_ESTADO_VAL  VARCHAR2(50 CHAR)              NULL,
  C_ID_ESTADO        VARCHAR2(2 CHAR)               NULL,
  D_FECHA_MODIF      DATE                           NULL,
  D_INICIO_VIGENCIA  DATE                           NULL,
  D_FIN_VIGENCIA     DATE                           NULL,
  D_FECHA_ALTA       DATE                           NULL
);


CREATE TABLE CAT_IDIOMA
(
  N_ID_IDIOMA        NUMBER(2)                      NULL,
  C_DESCRIPCION      VARCHAR2(50 CHAR)              NULL,
  C_ID_ESTADO        VARCHAR2(2 CHAR)               NULL,
  C_ALFA_IDIOMA      VARCHAR2(3 CHAR)               NULL,
  D_FECHA_ALTA       DATE                           NULL,
  D_FECHA_MODIF      DATE                           NULL,
  D_INICIO_VIGENCIA  DATE                           NULL,
  D_FIN_VIGENCIA     DATE                           NULL
);


CREATE TABLE CAT_ISLA
(
  C_ID_ISLA          VARCHAR2(2 CHAR)               NULL,
  C_DESC_ISLA        VARCHAR2(50 CHAR)              NULL,
  D_INICIO_VIGENCIA  DATE                           NULL,
  D_FIN_VIGENCIA     DATE                           NULL,
  C_ID_ESTADO        VARCHAR2(2 CHAR)               NULL,
  D_FECHA_MODIF      DATE                           NULL,
  D_FECHA_ALTA       DATE                           NULL,
  C_ID_PROVINCIA     VARCHAR2(2 CHAR)               NULL
);


CREATE TABLE CAT_JERARQUIA_OFICINA
(
  C_ID_JERARQUIA     VARCHAR2(2 CHAR)               NULL,
  C_DESC_JERARQUIA   VARCHAR2(300 CHAR)             NULL,
  C_ID_ESTADO        VARCHAR2(2 CHAR)               NULL,
  D_FECHA_MODIF      DATE                           NULL,
  D_INICIO_VIGENCIA  DATE                           NULL,
  D_FIN_VIGENCIA     DATE                           NULL,
  D_FECHA_ALTA       DATE                           NULL
);


CREATE TABLE CAT_LOCALIDAD
(
  C_ID_LOCALIDAD           VARCHAR2(4 CHAR)         NULL,
  C_ID_PROVINCIA           VARCHAR2(2 CHAR)         NULL,
  C_DESC_LOCALIDAD         VARCHAR2(50 CHAR)        NULL,
  C_ID_ESTADO              VARCHAR2(2 CHAR)         NULL,
  D_INICIO_VIGENCIA        DATE                     NULL,
  D_FIN_VIGENCIA           DATE                     NULL,
  C_ID_ISLA                VARCHAR2(2 CHAR)         NULL,
  D_FECHA_MODIF            DATE                     NULL,
  D_FECHA_ALTA             DATE                     NULL,
  C_ID_ENTIDAD_GEOGRAFICA  VARCHAR2(2 CHAR)         NULL,
  C_LOCALIDAD_RCP          VARCHAR2(4 CHAR)         NULL,
  C_ID_EXTERNO_DIR2        VARCHAR2(12 CHAR)        NULL
);


CREATE TABLE CAT_MOTIVO_EXTINCION
(
  N_ID_MOTIVO        VARCHAR2(3 CHAR)               NULL,
  C_DENOM_MOTIVO     VARCHAR2(400 CHAR)             NULL,
  D_INICIO_VIGENCIA  DATE                           NULL,
  D_FIN_VIGENCIA     DATE                           NULL,
  D_FECHA_ALTA       DATE                           NULL,
  D_FECHA_MODIF      DATE                           NULL,
  C_ID_ESTADO        VARCHAR2(2 CHAR)               NULL,
  N_ID_ORIGEN_DATOS  NUMBER(6)                      NULL
);


CREATE TABLE CAT_NIVEL_ADMINISTRACION
(
  C_ID_NIVEL_ADMIN   VARCHAR2(2 CHAR)               NULL,
  C_DESC_NIVEL       VARCHAR2(300 CHAR)             NULL,
  C_ID_ESTADO        VARCHAR2(2 CHAR)               NULL,
  D_FECHA_MODIF      DATE                           NULL,
  D_INICIO_VIGENCIA  DATE                           NULL,
  D_FIN_VIGENCIA     DATE                           NULL,
  D_FECHA_ALTA       DATE                           NULL,
  S_RAIZ             VARCHAR2(9 CHAR)               NULL
);


CREATE TABLE CAT_ORIGEN_DATOS
(
  N_ID_ORIGEN_DATOS  NUMBER(6)                      NULL,
  C_NOMBRE_ORIGEN    VARCHAR2(30 CHAR)              NULL,
  C_ID_ESTADO        VARCHAR2(2 CHAR)               NULL,
  D_FECHA_MODIF      DATE                           NULL,
  D_INICIO_VIGENCIA  DATE                           NULL,
  D_FIN_VIGENCIA     DATE                           NULL,
  D_FECHA_ALTA       DATE                           NULL
);


CREATE TABLE CAT_PAIS
(
  C_ID_PAIS          VARCHAR2(3 CHAR)               NULL,
  C_DESC_PAIS        VARCHAR2(100 CHAR)             NULL,
  C_ID_ESTADO        VARCHAR2(2 CHAR)               NULL,
  C_ALFA3_PAIS       VARCHAR2(3 CHAR)               NULL,
  D_INICIO_VIGENCIA  DATE                           NULL,
  D_FIN_VIGENCIA     DATE                           NULL,
  D_FECHA_MODIF      DATE                           NULL,
  D_FECHA_ALTA       DATE                           NULL,
  C_ALFA2_PAIS       VARCHAR2(2 CHAR)               NULL
);


CREATE TABLE CAT_PODER
(
  N_ID_PODER         NUMBER(6)                      NULL,
  C_DESCRIPCION      VARCHAR2(300 CHAR)             NULL,
  C_ID_ESTADO        VARCHAR2(2 CHAR)               NULL,
  D_FECHA_MODIF      DATE                           NULL,
  D_INICIO_VIGENCIA  DATE                           NULL,
  D_FIN_VIGENCIA     DATE                           NULL,
  D_FECHA_ALTA       DATE                           NULL
);


CREATE TABLE CAT_PROVINCIA
(
  C_ID_PROVINCIA     VARCHAR2(2 CHAR)               NULL,
  C_ID_COMUNIDAD     VARCHAR2(2 CHAR)               NULL,
  C_DESC_PROV        VARCHAR2(50 CHAR)              NULL,
  C_ID_ESTADO        VARCHAR2(2 CHAR)               NULL,
  D_INICIO_VIGENCIA  DATE                           NULL,
  D_FIN_VIGENCIA     DATE                           NULL,
  D_FECHA_MODIF      DATE                           NULL,
  D_FECHA_ALTA       DATE                           NULL
);


CREATE TABLE CAT_SERVICIOS
(
  ID             INTEGER                            NULL,
  DESCRIPCION    VARCHAR2(300 CHAR)                 NULL,
  RESTRINGIDO    CHAR(1 CHAR)                       NULL,
  VISIBILITY     VARCHAR2(3 CHAR)                   NULL,
  D_FECHA_MODIF  DATE                               NULL,
  D_FECHA_ALTA   DATE                               NULL
);


CREATE TABLE CAT_TIPO_CONTACTO
(
  N_ID_TIPO_CONTACTO    VARCHAR2(2 CHAR)            NULL,
  C_DESC_TIPO_CONTACTO  VARCHAR2(30 CHAR)           NULL,
  D_INICIO_VIGENCIA     DATE                        NULL,
  D_FIN_VIGENCIA        DATE                        NULL,
  D_FECHA_ALTA          DATE                        NULL,
  D_FECHA_MODIF         DATE                        NULL,
  C_ID_ESTADO           VARCHAR2(2 CHAR)            NULL
);


CREATE TABLE CAT_TIPO_ENTIDAD_PUBLICA
(
  C_ID_TIPO_UNIDAD        VARCHAR2(2 CHAR)          NULL,
  C_DESC_TIPO_UNIDAD      VARCHAR2(100 CHAR)        NULL,
  C_ID_ESTADO             VARCHAR2(2 CHAR)          NULL,
  D_FECHA_MODIF           DATE                      NULL,
  D_INICIO_VIGENCIA       DATE                      NULL,
  D_FIN_VIGENCIA          DATE                      NULL,
  D_FECHA_ALTA            DATE                      NULL,
  B_SW_EDP                CHAR(1 CHAR)              NULL,
  N_ID_TIPO_ENTIDAD_DIR2  NUMBER(6)                 NULL
);


CREATE TABLE CAT_TIPO_UNIDAD_ORGANICA
(
  C_ID_TIPO_UNIDAD   VARCHAR2(3 CHAR)               NULL,
  C_DESC_TIPO        VARCHAR2(300 CHAR)             NULL,
  C_ID_ESTADO        VARCHAR2(2 CHAR)               NULL,
  D_FECHA_MODIF      DATE                           NULL,
  D_INICIO_VIGENCIA  DATE                           NULL,
  D_FIN_VIGENCIA     DATE                           NULL,
  D_FECHA_ALTA       DATE                           NULL
);


CREATE TABLE CAT_TIPO_VIA
(
  C_ID_TIPO_VIA      VARCHAR2(2 CHAR)               NULL,
  C_DESC_TIPO_VIA    VARCHAR2(50 CHAR)              NULL,
  C_CAMPOS_TIPO_VIA  VARCHAR2(50 CHAR)              NULL,
  C_ID_ESTADO        VARCHAR2(2 CHAR)               NULL,
  D_FECHA_MODIF      DATE                           NULL,
  D_INICIO_VIGENCIA  DATE                           NULL,
  D_FIN_VIGENCIA     DATE                           NULL,
  D_FECHA_ALTA       DATE                           NULL
);


CREATE TABLE DIR_CONTACTO_OFICINA
(
  N_ID_CONTACTO       NUMBER(6)                     NULL,
  N_ID_TIPO_CONTACTO  VARCHAR2(2 CHAR)              NULL,
  C_ID_OFICINA        VARCHAR2(9 CHAR)              NULL,
  C_DATOS_CONTACTO    VARCHAR2(100 CHAR)            NULL,
  C_OBSERV_CONTACTO   VARCHAR2(400 CHAR)            NULL,
  D_INICIO_VIGENCIA   DATE                          NULL,
  D_FIN_VIGENCIA      DATE                          NULL,
  D_FECHA_ALTA        DATE                          NULL,
  D_FECHA_MODIF       DATE                          NULL,
  C_ID_ESTADO         VARCHAR2(2 CHAR)              NULL
);


CREATE TABLE DIR_CONTACTO_UD_ORGANICA
(
  N_ID_CONTACTO       NUMBER(6)                     NULL,
  N_ID_TIPO_CONTACTO  VARCHAR2(2 CHAR)              NULL,
  C_ID_UD_ORGANICA    VARCHAR2(9 CHAR)              NULL,
  C_DATOS_CONTACTO    VARCHAR2(100 CHAR)            NULL,
  C_OBSERV_CONTACTO   VARCHAR2(400 CHAR)            NULL,
  D_INICIO_VIGENCIA   DATE                          NULL,
  D_FIN_VIGENCIA      DATE                          NULL,
  D_FECHA_ALTA        DATE                          NULL,
  D_FECHA_MODIF       DATE                          NULL,
  C_ID_ESTADO         VARCHAR2(2 CHAR)              NULL
);


CREATE TABLE DIR_DIRECCION
(
  N_ID_DIRECCION           NUMBER(6)                NULL,
  C_ID_TIPO_VIA            VARCHAR2(2 CHAR)         NULL,
  C_NOMBRE_VIA             VARCHAR2(300 CHAR)       NULL,
  C_NUM_VIA                VARCHAR2(20 CHAR)        NULL,
  C_COMPLEMENTO_DIRECCION  VARCHAR2(300 CHAR)       NULL,
  C_CODIGO_POSTAL          VARCHAR2(14 CHAR)        NULL,
  C_ID_PAIS                VARCHAR2(3 CHAR)         NULL,
  C_ID_COMUNIDAD           VARCHAR2(2 CHAR)         NULL,
  C_ID_ENTIDAD_GEOGRAFICA  VARCHAR2(2 CHAR)         NULL,
  C_ID_PROVINCIA           VARCHAR2(2 CHAR)         NULL,
  C_ID_LOCALIDAD           VARCHAR2(4 CHAR)         NULL,
  C_LOC_EXTRANJERA         VARCHAR2(40 CHAR)        NULL,
  C_DIR_EXTRANJERA         VARCHAR2(200 CHAR)       NULL,
  D_FECHA_ALTA             DATE                     NULL,
  D_FECHA_MODIF            DATE                     NULL,
  C_OBSERVACION_LOCAL      VARCHAR2(400 CHAR)       NULL
);


CREATE TABLE DIR_ESTADO_ACTUALIZACION
(
  ID                   INTEGER                      NULL,
  FECHA_ACTUALIZACION  DATE                         NULL,
  ESTADO               VARCHAR2(10 CHAR)            NULL
);


CREATE TABLE DIR_OFICINA
(
  C_ID_OFICINA               VARCHAR2(9 CHAR)       NULL,
  C_DNM_OFICINA              VARCHAR2(300 CHAR)     NULL,
  B_OFICINA_REGISTRO         CHAR(1 BYTE)           NULL,
  B_OFICINA_INFORMACION      CHAR(1 BYTE)           NULL,
  B_OFICINA_TRAMITACION      CHAR(1 BYTE)           NULL,
  B_SIR                      CHAR(1 BYTE)           NULL,
  B_REG_ELECTRONICO          CHAR(1 BYTE)           NULL,
  B_INT_LOC_EST              CHAR(1 BYTE)           NULL,
  B_INT_LOC_AUT_RES          CHAR(1 BYTE)           NULL,
  B_INT_LOC_AUT_GEN          CHAR(1 BYTE)           NULL,
  B_INT_LOC_LOC_RES          CHAR(1 BYTE)           NULL,
  B_INT_LOC_LOC_GEN          CHAR(1 BYTE)           NULL,
  B_INT_AY_AY_RES            CHAR(1 BYTE)           NULL,
  B_INT_SIN_RESTRICCION      CHAR(1 BYTE)           NULL,
  B_GENERICO                 CHAR(1 BYTE)           NULL,
  C_DESC_IND_GENERICO        VARCHAR2(50 CHAR)      NULL,
  C_ID_TIPO_OFICINA          VARCHAR2(2 CHAR)       NULL,
  C_ID_DEP_OFICINA           VARCHAR2(9 CHAR)       NULL,
  C_ID_UNIDAD_RESPONSABLE    VARCHAR2(9 CHAR)       NULL,
  C_HORARIO_ATENCION         VARCHAR2(450 BYTE)     NULL,
  C_DIAS_INHABILES           VARCHAR2(450 BYTE)     NULL,
  B_MISMA_DIREC              CHAR(1 BYTE)           NULL,
  N_ID_LOC_DIRECCION         NUMBER(6)              NULL,
  C_ID_ESTADO                VARCHAR2(2 CHAR)       NULL,
  D_VIG_CREACION             DATE                   NULL,
  D_VIG_EXTINCION            DATE                   NULL,
  D_VIG_ANULACION            DATE                   NULL,
  C_ID_EXTERNO_FUENTE        VARCHAR2(40 CHAR)      NULL,
  C_ID_NIVEL_ADMINISTRACION  VARCHAR2(2 CHAR)       NULL,
  D_ALTA_SISTEMA             DATE                   NULL,
  D_ULT_ACTUALIZ             DATE                   NULL,
  C_DNM_OFICINA_LANG         VARCHAR2(300 CHAR)     NULL,
  N_ID_IDIOMA                NUMBER(2)              NULL,
  C_OBSERVACION_GENERAL      VARCHAR2(450 BYTE)     NULL,
  C_OBSERVACION_CONTACTO     VARCHAR2(450 BYTE)     NULL,
  C_OBSERVACION_BAJA         VARCHAR2(450 BYTE)     NULL
);


CREATE TABLE DIR_SERVICIOS_OFICINA
(
  C_ID_OFICINA         VARCHAR2(9 CHAR)             NULL,
  C_ID_SERVICIO        INTEGER                      NULL,
  D_FECHA_ALTA         DATE                         NULL,
  D_ULT_ACTUALIZACION  DATE                         NULL
);


CREATE TABLE DIR_UNIDAD_ORGANICA
(
  C_ID_UD_ORGANICA         VARCHAR2(9 CHAR)         NULL,
  C_DNM_UD_ORGANICA        VARCHAR2(300 CHAR)       NULL,
  C_DNM_SIGLAS_UD          VARCHAR2(10 CHAR)        NULL,
  C_ID_NIVEL_ADMON         VARCHAR2(2 CHAR)         NULL,
  C_ID_TIPO_ENT_PUBLICA    VARCHAR2(2 CHAR)         NULL,
  C_ID_TIPO_UD_ORGANICA    VARCHAR2(3 CHAR)         NULL,
  N_NIVEL_JERARQUICO       NUMBER(6)                NULL,
  C_ID_DEP_UD_SUPERIOR     VARCHAR2(9 CHAR)         NULL,
  C_ID_DEP_UD_PRINCIPAL    VARCHAR2(9 CHAR)         NULL,
  B_SW_DEP_EDP_PRINCIPAL   CHAR(1 CHAR)             NULL,
  C_ID_DEP_EDP_PRINCIPAL   VARCHAR2(9 CHAR)         NULL,
  C_ID_AMB_TERR_COMPET     VARCHAR2(2 CHAR)         NULL,
  C_ID_AMB_ENT_GEOGRAFICA  VARCHAR2(2 CHAR)         NULL,
  C_ID_AMB_PAIS            VARCHAR2(3 CHAR)         NULL,
  C_ID_AMB_C_AUTONOMA      VARCHAR2(2 CHAR)         NULL,
  C_ID_AMB_PROVINCIA       VARCHAR2(2 CHAR)         NULL,
  C_ID_AMB_ISLA            VARCHAR2(2 CHAR)         NULL,
  C_ID_AMB_MUNICIPIO       VARCHAR2(4 CHAR)         NULL,
  C_ID_AMB_ELM             VARCHAR2(4 CHAR)         NULL,
  C_ID_AMB_LOC_EXTRANJERA  VARCHAR2(50 CHAR)        NULL,
  C_COMPET_UD              VARCHAR2(300 CHAR)       NULL,
  C_COMPET_DISP_LEGAL      VARCHAR2(300 CHAR)       NULL,
  B_SW_LOC_UD_SUPERIOR     CHAR(1 CHAR)             NULL,
  N_ID_LOC_DIRECCION       NUMBER(6)                NULL,
  C_ID_ESTADO              VARCHAR2(2 CHAR)         NULL,
  D_VIG_ALTA_OFICIAL       DATE                     NULL,
  D_VIG_BAJA_OFICIAL       DATE                     NULL,
  D_VIG_EXTINCION          DATE                     NULL,
  D_VIG_ANULACION          DATE                     NULL,
  C_ID_EXTERNO_FUENTE      VARCHAR2(40 CHAR)        NULL,
  N_ID_PODER               NUMBER(6)                NULL,
  C_ID_EXTERNO_DIR2        VARCHAR2(12 CHAR)        NULL,
  D_ALTA_SISTEMA           DATE                     NULL,
  D_ULT_ACTUALIZ           DATE                     NULL,
  S_RAIZ                   VARCHAR2(9 CHAR)         NULL,
  NIF_CIF                  VARCHAR2(9 CHAR)         NULL,
  C_DNM_UD_ORGANICA_LANG   VARCHAR2(300 CHAR)       NULL,
  N_ID_IDIOMA              NUMBER(2)                NULL,
  C_OBSERVACION_GENERAL    VARCHAR2(450 BYTE)       NULL,
  C_OBSERVACION_CONTACTO   VARCHAR2(450 BYTE)       NULL,
  C_OBSERVACION_BAJA       VARCHAR2(450 BYTE)       NULL,
  N_ID_OBSERVACION_BAJA    NUMBER(3)                NULL,
  C_ID_TYPEUNO             NUMBER(1)                NULL,
  C_ID_UORESPONSABLE       VARCHAR2(9 BYTE)         NULL,
  C_OBSERVACION            VARCHAR2(450 BYTE)       NULL,
  N_TYPE                   NUMBER(1)                NULL
);


CREATE TABLE QRTZ_BLOB_TRIGGERS
(
  TRIGGER_NAME   VARCHAR2(200 BYTE)                 NULL,
  TRIGGER_GROUP  VARCHAR2(200 BYTE)                 NULL,
  BLOB_DATA      BLOB                               NULL
);


CREATE TABLE QRTZ_CALENDARS
(
  CALENDAR_NAME  VARCHAR2(200 BYTE)                 NULL,
  CALENDAR       BLOB                               NULL
);


CREATE TABLE QRTZ_CRON_TRIGGERS
(
  TRIGGER_NAME     VARCHAR2(200 BYTE)               NULL,
  TRIGGER_GROUP    VARCHAR2(200 BYTE)               NULL,
  CRON_EXPRESSION  VARCHAR2(120 BYTE)               NULL,
  TIME_ZONE_ID     VARCHAR2(80 BYTE)                NULL
);


CREATE TABLE QRTZ_FIRED_TRIGGERS
(
  ENTRY_ID           VARCHAR2(95 BYTE)              NULL,
  TRIGGER_NAME       VARCHAR2(200 BYTE)             NULL,
  TRIGGER_GROUP      VARCHAR2(200 BYTE)             NULL,
  IS_VOLATILE        VARCHAR2(1 BYTE)               NULL,
  INSTANCE_NAME      VARCHAR2(200 BYTE)             NULL,
  FIRED_TIME         NUMBER(13)                     NULL,
  PRIORITY           NUMBER(13)                     NULL,
  STATE              VARCHAR2(16 BYTE)              NULL,
  JOB_NAME           VARCHAR2(200 BYTE)             NULL,
  JOB_GROUP          VARCHAR2(200 BYTE)             NULL,
  IS_STATEFUL        VARCHAR2(1 BYTE)               NULL,
  REQUESTS_RECOVERY  VARCHAR2(1 BYTE)               NULL
);


CREATE TABLE QRTZ_JOB_DETAILS
(
  JOB_NAME           VARCHAR2(200 BYTE)             NULL,
  JOB_GROUP          VARCHAR2(200 BYTE)             NULL,
  DESCRIPTION        VARCHAR2(250 BYTE)             NULL,
  JOB_CLASS_NAME     VARCHAR2(250 BYTE)             NULL,
  IS_DURABLE         VARCHAR2(1 BYTE)               NULL,
  IS_VOLATILE        VARCHAR2(1 BYTE)               NULL,
  IS_STATEFUL        VARCHAR2(1 BYTE)               NULL,
  REQUESTS_RECOVERY  VARCHAR2(1 BYTE)               NULL,
  JOB_DATA           BLOB                           NULL
);


CREATE TABLE QRTZ_JOB_LISTENERS
(
  JOB_NAME      VARCHAR2(200 BYTE)                  NULL,
  JOB_GROUP     VARCHAR2(200 BYTE)                  NULL,
  JOB_LISTENER  VARCHAR2(200 BYTE)                  NULL
);


CREATE TABLE QRTZ_LOCKS
(
  LOCK_NAME  VARCHAR2(40 BYTE)                      NULL
);


CREATE TABLE QRTZ_PAUSED_TRIGGER_GRPS
(
  TRIGGER_GROUP  VARCHAR2(200 BYTE)                 NULL
);


CREATE TABLE QRTZ_SCHEDULER_STATE
(
  INSTANCE_NAME      VARCHAR2(200 BYTE)             NULL,
  LAST_CHECKIN_TIME  NUMBER(13)                     NULL,
  CHECKIN_INTERVAL   NUMBER(13)                     NULL
);


CREATE TABLE QRTZ_SIMPLE_TRIGGERS
(
  TRIGGER_NAME     VARCHAR2(200 BYTE)               NULL,
  TRIGGER_GROUP    VARCHAR2(200 BYTE)               NULL,
  REPEAT_COUNT     NUMBER(7)                        NULL,
  REPEAT_INTERVAL  NUMBER(12)                       NULL,
  TIMES_TRIGGERED  NUMBER(10)                       NULL
);


CREATE TABLE QRTZ_TRIGGERS
(
  TRIGGER_NAME    VARCHAR2(200 BYTE)                NULL,
  TRIGGER_GROUP   VARCHAR2(200 BYTE)                NULL,
  JOB_NAME        VARCHAR2(200 BYTE)                NULL,
  JOB_GROUP       VARCHAR2(200 BYTE)                NULL,
  IS_VOLATILE     VARCHAR2(1 BYTE)                  NULL,
  DESCRIPTION     VARCHAR2(250 BYTE)                NULL,
  NEXT_FIRE_TIME  NUMBER(13)                        NULL,
  PREV_FIRE_TIME  NUMBER(13)                        NULL,
  PRIORITY        NUMBER(13)                        NULL,
  TRIGGER_STATE   VARCHAR2(16 BYTE)                 NULL,
  TRIGGER_TYPE    VARCHAR2(8 BYTE)                  NULL,
  START_TIME      NUMBER(13)                        NULL,
  END_TIME        NUMBER(13)                        NULL,
  CALENDAR_NAME   VARCHAR2(200 BYTE)                NULL,
  MISFIRE_INSTR   NUMBER(2)                         NULL,
  JOB_DATA        BLOB                              NULL
);


CREATE TABLE QRTZ_TRIGGER_LISTENERS
(
  TRIGGER_NAME      VARCHAR2(200 BYTE)              NULL,
  TRIGGER_GROUP     VARCHAR2(200 BYTE)              NULL,
  TRIGGER_LISTENER  VARCHAR2(200 BYTE)              NULL
);


CREATE TABLE REL_OFI_OFI
(
  C_ID_OFICINA_ULTIMA    VARCHAR2(9 CHAR)           NULL,
  C_ID_OFICINA_ANTERIOR  VARCHAR2(9 CHAR)           NULL,
  N_ID_MOTIVO            VARCHAR2(3 CHAR)           NULL,
  D_FECHA_ALTA           DATE                       NULL,
  D_FECHA_MODIF          DATE                       NULL,
  C_ID_ESTADO_ULTIMA     VARCHAR2(2 CHAR)           NULL,
  C_ID_ESTADO_ANTERIOR   VARCHAR2(2 CHAR)           NULL,
  D_FECHA_EXTINCION      DATE                       NULL,
  D_FECHA_BAJA           DATE                       NULL,
  C_OBSER_BAJA           VARCHAR2(400 CHAR)         NULL,
  D_FECHA_ANULACION      DATE                       NULL,
  C_ID_ESTADO            VARCHAR2(2 CHAR)           NULL
);


CREATE TABLE REL_ORGANIZATIVAS_UO_OFI
(
  C_ID_UNIDAD_ORGANICA  VARCHAR2(9 CHAR)            NULL,
  C_ID_OFICINA          VARCHAR2(9 CHAR)            NULL,
  D_FECHA_ALTA          DATE                        NULL,
  D_FECHA_MODIF         DATE                        NULL,
  C_ID_ESTADO           VARCHAR2(2 CHAR)            NULL
);


CREATE TABLE REL_UNIDAD_ORGANICA_OFICINA
(
  C_ID_UNIDAD_ORGANICA  VARCHAR2(9 CHAR)            NULL,
  C_ID_OFICINA          VARCHAR2(9 CHAR)            NULL,
  D_FECHA_ALTA          DATE                        NULL,
  D_FECHA_MODIF         DATE                        NULL,
  C_ID_ESTADO           VARCHAR2(2 CHAR)            NULL
);


CREATE TABLE REL_UO_UO
(
  C_ID_UD_ORGANICA_ULTIMA    VARCHAR2(9 CHAR)       NULL,
  C_ID_UD_ORGANICA_ANTERIOR  VARCHAR2(9 CHAR)       NULL,
  N_ID_MOTIVO                VARCHAR2(3 CHAR)       NULL,
  D_FECHA_ALTA               DATE                   NULL,
  D_FECHA_MODIF              DATE                   NULL,
  C_ID_ESTADO_ULTIMA         VARCHAR2(2 CHAR)       NULL,
  C_ID_ESTADO_ANTERIOR       VARCHAR2(2 CHAR)       NULL,
  D_FECHA_EXTINCION          DATE                   NULL,
  D_FECHA_BAJA               DATE                   NULL,
  C_OBSER_BAJA               VARCHAR2(400 CHAR)     NULL,
  D_FECHA_ANULACION          DATE                   NULL,
  C_ID_ESTADO                VARCHAR2(2 CHAR)       NULL
);
