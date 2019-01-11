drop table spac_s_sesion_mensaje;

CREATE TABLE spac_s_sesion_mensaje (
  id integer NOT NULL,
  id_sesion varchar(200),
  texto text,
  usuario varchar(200),
  tipo varchar(200),
  fecha_mensaje timestamp without time zone,
  CONSTRAINT spac_s_sesion_mensaje_pk PRIMARY KEY (id)
);

DROP SEQUENCE spac_sq_2081913613;

CREATE SEQUENCE SPAC_SQ_S_SESION_MENSAJE
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1
  CACHE 1;