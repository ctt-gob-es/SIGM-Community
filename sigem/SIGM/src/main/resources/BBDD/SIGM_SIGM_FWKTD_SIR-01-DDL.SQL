CREATE TABLE CONFIGURACION
(
  PARAMETRO  VARCHAR2(50 BYTE)                  NOT NULL,
  VALOR      VARCHAR2(70 BYTE)                      NULL
);

COMMENT ON TABLE CONFIGURACION IS 'Tabla que almacena el valor de los parametros de configuracion de CIR.';

COMMENT ON COLUMN CONFIGURACION.PARAMETRO IS 'Nombre del parametro a configurar.';

COMMENT ON COLUMN CONFIGURACION.VALOR IS 'Valor del parametro.';


CREATE TABLE CONFIRMACION_PENDIENTES
(
  CD_INTERCAMBIO  VARCHAR2(31 BYTE)             NOT NULL,
  WS_ERROR        VARCHAR2(160 BYTE)                NULL,
  FIRMA           VARCHAR2(4000 BYTE)               NULL,
  MENSAJE         VARCHAR2(4000 BYTE)               NULL,
  FECHA           DATE                              NULL,
  NUM_REINTENTOS  INTEGER                       DEFAULT 0                         NULL,
  CORRECTO        INTEGER                       DEFAULT 0                         NULL
);

COMMENT ON COLUMN CONFIRMACION_PENDIENTES.CD_INTERCAMBIO IS 'Identificador de intercambio del mensaje';

COMMENT ON COLUMN CONFIRMACION_PENDIENTES.WS_ERROR IS 'Endpoint del WS en el que se producira el reintento de reenvio de la confirmaci�n';

COMMENT ON COLUMN CONFIRMACION_PENDIENTES.FIRMA IS 'Firma del mensaje';

COMMENT ON COLUMN CONFIRMACION_PENDIENTES.MENSAJE IS 'Mensaje de confirmacion';

COMMENT ON COLUMN CONFIRMACION_PENDIENTES.FECHA IS 'Fecha en la que se produjo la ultima modificacion';

COMMENT ON COLUMN CONFIRMACION_PENDIENTES.NUM_REINTENTOS IS 'Numero de reintentos de reenvios de la confirmacion';

COMMENT ON COLUMN CONFIRMACION_PENDIENTES.CORRECTO IS 'Indica si el reenvio se produjo de forma correcta';


CREATE TABLE DOCUMENTOS
(
  ID                  VARCHAR2(64 BYTE)         NOT NULL,
  NOMBRE              VARCHAR2(1024 BYTE)       NOT NULL,
  TIPO_MIME           VARCHAR2(128 BYTE)            NULL,
  TAMANO              LONG                      DEFAULT 0                     NOT NULL,
  FECHA_CREACION      TIMESTAMP(6)              NOT NULL,
  FECHA_MODIFICACION  TIMESTAMP(6)                  NULL,
  CONTENIDO           BLOB                          NULL,
  METADATOS           CLOB                          NULL
)
LOB (CONTENIDO) STORE AS (
  TABLESPACE SIGMSIR_LOB
  ENABLE       STORAGE IN ROW
  CHUNK       8192
  RETENTION
  NOCACHE
  INDEX (TABLESPACE SIGMSIR_LOB))
LOB (METADATOS) STORE AS (
  TABLESPACE SIGMSIR_LOB
  ENABLE       STORAGE IN ROW
  CHUNK       8192
  RETENTION
  NOCACHE
  INDEX (TABLESPACE SIGMSIR_LOB));



CREATE TABLE PLANIFICACION_ENVIO
(
  ID_PLANIFICACION     INTEGER                  NOT NULL,
  DS_PLANIFICACION     VARCHAR2(70 BYTE)            NULL,
  HORA_INICIO          VARCHAR2(5 BYTE)         NOT NULL,
  HORA_FIN             VARCHAR2(5 BYTE)         NOT NULL,
  TAMANYO_FICHERO_MIN  INTEGER                  NOT NULL,
  TAMANYO_FICHERO_MAX  INTEGER                  NOT NULL
);

COMMENT ON TABLE PLANIFICACION_ENVIO IS 'Tabla que almacena las planificaciones de env�o de un nodo territorial';

COMMENT ON COLUMN PLANIFICACION_ENVIO.ID_PLANIFICACION IS 'Id de la tabla. Autonumerico';

