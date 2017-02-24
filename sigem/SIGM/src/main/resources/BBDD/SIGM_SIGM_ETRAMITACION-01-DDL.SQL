CREATE TABLE CSV_APLICACIONES
(
  ID             INTEGER                        NOT NULL,
  CODIGO         VARCHAR2(32 BYTE)              NOT NULL,
  NOMBRE         VARCHAR2(1024 BYTE)            NOT NULL,
  INFO_CONEXION  CLOB                               NULL
);

COMMENT ON COLUMN CSV_APLICACIONES.ID IS 'Identificador de la aplicación';

COMMENT ON COLUMN CSV_APLICACIONES.CODIGO IS 'Código de la aplicación';

COMMENT ON COLUMN CSV_APLICACIONES.NOMBRE IS 'Nombre de la aplicación';

COMMENT ON COLUMN CSV_APLICACIONES.INFO_CONEXION IS 'Información de la conexión a la aplicación';


CREATE TABLE CSV_DOCUMENTOS
(
  ID               INTEGER                      NOT NULL,
  NOMBRE           VARCHAR2(1024 BYTE)          NOT NULL,
  DESCRIPCION      CLOB                             NULL,
  TIPO_MIME        VARCHAR2(256 BYTE)           NOT NULL,
  FECHA_CREACION   DATE                             NULL,
  FECHA_CADUCIDAD  DATE                             NULL,
  CSV              VARCHAR2(128 BYTE)           NOT NULL,
  FECHA_CSV        DATE                         NOT NULL,
  DISPONIBILIDAD   INTEGER                      NOT NULL,
  ID_APLICACION    INTEGER                      NOT NULL
);

COMMENT ON COLUMN CSV_DOCUMENTOS.ID IS 'Identificador del documento';

COMMENT ON COLUMN CSV_DOCUMENTOS.NOMBRE IS 'Nombre del documento';

COMMENT ON COLUMN CSV_DOCUMENTOS.DESCRIPCION IS 'Descripcion del documento';

COMMENT ON COLUMN CSV_DOCUMENTOS.TIPO_MIME IS 'Tipo MIME del documento';

COMMENT ON COLUMN CSV_DOCUMENTOS.FECHA_CREACION IS 'Fecha de creacion del documento';

COMMENT ON COLUMN CSV_DOCUMENTOS.FECHA_CADUCIDAD IS 'Fecha de caducidad del documento';

COMMENT ON COLUMN CSV_DOCUMENTOS.CSV IS 'CSV del documento';

COMMENT ON COLUMN CSV_DOCUMENTOS.FECHA_CSV IS 'Fecha de generación del CSV del documento';

COMMENT ON COLUMN CSV_DOCUMENTOS.DISPONIBILIDAD IS 'Disponibilidad del documento';

COMMENT ON COLUMN CSV_DOCUMENTOS.ID_APLICACION IS 'Identificador de la aplicación que contiene el documento';


CREATE TABLE SGMAFCONECTOR_AUTENTICACION
(
  TRAMITEID   VARCHAR2(32 CHAR)                 NOT NULL,
  CONECTORID  VARCHAR2(32 CHAR)                 NOT NULL
);


CREATE TABLE SGMCERTIFICACION
(
  ID_FICHERO  VARCHAR2(32 CHAR)                 NOT NULL,
  ID_USUARIO  VARCHAR2(32 CHAR)                 NOT NULL
);


CREATE TABLE SGMCTNOTIFICACION
(
  NOTIFICACION_ID             VARCHAR2(32 CHAR) NOT NULL,
  FECHA_NOTIFICACION          TIMESTAMP(6)      NOT NULL,
  DEU                         VARCHAR2(32 CHAR)     NULL,
  SERVICIO_NOTIFICACIONES_ID  VARCHAR2(32 CHAR)     NULL,
  EXPEDIENTE                  VARCHAR2(32 CHAR)     NULL,
  DESCRIPCION                 VARCHAR2(4000 CHAR)     NULL,
  HITO_ID                     VARCHAR2(32 CHAR) NOT NULL
);


