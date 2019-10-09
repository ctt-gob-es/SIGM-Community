drop table spac_s_sesion_mensaje;

CREATE TABLE spac_s_sesion_mensaje (
  id integer NOT NULL,
  id_sesion varchar2(200 CHAR),
  texto clob,
  usuario varchar2(200 CHAR),
  tipo varchar2(200 CHAR),
  fecha_mensaje timestamp,
  CONSTRAINT spac_s_sesion_mensaje_pk PRIMARY KEY (id)
);

DROP SEQUENCE spac_sq_2081913613;

CREATE SEQUENCE SPAC_SQ_S_SESION_MENSAJE
  INCREMENT by 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START with 1;