COMMENT ON COLUMN PLANIFICACION_ENVIO.DS_PLANIFICACION IS 'Descripci�n de la planificaci�n';

COMMENT ON COLUMN PLANIFICACION_ENVIO.HORA_INICIO IS 'Hora inicio planificaci�n';

COMMENT ON COLUMN PLANIFICACION_ENVIO.HORA_FIN IS 'Hora fin planificaci�n';

COMMENT ON COLUMN PLANIFICACION_ENVIO.TAMANYO_FICHERO_MIN IS 'Tama�o del fichero de intercambio';


CREATE TABLE REINTENTOS_ENVIO
(
  ID_REINTENTO      INTEGER                         NULL,
  NOMBRE_FICHERO    VARCHAR2(80 BYTE)               NULL,
  NUM_REINTENTOS    INTEGER                         NULL,
  ULTIMO_REINTENTO  DATE                        DEFAULT sysdate                   NULL
);

COMMENT ON TABLE REINTENTOS_ENVIO IS 'Tabla que almacena el numero de reintentos que se han llevado a cabo de un fichero de intercambio.';

COMMENT ON COLUMN REINTENTOS_ENVIO.ID_REINTENTO IS 'Identificador unico de la tabla.';

COMMENT ON COLUMN REINTENTOS_ENVIO.NOMBRE_FICHERO IS 'Nombre del fichero de intercambio que se esta intentando enviar.';

COMMENT ON COLUMN REINTENTOS_ENVIO.NUM_REINTENTOS IS 'Numero de reintentos del envio.';

COMMENT ON COLUMN REINTENTOS_ENVIO.ULTIMO_REINTENTO IS 'Fecha y hora en la que se llevo a cabo el ultimo reintento.';


CREATE TABLE RELACION_ID_ORGANISMOS
(
  CD_ORGANISMO_ORIGINAL    VARCHAR2(16 BYTE)    NOT NULL,
  CD_ORGANISMO_DIRECTORIO  VARCHAR2(16 BYTE)    NOT NULL,
  CD_APLICACION            VARCHAR2(30 BYTE)        NULL,
  CD_OFICINA_REGISTRO      VARCHAR2(16 BYTE)    NOT NULL,
  FE_INACTIVO              DATE                     NULL
);

COMMENT ON TABLE RELACION_ID_ORGANISMOS IS 'Tabla que almacena la relacion entre el codigo del organismo en directorio y el codigo que emplea la oficina de registro en local.';

COMMENT ON COLUMN RELACION_ID_ORGANISMOS.CD_ORGANISMO_ORIGINAL IS 'Codigo del organismo en la base de datos local de la oficina de registro.';

COMMENT ON COLUMN RELACION_ID_ORGANISMOS.CD_ORGANISMO_DIRECTORIO IS 'Codigo del organismo en el directorio centralizado.';

COMMENT ON COLUMN RELACION_ID_ORGANISMOS.CD_APLICACION IS 'Codificacion que emplea la oficina de registro.';

COMMENT ON COLUMN RELACION_ID_ORGANISMOS.CD_OFICINA_REGISTRO IS 'Codigo de la oficina de registro.';

COMMENT ON COLUMN RELACION_ID_ORGANISMOS.FE_INACTIVO IS 'Fecha a partir de la cual la oficina esta inactiva.';