CREATE TABLE SGMCTPAGO
(
  ENTIDAD_EMISORA_ID  VARCHAR2(32 CHAR)             NULL,
  AUTOLIQUIDACION_ID  VARCHAR2(32 CHAR)             NULL,
  PAGO_ID             VARCHAR2(32 CHAR)         NOT NULL,
  DOCUMENTO_ID        VARCHAR2(32 CHAR)             NULL,
  MENSAJE             VARCHAR2(4000 CHAR)           NULL,
  HITO_ID             VARCHAR2(32 CHAR)             NULL,
  FECHA_PAGO          TIMESTAMP(6)                  NULL,
  EXPEDIENTE          VARCHAR2(32 CHAR)             NULL,
  IMPORTE             VARCHAR2(12 CHAR)             NULL
);


CREATE TABLE SGMCTSUBSANACION
(
  SUBSANACION_ID     VARCHAR2(32 CHAR)          NOT NULL,
  DOCUMENTO_ID       VARCHAR2(32 CHAR)              NULL,
  MENSAJE            VARCHAR2(4000 CHAR)            NULL,
  HITO_ID            VARCHAR2(32 CHAR)              NULL,
  FECHA_SUBSANACION  TIMESTAMP(6)                   NULL,
  EXPEDIENTE         VARCHAR2(32 CHAR)              NULL
);


CREATE TABLE SGMNTINFO_ESTADO_NOTI
(
  CGUID         INTEGER                         NOT NULL,
  CUIDSISNOT    VARCHAR2(100 CHAR)                  NULL,
  CDESCRIPCION  VARCHAR2(100 CHAR)                  NULL
);


CREATE TABLE SGMNTINFO_NOTIFICACION
(
  CGUID                 VARCHAR2(64 CHAR)           NULL,
  CNIFDEST              VARCHAR2(10 CHAR)       NOT NULL,
  CNUMREG               VARCHAR2(16 CHAR)           NULL,
  CFHREG                DATE                        NULL,
  CNUMEXP               VARCHAR2(32 CHAR)       NOT NULL,
  CPROC                 VARCHAR2(50 CHAR)           NULL,
  CESTADO               INTEGER                     NULL,
  CFHESTADO             DATE                        NULL,
  CFHENTREGA            DATE                        NULL,
  CUSUARIO              VARCHAR2(10 CHAR)           NULL,
  CTIPOCORRESPONDENCIA  VARCHAR2(10 CHAR)           NULL,
  CORGANISMO            VARCHAR2(100 CHAR)          NULL,
  CASUNTO               VARCHAR2(100 CHAR)          NULL,
  CTIPO                 VARCHAR2(20 CHAR)           NULL,
  CTEXTO                VARCHAR2(1000 CHAR)         NULL,
  CNOMBREDEST           VARCHAR2(100 CHAR)          NULL,
  CAPELLIDOSDEST        VARCHAR2(150 CHAR)          NULL,
  CCORREODEST           VARCHAR2(200 CHAR)          NULL,
  CIDIOMA               VARCHAR2(20 CHAR)           NULL,
  CTIPOVIA              VARCHAR2(10 CHAR)           NULL,
  CVIA                  VARCHAR2(200 CHAR)          NULL,
  CNUMEROVIA            VARCHAR2(10 CHAR)           NULL,
  CESCALERAVIA          VARCHAR2(10 CHAR)           NULL,
  CPISOVIA              VARCHAR2(10 CHAR)           NULL,
  CPUERTAVIA            VARCHAR2(10 CHAR)           NULL,
  CTELEFONO             VARCHAR2(20 CHAR)           NULL,
  CMUNICIPIO            VARCHAR2(100 CHAR)          NULL,
  CPROVINCIA            VARCHAR2(100 CHAR)          NULL,
  CCP                   VARCHAR2(10 CHAR)           NULL,
  CERROR                VARCHAR2(100 CHAR)          NULL,
  CNOTIID               VARCHAR2(64 CHAR)       NOT NULL,
  CSISTEMAID            VARCHAR2(32 CHAR)           NULL,
  CDEU                  VARCHAR2(20 CHAR)           NULL,
  CTFNOMOVIL            VARCHAR2(14 CHAR)           NULL
);

COMMIT;

