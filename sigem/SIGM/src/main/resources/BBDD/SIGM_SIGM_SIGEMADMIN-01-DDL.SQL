CREATE TABLE SGM_ADM_ACCIONES
(
  ID              NUMBER                        NOT NULL,
  NOMBRE          VARCHAR2(256 CHAR)            NOT NULL,
  CLASE_CONFIG    VARCHAR2(256 CHAR)            NOT NULL,
  CLASE_EXEC      VARCHAR2(256 CHAR)            NOT NULL,
  INFO_ADICIONAL  CLOB                              NULL
);


CREATE TABLE SGM_ADM_ENTIDADES
(
  ID           VARCHAR2(3 CHAR)                 NOT NULL,
  NOMBRECORTO  VARCHAR2(70 CHAR)                NOT NULL,
  NOMBRELARGO  VARCHAR2(500 CHAR)                   NULL,
  CODIGO_INE   VARCHAR2(12 CHAR)                    NULL
);


CREATE TABLE SGM_ADM_USUARIOS
(
  USUARIO     VARCHAR2(15 CHAR)                 NOT NULL,
  PASSWORD    VARCHAR2(100 CHAR)                NOT NULL,
  NOMBRE      VARCHAR2(50 CHAR)                 NOT NULL,
  APELLIDOS   VARCHAR2(50 CHAR)                 NOT NULL,
  FECHA_ALTA  DATE                              NOT NULL
);


CREATE TABLE SGM_ADM_PERFILES
(
  ID_ENTIDAD     VARCHAR2(3 CHAR)               NOT NULL,
  ID_USUARIO     VARCHAR2(15 CHAR)              NOT NULL,
  ID_APLICACION  VARCHAR2(2 CHAR)               NOT NULL
);

COMMIT;

/* Formatted on 08/10/2014 16:14:21 (QP5 v5.139.911.3011) */
CREATE OR REPLACE FORCE VIEW SGM_ADM_USUARIOS_V
(
   ID_ENTIDAD,
   ID_USUARIO,
   PASSWORD,
   NOMBRE,
   APELLIDOS,
   FECHA_ALTA
)
AS
   SELECT DISTINCT sgm_adm_perfiles.id_entidad,
                   sgm_adm_perfiles.id_usuario,
                   sgm_adm_usuarios.password,
                   sgm_adm_usuarios.nombre,
                   sgm_adm_usuarios.apellidos,
                   sgm_adm_usuarios.fecha_alta
     FROM sgm_adm_perfiles, sgm_adm_usuarios
    WHERE sgm_adm_perfiles.id_usuario = sgm_adm_usuarios.usuario;
	
COMMIT;