CREATE TABLE SIR_ASIENTOS_REGISTRALES
(
  ID                    NUMBER                  NOT NULL,
  CD_ENT_REG            VARCHAR2(21 CHAR)       NOT NULL,
  CD_INTERCAMBIO        VARCHAR2(33 CHAR)           NULL,
  CD_ESTADO             INTEGER                 NOT NULL,
  FE_ESTADO             TIMESTAMP(6)            NOT NULL,
  FE_ENVIO              TIMESTAMP(6)                NULL,
  FE_RECEPCION          TIMESTAMP(6)                NULL,
  NUM_REINTENTOS        INTEGER                 DEFAULT 0                         NULL,
  NUM_REGISTRO          VARCHAR2(20 CHAR)           NULL,
  FE_REGISTRO           TIMESTAMP(6)                NULL,
  NUM_REGISTRO_INICIAL  VARCHAR2(20 CHAR)       NOT NULL,
  FE_REGISTRO_INICIAL   TIMESTAMP(6)            NOT NULL,
  TS_REGISTRO_INICIAL   BLOB                        NULL,
  NUM_EXP               VARCHAR2(80 CHAR)           NULL,
  REFERENCIA_EXTERNA    VARCHAR2(16 CHAR)           NULL,
  TS_ENTRADA            BLOB                        NULL,
  CD_ENT_REG_ORIGEN     VARCHAR2(21 CHAR)       NOT NULL,
  DS_ENT_REG_ORIGEN     VARCHAR2(80 CHAR)           NULL,
  CD_ORG_ORIGEN         VARCHAR2(21 CHAR)           NULL,
  DS_ORG_ORIGEN         VARCHAR2(80 CHAR)           NULL,
  CD_ENT_REG_DESTINO    VARCHAR2(21 CHAR)       NOT NULL,
  DS_ENT_REG_DESTINO    VARCHAR2(80 CHAR)           NULL,
  CD_ORG_DESTINO        VARCHAR2(21 CHAR)           NULL,
  DS_ORG_DESTINO        VARCHAR2(80 CHAR)           NULL,
  DS_RESUMEN            VARCHAR2(240 CHAR)          NULL,
  CD_ASUNTO_DESTINO     VARCHAR2(16 CHAR)           NULL,
  CD_TIPO_TRANSPORTE    VARCHAR2(2 CHAR)            NULL,
  DS_NUM_TRANSPORTE     VARCHAR2(20 CHAR)           NULL,
  DS_NOMBRE_USUARIO     VARCHAR2(80 CHAR)           NULL,
  DS_CONTACTO_USUARIO   VARCHAR2(160 CHAR)          NULL,
  CD_APP_EMISORA        VARCHAR2(4 CHAR)            NULL,
  CD_TIPO_ANOTACION     VARCHAR2(2 CHAR)            NULL,
  DS_TIPO_ANOTACION     VARCHAR2(80 CHAR)           NULL,
  CD_TIPO_REGISTRO      VARCHAR2(1 CHAR)        NOT NULL,
  CD_DOC_FISICA         VARCHAR2(2 CHAR)        NOT NULL,
  DS_OBSERVACIONES      VARCHAR2(4000 CHAR)           NULL,
  CD_PRUEBA             VARCHAR2(1 CHAR)        NOT NULL,
  CD_ENT_REG_INICIO     VARCHAR2(21 CHAR)       NOT NULL,
  DS_ENT_REG_INICIO     VARCHAR2(80 CHAR)           NULL,
  DS_EXPONE             VARCHAR2(4000 CHAR)         NULL,
  DS_SOLICITA           VARCHAR2(4000 CHAR)         NULL,
  CD_ERROR              VARCHAR2(4 CHAR)            NULL,
  DS_ERROR              VARCHAR2(4000 CHAR)         NULL
)LOB (TS_REGISTRO_INICIAL) STORE AS (
  TABLESPACE SIGMSIR_LOB
  ENABLE       STORAGE IN ROW
  CHUNK       8192
  RETENTION
  NOCACHE
  INDEX (TABLESPACE SIGMSIR_LOB))
LOB (TS_ENTRADA) STORE AS (
  TABLESPACE SIGMSIR_LOB
  ENABLE       STORAGE IN ROW
  CHUNK       8192
  RETENTION
  NOCACHE
  INDEX (TABLESPACE SIGMSIR_LOB));

COMMENT ON COLUMN SIR_ASIENTOS_REGISTRALES.ID IS 'Identificador �nico del asiento registral';

COMMENT ON COLUMN SIR_ASIENTOS_REGISTRALES.CD_INTERCAMBIO IS 'Identificador de intercambio';

COMMENT ON COLUMN SIR_ASIENTOS_REGISTRALES.CD_ESTADO IS 'C�digo de estado';

COMMENT ON COLUMN SIR_ASIENTOS_REGISTRALES.FE_ESTADO IS 'Fecha de estado';

COMMENT ON COLUMN SIR_ASIENTOS_REGISTRALES.FE_ENVIO IS 'Fecha de env�o';

COMMENT ON COLUMN SIR_ASIENTOS_REGISTRALES.FE_RECEPCION IS 'Fecha de recepci�n';

COMMENT ON COLUMN SIR_ASIENTOS_REGISTRALES.NUM_REINTENTOS IS 'N�mero de reintentos de env�o';

COMMENT ON COLUMN SIR_ASIENTOS_REGISTRALES.NUM_REGISTRO IS 'N�mero de registro de entrada (en origen)';