CREATE TABLE SGMRDEDOCUMENTOS
(
  GUID       VARCHAR2(64 CHAR)                  NOT NULL,
  CONTENIDO  BLOB                               NOT NULL,
  FILEHASH   VARCHAR2(64 CHAR)                  NOT NULL,
  EXTENSION  VARCHAR2(16 CHAR)                  NOT NULL,
  FECHA      TIMESTAMP(6)                       NOT NULL
);


CREATE TABLE SGMRDETIPOSMIME
(
  EXTENSION  VARCHAR2(8 CHAR)                   NOT NULL,
  TIPOMIME   VARCHAR2(64 CHAR)                  NOT NULL
);


CREATE TABLE SGMRTCATALOGO_CALENDARIO
(
  LABORABLES     VARCHAR2(13 CHAR)              NOT NULL,
  HORA_INICIO    VARCHAR2(2 CHAR)                   NULL,
  MINUTO_INICIO  VARCHAR2(2 CHAR)                   NULL,
  HORA_FIN       VARCHAR2(2 CHAR)                   NULL,
  MINUTO_FIN     VARCHAR2(2 CHAR)                   NULL
);


CREATE TABLE SGMRTCATALOGO_CONECTORES
(
  CONECTORID   VARCHAR2(32 CHAR)                NOT NULL,
  DESCRIPCION  VARCHAR2(256 CHAR)               NOT NULL,
  TIPO         VARCHAR2(32 CHAR)                NOT NULL
);


CREATE TABLE SGMRTCATALOGO_DIASCALENDARIO
(
  ID           INTEGER                          NOT NULL,
  FECHA        VARCHAR2(5 CHAR)                 NOT NULL,
  DESCRIPCION  VARCHAR2(256 CHAR)               NOT NULL,
  FIJO         INTEGER                          NOT NULL
);


CREATE TABLE SGMRTCATALOGO_DOCUMENTOS
(
  ID                  VARCHAR2(32 CHAR)         NOT NULL,
  DESCRIPCION         VARCHAR2(256 CHAR)        NOT NULL,
  EXTENSION           VARCHAR2(128 CHAR)        NOT NULL,
  CONECTOR_FIRMA      VARCHAR2(32 CHAR)             NULL,
  CONECTOR_CONTENIDO  VARCHAR2(32 CHAR)             NULL
);


CREATE TABLE SGMRTCATALOGO_ORGANOS
(
  ORGANO       VARCHAR2(16 CHAR)                NOT NULL,
  DESCRIPCION  VARCHAR2(250 CHAR)               NOT NULL
);


CREATE TABLE SGMRTCATALOGO_TIPOCONECTOR
(
  TIPOID       VARCHAR2(32 CHAR)                NOT NULL,
  DESCRIPCION  VARCHAR2(256 CHAR)               NOT NULL
);


CREATE TABLE SGMRTCATALOGO_TRAMITES
(
  ID                 VARCHAR2(32 CHAR)          NOT NULL,
  ASUNTO             VARCHAR2(32 CHAR)          NOT NULL,
  DESCRIPCION        VARCHAR2(256 CHAR)         NOT NULL,
  ORGANO             VARCHAR2(16 CHAR)          NOT NULL,
  LIMITE_DOCUMENTOS  VARCHAR2(1 CHAR)           NOT NULL,
  FIRMA              VARCHAR2(1 CHAR)           DEFAULT '1'                   NOT NULL,
  OFICINA            VARCHAR2(32 CHAR)          NOT NULL,
  ID_PROCEDIMIENTO   VARCHAR2(32 CHAR)              NULL
);


CREATE TABLE SGMRTREGISTRO
(
  NUMERO_REGISTRO     VARCHAR2(16 CHAR)         NOT NULL,
  FECHA_REGISTRO      TIMESTAMP(6)              NOT NULL,
  EMISOR_ID           VARCHAR2(128 CHAR)        NOT NULL,
  NOMBRE              VARCHAR2(128 CHAR)        NOT NULL,
  CORREO_ELECTRONICO  VARCHAR2(60 CHAR)             NULL,
  ASUNTO              VARCHAR2(200 CHAR)        NOT NULL,
  ORGANO              VARCHAR2(16 CHAR)         NOT NULL,
  TIPO_EMISOR_ID      VARCHAR2(1 CHAR)          NOT NULL,
  INFO_ADICIONAL      BLOB                          NULL,
  ESTADO              VARCHAR2(1 CHAR)          NOT NULL,
  OFICINA             VARCHAR2(32 CHAR)             NULL,
  NUMERO_EXPEDIENTE   VARCHAR2(32 CHAR)             NULL,
  FECHA_EFECTIVA      TIMESTAMP(6)                  NULL
);


