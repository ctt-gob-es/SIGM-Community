--DROP TABLE spac_datos_firma;
-- TABLA
CREATE TABLE spac_datos_firma
(
  id integer NOT NULL,
  id_documento integer NOT NULL,
  usuario character varying(32) NOT NULL,
  nif character varying(20) NOT NULL,
  nombre character varying(250) NOT NULL,
  cargo character varying(250),
  fecha_firma timestamp without time zone NOT NULL,
  id_circuito integer,
  id_paso integer,
  CONSTRAINT pk_datos_firma PRIMARY KEY (id)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE spac_datos_firma
  OWNER TO postgres;

-- SECUENCIA
CREATE SEQUENCE spac_sq_id_datosfirma
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1
  CACHE 1;
ALTER TABLE spac_sq_id_datosfirma
  OWNER TO postgres;

-- SPAC_CT_ENTIDADES
INSERT INTO SPAC_CT_ENTIDADES VALUES
(20, 0, 'SPAC_DATOS_FIRMA', 'ID', '', '', null, 'Datos de firma de documentos', 'SPAC_SQ_ID_DATOSFIRMA', '', '', null);