COMMENT ON COLUMN SIR_ASIENTOS_REGISTRALES.FE_REGISTRO IS 'Fecha y hora de entrada (en origen)';

COMMENT ON COLUMN SIR_ASIENTOS_REGISTRALES.NUM_REGISTRO_INICIAL IS 'N�mero de registro inicial';

COMMENT ON COLUMN SIR_ASIENTOS_REGISTRALES.FE_REGISTRO_INICIAL IS 'Fecha y hora del registro inicial';

COMMENT ON COLUMN SIR_ASIENTOS_REGISTRALES.TS_REGISTRO_INICIAL IS 'Timestamp del registro inicial';

COMMENT ON COLUMN SIR_ASIENTOS_REGISTRALES.NUM_EXP IS 'N�mero de expediente objeto de la tramitaci�n administrativa';

COMMENT ON COLUMN SIR_ASIENTOS_REGISTRALES.REFERENCIA_EXTERNA IS 'Referencia que el destino precise conocer y sea conocida por el solicitante';

COMMENT ON COLUMN SIR_ASIENTOS_REGISTRALES.TS_ENTRADA IS 'Sello de tiempo (timestamp) del registro de entrada (en origen)';

COMMENT ON COLUMN SIR_ASIENTOS_REGISTRALES.CD_ENT_REG_ORIGEN IS 'C�digo de la entidad registral de origen';

COMMENT ON COLUMN SIR_ASIENTOS_REGISTRALES.DS_ENT_REG_ORIGEN IS 'Descripci�n de la entidad registral de origen';

COMMENT ON COLUMN SIR_ASIENTOS_REGISTRALES.CD_ORG_ORIGEN IS 'C�digo de la unidad de tramitaci�n de origen';

COMMENT ON COLUMN SIR_ASIENTOS_REGISTRALES.DS_ORG_ORIGEN IS 'Descripci�n de la unidad de tramitaci�n de origen';

COMMENT ON COLUMN SIR_ASIENTOS_REGISTRALES.CD_ENT_REG_DESTINO IS 'C�digo de la entidad registral de destino';

COMMENT ON COLUMN SIR_ASIENTOS_REGISTRALES.DS_ENT_REG_DESTINO IS 'Descripci�n de la entidad registral de destino';

COMMENT ON COLUMN SIR_ASIENTOS_REGISTRALES.CD_ORG_DESTINO IS 'C�digo de la unidad de tramitaci�n de destino';

COMMENT ON COLUMN SIR_ASIENTOS_REGISTRALES.DS_ORG_DESTINO IS 'Descripci�n de la unidad de tramitaci�n de destino';

COMMENT ON COLUMN SIR_ASIENTOS_REGISTRALES.DS_RESUMEN IS 'Abstract o resumen';

COMMENT ON COLUMN SIR_ASIENTOS_REGISTRALES.CD_ASUNTO_DESTINO IS 'C�digo de asunto en destino';

COMMENT ON COLUMN SIR_ASIENTOS_REGISTRALES.CD_TIPO_TRANSPORTE IS 'Tipo de transporte de entrada';

COMMENT ON COLUMN SIR_ASIENTOS_REGISTRALES.DS_NUM_TRANSPORTE IS 'N�mero de transporte de entrada';

COMMENT ON COLUMN SIR_ASIENTOS_REGISTRALES.DS_NOMBRE_USUARIO IS 'Nombre de usuario de origen';

COMMENT ON COLUMN SIR_ASIENTOS_REGISTRALES.DS_CONTACTO_USUARIO IS 'Contacto del usuario de origen (tel�fono o direcci�n de correo electr�nico)';

COMMENT ON COLUMN SIR_ASIENTOS_REGISTRALES.CD_APP_EMISORA IS 'Identifica la aplicaci�n emisora y su versi�n';

COMMENT ON COLUMN SIR_ASIENTOS_REGISTRALES.CD_TIPO_ANOTACION IS 'Tipo de la anotaci�n';

COMMENT ON COLUMN SIR_ASIENTOS_REGISTRALES.DS_TIPO_ANOTACION IS 'Descripci�n del tipo de anotaci�n';

COMMENT ON COLUMN SIR_ASIENTOS_REGISTRALES.CD_TIPO_REGISTRO IS 'Tipo de registro';

COMMENT ON COLUMN SIR_ASIENTOS_REGISTRALES.CD_DOC_FISICA IS 'Indica si el fichero va acompa�ado de documentaci�n f�sica';