CREATE TABLE SGMRTREGISTRO_DOCS_CSV
(
  GUID  VARCHAR2(32 CHAR)                       NOT NULL,
  CSV   VARCHAR2(128 CHAR)                      NOT NULL
);


CREATE TABLE SGMRTREGISTRO_DOCUMENTOS
(
  NUMERO_REGISTRO     VARCHAR2(16 CHAR)         NOT NULL,
  CODIGO              VARCHAR2(32 CHAR)         NOT NULL,
  GUID                VARCHAR2(32 CHAR)         NOT NULL,
  CONECTOR_FIRMA      VARCHAR2(32 CHAR)             NULL,
  CONECTOR_DOCUMENTO  VARCHAR2(32 CHAR)             NULL
);


CREATE TABLE SGMRTREGISTRO_SECUENCIA
(
  ANYO       VARCHAR2(4 CHAR)                   NOT NULL,
  ETIQUETA   VARCHAR2(8 CHAR)                   NOT NULL,
  SECUENCIA  INTEGER                            NOT NULL
);

COMMIT;

CREATE TABLE SGMRTTMP_DOCUMENTOS
(
  SESIONID  VARCHAR2(64 CHAR)                   NOT NULL,
  GUID      VARCHAR2(32 CHAR)                   NOT NULL,
  FECHA     TIMESTAMP(6)                        NOT NULL
);


CREATE TABLE SGM_AU_USUARIOS
(
  NOMBRE              VARCHAR2(50 CHAR)         NOT NULL,
  APELLIDOS           VARCHAR2(50 CHAR)         NOT NULL,
  NIF                 VARCHAR2(32 CHAR)         NOT NULL,
  CORREO_ELECTRONICO  VARCHAR2(60 CHAR)         NOT NULL,
  USUARIO             VARCHAR2(15 CHAR)         NOT NULL,
  PASSWORD            VARCHAR2(100 CHAR)        NOT NULL
);


CREATE TABLE SGM_CNXUSR
(
  CNIF        VARCHAR2(16 CHAR)                 NOT NULL,
  CFLAGS      INTEGER                           NOT NULL,
  CFECHAHORA  TIMESTAMP(6)                      NOT NULL
);


CREATE TABLE SGM_CTFICHHITO
(
  CGUIDHITO  VARCHAR2(32 CHAR)                  NOT NULL,
  CGUIDFICH  VARCHAR2(32 CHAR)                  NOT NULL,
  CTITULO    VARCHAR2(128 CHAR)                 NOT NULL
);


CREATE TABLE SGM_CTHITOESTEXP
(
  CNUMEXP   VARCHAR2(32 CHAR)                   NOT NULL,
  CGUID     VARCHAR2(32 CHAR)                   NOT NULL,
  CCOD      VARCHAR2(32 CHAR)                       NULL,
  CFECHA    TIMESTAMP(6)                        NOT NULL,
  CDESCR    VARCHAR2(4000 CHAR)                 NOT NULL,
  CINFOAUX  BLOB                                    NULL
);


CREATE TABLE SGM_CTHITOHISTEXP
(
  CNUMEXP   VARCHAR2(32 CHAR)                   NOT NULL,
  CGUID     VARCHAR2(32 CHAR)                   NOT NULL,
  CCOD      VARCHAR2(32 CHAR)                       NULL,
  CFECHA    TIMESTAMP(6)                        NOT NULL,
  CDESCR    VARCHAR2(4000 CHAR)                 NOT NULL,
  CINFOAUX  BLOB                                    NULL
);