COMMENT ON COLUMN SIR_ASIENTOS_REGISTRALES.DS_OBSERVACIONES IS 'Observaciones recogidas por el funcionario del registro';

COMMENT ON COLUMN SIR_ASIENTOS_REGISTRALES.CD_PRUEBA IS 'Indica si el asiento registral es una prueba';

COMMENT ON COLUMN SIR_ASIENTOS_REGISTRALES.CD_ENT_REG_INICIO IS 'C�digo de la entidad registral de inicio';

COMMENT ON COLUMN SIR_ASIENTOS_REGISTRALES.DS_ENT_REG_INICIO IS 'Descripci�n de la entidad registral de inicio';

COMMENT ON COLUMN SIR_ASIENTOS_REGISTRALES.DS_EXPONE IS 'Exposici�n de los hechos y antecedentes relacionados con la solicitud';

COMMENT ON COLUMN SIR_ASIENTOS_REGISTRALES.DS_SOLICITA IS 'Descripci�n del objeto de la solicitud';

COMMENT ON COLUMN SIR_ASIENTOS_REGISTRALES.CD_ERROR IS 'C�digo del error';

COMMENT ON COLUMN SIR_ASIENTOS_REGISTRALES.DS_ERROR IS 'Descripci�n del error';


CREATE TABLE SIR_CONFIGURACION
(
  ID           NUMBER                           NOT NULL,
  NOMBRE       VARCHAR2(250 CHAR)               NOT NULL,
  DESCRIPCION  CLOB                                 NULL,
  VALOR        CLOB                                 NULL
);

COMMENT ON COLUMN SIR_CONFIGURACION.ID IS 'Identificador �nico del par�metro de configuraci�n';

COMMENT ON COLUMN SIR_CONFIGURACION.NOMBRE IS 'Nombre del par�metro de configuraci�n';

COMMENT ON COLUMN SIR_CONFIGURACION.DESCRIPCION IS 'Descripci�n del par�metro de configuraci�n';

COMMENT ON COLUMN SIR_CONFIGURACION.VALOR IS 'Valor del par�metro de configuraci�n';


CREATE TABLE SIR_CONTADORES
(
  CD_ENT_REG  VARCHAR2(21 CHAR)                 NOT NULL,
  TIPO        NUMBER                            NOT NULL,
  CONTADOR    NUMBER                            NOT NULL
);

COMMENT ON COLUMN SIR_CONTADORES.CD_ENT_REG IS 'C�digo de la entidad registral';

COMMENT ON COLUMN SIR_CONTADORES.TIPO IS 'Tipo de contador';

COMMENT ON COLUMN SIR_CONTADORES.CONTADOR IS 'Contador';


CREATE TABLE SIR_INTERESADOS
(
  ID                         NUMBER             NOT NULL,
  ID_ASIENTO_REGISTRAL       NUMBER             NOT NULL,
  CD_TIPO_IDENTIFICADOR      VARCHAR2(1 CHAR)       NULL,
  DS_IDENTIFICADOR           VARCHAR2(17 CHAR)      NULL,
  DS_RAZON_SOCIAL            VARCHAR2(80 CHAR)      NULL,
  DS_NOMBRE                  VARCHAR2(30 CHAR)      NULL,
  DS_APELLIDO1               VARCHAR2(30 CHAR)      NULL,
  DS_APELLIDO2               VARCHAR2(30 CHAR)      NULL,
  CD_PAIS                    VARCHAR2(4 CHAR)       NULL,
  CD_PROVINCIA               VARCHAR2(2 CHAR)       NULL,
  CD_MUNICIPIO               VARCHAR2(5 CHAR)       NULL,
  DS_DIRECCION               VARCHAR2(160 CHAR)     NULL,
  CD_POSTAL                  VARCHAR2(5 CHAR)       NULL,
  DS_EMAIL                   VARCHAR2(160 CHAR)     NULL,
  DS_TELEFONO                VARCHAR2(20 CHAR)      NULL,
  DS_DEU                     VARCHAR2(160 CHAR)     NULL,
  CD_CANAL_PREF              VARCHAR2(2 CHAR)       NULL,
  CD_TIPO_IDENTIFICADOR_REP  VARCHAR2(1 CHAR)       NULL,
  DS_IDENTIFICADOR_REP       VARCHAR2(17 CHAR)      NULL,
  DS_RAZON_SOCIAL_REP        VARCHAR2(80 CHAR)      NULL,
  DS_NOMBRE_REP              VARCHAR2(30 CHAR)      NULL,
  DS_APELLIDO1_REP           VARCHAR2(30 CHAR)      NULL,
  DS_APELLIDO2_REP           VARCHAR2(30 CHAR)      NULL,
  CD_PAIS_REP                VARCHAR2(4 CHAR)       NULL,
  CD_PROVINCIA_REP           VARCHAR2(2 CHAR)       NULL,
  CD_MUNICIPIO_REP           VARCHAR2(5 CHAR)       NULL,
  DS_DIRECCION_REP           VARCHAR2(160 CHAR)     NULL,
  CD_POSTAL_REP              VARCHAR2(5 CHAR)       NULL,
  DS_EMAIL_REP               VARCHAR2(160 CHAR)     NULL,
  DS_TELEFONO_REP            VARCHAR2(20 CHAR)      NULL,
  DS_DEU_REP                 VARCHAR2(160 CHAR)     NULL,
  CD_CANAL_PREF_REP          VARCHAR2(2 CHAR)       NULL,
  DS_OBSERVACIONES           VARCHAR2(160 CHAR)     NULL
);

COMMENT ON COLUMN SIR_INTERESADOS.ID IS 'Identificador del interesado';

COMMENT ON COLUMN SIR_INTERESADOS.ID_ASIENTO_REGISTRAL IS 'Identificador del asiento registral';

COMMENT ON COLUMN SIR_INTERESADOS.CD_TIPO_IDENTIFICADOR IS 'C�digo del tipo de identificador del interesado';

COMMENT ON COLUMN SIR_INTERESADOS.DS_IDENTIFICADOR IS 'Identificador del interesado';

COMMENT ON COLUMN SIR_INTERESADOS.DS_RAZON_SOCIAL IS 'Raz�n social para el caso en el que el interesado sea persona jur�dica';

COMMENT ON COLUMN SIR_INTERESADOS.DS_NOMBRE IS 'Nombre del interesado';

COMMENT ON COLUMN SIR_INTERESADOS.DS_APELLIDO1 IS 'Primer apellido del interesado';

COMMENT ON COLUMN SIR_INTERESADOS.DS_APELLIDO2 IS 'Segundo apellido del interesado';

COMMENT ON COLUMN SIR_INTERESADOS.CD_PAIS IS 'C�digo del pa�s del interesado';

COMMENT ON COLUMN SIR_INTERESADOS.CD_PROVINCIA IS 'C�digo de la provincia del interesado';

COMMENT ON COLUMN SIR_INTERESADOS.CD_MUNICIPIO IS 'C�digo del municipio del interesado';

COMMENT ON COLUMN SIR_INTERESADOS.DS_DIRECCION IS 'Direcci�n postal del interesado';

COMMENT ON COLUMN SIR_INTERESADOS.CD_POSTAL IS 'C�digo postal del interesado';

COMMENT ON COLUMN SIR_INTERESADOS.DS_EMAIL IS 'Direcci�n de correo electr�nico del interesado';

COMMENT ON COLUMN SIR_INTERESADOS.DS_TELEFONO IS 'Tel�fono de contacto del interesado';

COMMENT ON COLUMN SIR_INTERESADOS.DS_DEU IS 'Direcci�n electr�nica habilitada del interesado';

COMMENT ON COLUMN SIR_INTERESADOS.CD_CANAL_PREF IS 'Canal preferente de notificaci�n del interesado';

COMMENT ON COLUMN SIR_INTERESADOS.CD_TIPO_IDENTIFICADOR_REP IS 'C�digo del tipo de identificador del representante';

COMMENT ON COLUMN SIR_INTERESADOS.DS_IDENTIFICADOR_REP IS 'Identificador del representante';

COMMENT ON COLUMN SIR_INTERESADOS.DS_RAZON_SOCIAL_REP IS 'Raz�n social para el caso en el que el representante sea persona jur�dica';

COMMENT ON COLUMN SIR_INTERESADOS.DS_NOMBRE_REP IS 'Nombre del representante';

COMMENT ON COLUMN SIR_INTERESADOS.DS_APELLIDO1_REP IS 'Primer apellido del representante';

COMMENT ON COLUMN SIR_INTERESADOS.DS_APELLIDO2_REP IS 'Segundo apellido del representante';

COMMENT ON COLUMN SIR_INTERESADOS.CD_PAIS_REP IS 'C�digo del pa�s del representante';