CREATE TABLE SGM_CTINFOEXP
(
  CNUMEXP      VARCHAR2(32 CHAR)                NOT NULL,
  CPROC        VARCHAR2(254 CHAR)                   NULL,
  CFHINICIO    DATE                             NOT NULL,
  CNUMREGINI   VARCHAR2(32 CHAR)                    NULL,
  CFHREGINI    DATE                                 NULL,
  CINFOAUX     VARCHAR2(4000 CHAR)                  NULL,
  CAPORTACION  CHAR(1 CHAR)                         NULL,
  CCODPRES     VARCHAR2(32 CHAR)                    NULL,
  ESTADO       VARCHAR2(1 CHAR)                 DEFAULT '0'                   NOT NULL
);


CREATE TABLE SGM_CTINTEXP
(
  CNUMEXP     VARCHAR2(32 CHAR)                 NOT NULL,
  CNIF        VARCHAR2(16 CHAR)                 NOT NULL,
  CNOM        VARCHAR2(128 CHAR)                    NULL,
  CPRINCIPAL  CHAR(1 CHAR)                      NOT NULL,
  CINFOAUX    VARCHAR2(4000 CHAR)                   NULL
);


CREATE TABLE SGM_NTFICHNOTIF
(
  CGUID       VARCHAR2(32 CHAR)                 NOT NULL,
  CGUIDNOTIF  VARCHAR2(32 CHAR)                 NOT NULL,
  CCODIGO     VARCHAR2(32 CHAR)                     NULL,
  CROL        VARCHAR2(32 CHAR)                     NULL,
  CTITULO     VARCHAR2(128 CHAR)                NOT NULL
);


CREATE TABLE SGM_NTINFONOTIF
(
  CGUID       VARCHAR2(32 CHAR)                 NOT NULL,
  CNIFDEST    VARCHAR2(16 CHAR)                 NOT NULL,
  CNUMREG     VARCHAR2(32 CHAR)                 NOT NULL,
  CFHREG      TIMESTAMP(6)                      NOT NULL,
  CNUMEXP     VARCHAR2(32 CHAR)                 NOT NULL,
  CPROC       VARCHAR2(254 CHAR)                NOT NULL,
  CINFOAUX    VARCHAR2(4000 CHAR)                   NULL,
  CAVISADA    INTEGER                           NOT NULL,
  CFHAVISO    TIMESTAMP(6)                          NULL,
  CENTREGADA  CHAR(1 CHAR)                      NOT NULL,
  CFHENTREGA  TIMESTAMP(6)                          NULL,
  CDIRCE      VARCHAR2(128 CHAR)                NOT NULL,
  CASUNTOMCE  VARCHAR2(254 CHAR)                NOT NULL,
  CTEXTOMCE   VARCHAR2(254 CHAR)                NOT NULL,
  CTITULO     VARCHAR2(128 CHAR)                NOT NULL,
  CTITULODE   VARCHAR2(128 CHAR)                NOT NULL,
  CTITULOAR   VARCHAR2(128 CHAR)                NOT NULL
);


CREATE TABLE SGM_PE_AL12NSEC
(
  NUMSEC  NUMBER                                NOT NULL,
  MINIMO  NUMBER                                NOT NULL,
  MAXIMO  NUMBER                                NOT NULL
);


CREATE TABLE SGM_PE_AL3NSEC
(
  MODELO  VARCHAR2(3 CHAR)                      NOT NULL,
  NUMSEC  NUMBER                                NOT NULL,
  MINIMO  NUMBER                                NOT NULL,
  MAXIMO  NUMBER                                NOT NULL
);


CREATE TABLE SGM_PE_LIQUIDACIONES
(
  REFERENCIA             VARCHAR2(13 CHAR)      NOT NULL,
  ID_ENTIDAD_EMISORA     VARCHAR2(6 CHAR)       NOT NULL,
  ID_TASA                VARCHAR2(3 CHAR)       NOT NULL,
  EJERCICIO              VARCHAR2(4 CHAR)           NULL,
  VENCIMIENTO            VARCHAR2(8 CHAR)           NULL,
  DISCRIMINANTE_PERIODO  VARCHAR2(1 CHAR)           NULL,
  REMESA                 VARCHAR2(2 CHAR)           NULL,
  NIF                    VARCHAR2(9 CHAR)       NOT NULL,
  IMPORTE                VARCHAR2(12 CHAR)      NOT NULL,
  NRC                    VARCHAR2(22 CHAR)          NULL,
  ESTADO                 VARCHAR2(2 CHAR)           NULL,
  NOMBRE                 VARCHAR2(512 CHAR)         NULL,
  DATOS_ESPECIFICOS      VARCHAR2(2048 CHAR)        NULL,
  INICIO_PERIODO         DATE                       NULL,
  FIN_PERIODO            DATE                       NULL,
  SOLICITUD              BLOB                       NULL,
  FECHA_PAGO             TIMESTAMP(6)               NULL
);