COMMENT ON COLUMN SIR_INTERESADOS.CD_PROVINCIA_REP IS 'C�digo de la provincia del representante';

COMMENT ON COLUMN SIR_INTERESADOS.CD_MUNICIPIO_REP IS 'C�digo del municipio del representante';

COMMENT ON COLUMN SIR_INTERESADOS.DS_DIRECCION_REP IS 'Direcci�n postal del representante';

COMMENT ON COLUMN SIR_INTERESADOS.CD_POSTAL_REP IS 'C�digo postal del representante';

COMMENT ON COLUMN SIR_INTERESADOS.DS_EMAIL_REP IS 'Direcci�n de correo electr�nico del representante';

COMMENT ON COLUMN SIR_INTERESADOS.DS_TELEFONO_REP IS 'Tel�fono de contacto del representante';

COMMENT ON COLUMN SIR_INTERESADOS.DS_DEU_REP IS 'Direcci�n electr�nica habilitada del representante';

COMMENT ON COLUMN SIR_INTERESADOS.CD_CANAL_PREF_REP IS 'Canal preferente de notificaci�n del representante';

COMMENT ON COLUMN SIR_INTERESADOS.DS_OBSERVACIONES IS 'Observaciones del interesado y/o del representante';


CREATE TABLE URL_APLICACIONES
(
  CD_OFICINA_REGISTRO  VARCHAR2(25 BYTE)        NOT NULL,
  DS_OFICINA_REGISTRO  VARCHAR2(200 BYTE)           NULL,
  URL                  VARCHAR2(200 BYTE)           NULL
);


CREATE TABLE SIR_ANEXOS
(
  ID                       NUMBER               NOT NULL,
  ID_ASIENTO_REGISTRAL     NUMBER               NOT NULL,
  CD_ID_FICHERO            VARCHAR2(50 CHAR)        NULL,
  DS_NOMBRE_FICHERO        VARCHAR2(80 CHAR)    NOT NULL,
  CD_VALIDEZ               VARCHAR2(2 CHAR)         NULL,
  CD_TIPO_DOCUMENTO        VARCHAR2(2 CHAR)     NOT NULL,
  CERTIFICADO              BLOB                     NULL,
  VALIDACION_OCSP          BLOB                     NULL,
  FIRMA                    BLOB                     NULL,
  TS_FICHERO               BLOB                     NULL,
  HASH                     BLOB                     NULL,
  TIPO_MIME                VARCHAR2(128 CHAR)       NULL,
  UID_DM                   VARCHAR2(128 CHAR)       NULL,
  ID_FICHERO_FIRMADO       NUMBER                   NULL,
  CD_ID_DOCUMENTO_FIRMADO  VARCHAR2(50 CHAR)        NULL,
  DS_OBSERVACIONES         VARCHAR2(50 CHAR)        NULL
)LOB (CERTIFICADO) STORE AS (
  TABLESPACE SIGMSIR_LOB
  ENABLE       STORAGE IN ROW
  CHUNK       8192
  RETENTION
  NOCACHE
  INDEX (TABLESPACE SIGMSIR_LOB))
LOB (VALIDACION_OCSP) STORE AS (
  TABLESPACE SIGMSIR_LOB
  ENABLE       STORAGE IN ROW
  CHUNK       8192
  RETENTION
  NOCACHE
  INDEX (TABLESPACE SIGMSIR_LOB))
LOB (FIRMA) STORE AS (
  TABLESPACE SIGMSIR_LOB
  ENABLE       STORAGE IN ROW
  CHUNK       8192
  RETENTION
  NOCACHE
  INDEX (TABLESPACE SIGMSIR_LOB))
LOB (TS_FICHERO) STORE AS (
  TABLESPACE SIGMSIR_LOB
  ENABLE       STORAGE IN ROW
  CHUNK       8192
  RETENTION
  NOCACHE
  INDEX (TABLESPACE SIGMSIR_LOB))
LOB (HASH) STORE AS (
  TABLESPACE SIGMSIR_LOB
  ENABLE       STORAGE IN ROW
  CHUNK       8192
  RETENTION
  NOCACHE
  INDEX (TABLESPACE SIGMSIR_LOB));

COMMENT ON COLUMN SIR_ANEXOS.ID IS 'Identificador del anexo';

COMMENT ON COLUMN SIR_ANEXOS.ID_ASIENTO_REGISTRAL IS 'Identificador del asiento registral';

COMMENT ON COLUMN SIR_ANEXOS.CD_ID_FICHERO IS 'Identificador del fichero intercambiado';

COMMENT ON COLUMN SIR_ANEXOS.DS_NOMBRE_FICHERO IS 'Nombre del fichero original del documento';

COMMENT ON COLUMN SIR_ANEXOS.CD_VALIDEZ IS 'Validez del documento';

COMMENT ON COLUMN SIR_ANEXOS.CD_TIPO_DOCUMENTO IS 'C�digo del tipo de documento';

COMMENT ON COLUMN SIR_ANEXOS.CERTIFICADO IS 'Certificado del fichero anexo';

COMMENT ON COLUMN SIR_ANEXOS.VALIDACION_OCSP IS 'Validaci�n OCSP del certificado';

COMMENT ON COLUMN SIR_ANEXOS.FIRMA IS 'Firma electr�nica del fichero anexo';

COMMENT ON COLUMN SIR_ANEXOS.TS_FICHERO IS 'Sello de tiempo del fichero anexo';

COMMENT ON COLUMN SIR_ANEXOS.HASH IS 'Huella binaria del fichero anexo';

COMMENT ON COLUMN SIR_ANEXOS.TIPO_MIME IS 'Tipo MIME del fichero anexo';

COMMENT ON COLUMN SIR_ANEXOS.UID_DM IS 'UID del contenido del anexo en el gestor documental';

COMMENT ON COLUMN SIR_ANEXOS.ID_FICHERO_FIRMADO IS 'Identificador (id) del fichero firmado';

COMMENT ON COLUMN SIR_ANEXOS.CD_ID_DOCUMENTO_FIRMADO IS 'Identificador (cd_id_fichero) del documento firmado';

COMMENT ON COLUMN SIR_ANEXOS.DS_OBSERVACIONES IS 'Observaciones del fichero adjunto';

COMMIT;
  
CREATE SEQUENCE HIBERNATE_SEQUENCE
  START WITH 1
  MAXVALUE 9223372036854775807
  MINVALUE 1
  NOCYCLE
  CACHE 20
  NOORDER;
  
  CREATE SEQUENCE SIR_ANEXOS_SEQ
  START WITH 1
  MAXVALUE 999999999999999999999999999
  MINVALUE 1
  NOCYCLE
  CACHE 20
  NOORDER;
  
  CREATE SEQUENCE SIR_AREGS_SEQ
  START WITH 1
  MAXVALUE 999999999999999999999999999
  MINVALUE 1
  NOCYCLE
  CACHE 20
  NOORDER;
  
  CREATE SEQUENCE SIR_CONFIG_SEQ
  START WITH 21
  MAXVALUE 999999999999999999999999999
  MINVALUE 1
  NOCYCLE
  CACHE 20
  NOORDER;
  
  CREATE SEQUENCE SIR_INTERESADOS_SEQ
  START WITH 1
  MAXVALUE 999999999999999999999999999
  MINVALUE 1
  NOCYCLE
  CACHE 20
  NOORDER;
  
  COMMIT;
  
  CREATE TABLE QRTZ_CALENDARS
(
  CALENDAR_NAME  VARCHAR2(200 BYTE)                 NULL,
  CALENDAR       BLOB                               NULL
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


CREATE TABLE QRTZ_BLOB_TRIGGERS
(
  TRIGGER_NAME   VARCHAR2(200 BYTE)                 NULL,
  TRIGGER_GROUP  VARCHAR2(200 BYTE)                 NULL,
  BLOB_DATA      BLOB                               NULL
);


CREATE TABLE QRTZ_CRON_TRIGGERS
(
  TRIGGER_NAME     VARCHAR2(200 BYTE)               NULL,
  TRIGGER_GROUP    VARCHAR2(200 BYTE)               NULL,
  CRON_EXPRESSION  VARCHAR2(120 BYTE)               NULL,
  TIME_ZONE_ID     VARCHAR2(80 BYTE)                NULL
);


CREATE TABLE QRTZ_SIMPLE_TRIGGERS
(
  TRIGGER_NAME     VARCHAR2(200 BYTE)               NULL,
  TRIGGER_GROUP    VARCHAR2(200 BYTE)               NULL,
  REPEAT_COUNT     NUMBER(7)                        NULL,
  REPEAT_INTERVAL  NUMBER(12)                       NULL,
  TIMES_TRIGGERED  NUMBER(10)                       NULL
);