CREATE TABLE SGM_PE_TASAS
(
  CODIGO              VARCHAR2(3 CHAR)          NOT NULL,
  ID_ENTIDAD_EMISORA  VARCHAR2(6 CHAR)          NOT NULL,
  NOMBRE              VARCHAR2(255 CHAR)            NULL,
  TIPO                VARCHAR2(3 CHAR)          NOT NULL,
  MODELO              VARCHAR2(3 CHAR)              NULL,
  CAPTURA             VARCHAR2(1 CHAR)              NULL,
  DATOS_ESPECIFICOS   VARCHAR2(2048 CHAR)           NULL
);


CREATE TABLE SGMAFSESION_INFO
(
  SESIONID                        VARCHAR2(32 CHAR) NOT NULL,
  CONECTORID                      VARCHAR2(32 CHAR) NOT NULL,
  FECHA_LOGIN                     VARCHAR2(19 CHAR) NOT NULL,
  NOMBRE_SOLICITANTE              VARCHAR2(128 CHAR) NOT NULL,
  SOLICITANTE_ID                  VARCHAR2(32 CHAR) NOT NULL,
  CORREO_ELECTRONICO_SOLICITANTE  VARCHAR2(60 CHAR)     NULL,
  EMISOR_CERTIFICADO_SOLICITANTE  VARCHAR2(256 CHAR)     NULL,
  SOLICITANTE_CERTIFICADO_SN      VARCHAR2(256 CHAR)     NULL,
  SOLICITANTE_INQUALITY           VARCHAR2(32 CHAR)     NULL,
  RAZON_SOCIAL                    VARCHAR2(256 CHAR)     NULL,
  CIF                             VARCHAR2(9 CHAR)     NULL,
  ID_ENTIDAD                      VARCHAR2(10 CHAR)     NULL,
  SOLICITANTE_FIRSTNAME           VARCHAR2(36 CHAR)     NULL,
  SOLICITANTE_SURNAME             VARCHAR2(45 CHAR)     NULL,
  SOLICITANTE_SURNAME2            VARCHAR2(45 CHAR)     NULL
);


CREATE TABLE SGMNTINFO_DOCUMENTO
(
  CNOTIEXPE     VARCHAR2(32 CHAR)               NOT NULL,
  CNOTINIFDEST  VARCHAR2(10 CHAR)               NOT NULL,
  CCODIGO       VARCHAR2(200 CHAR)                  NULL,
  CGUID         VARCHAR2(100 CHAR)                  NULL,
  CNOTIID       VARCHAR2(64 CHAR)               NOT NULL,
  CTIPODOC      INTEGER                         DEFAULT 1                         NULL
);


CREATE TABLE SGMRTCATALOGO_DOCSTRAMITE
(
  TRAMITE_ID             VARCHAR2(32 CHAR)      NOT NULL,
  DOCUMENTO_ID           VARCHAR2(32 CHAR)      NOT NULL,
  CODIGO_DOCUMENTO       VARCHAR2(32 CHAR)      NOT NULL,
  DOCUMENTO_OBLIGATORIO  VARCHAR2(1 CHAR)       NOT NULL
);

COMMIT;

CREATE SEQUENCE CSV_APPS_SEQ
  START WITH 21
  MAXVALUE 999999999999999999999999999
  MINVALUE 1
  NOCYCLE
  CACHE 20
  NOORDER;
  
CREATE SEQUENCE CSV_DOCS_SEQ
  START WITH 1
  MAXVALUE 999999999999999999999999999
  MINVALUE 1
  NOCYCLE
  CACHE 20
  NOORDER;

COMMIT;