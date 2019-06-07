--DROP TABLE spac_datos_firma;
-- TABLA
CREATE TABLE spac_datos_firma
(
  id integer NOT NULL,
  id_documento integer NOT NULL,
  usuario varchar2(32 char) NOT NULL,
  nif varchar2(20 char) NOT NULL,
  nombre varchar2(250 char) NOT NULL,
  cargo varchar2(250 char),
  fecha_firma timestamp NOT NULL,
  id_circuito integer,
  id_paso integer,
  CONSTRAINT pk_datos_firma PRIMARY KEY (id)
)
;

-- SECUENCIA
CREATE SEQUENCE spac_sq_id_datosfirma
  INCREMENT BY 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START WITH 1;  
  

-- SPAC_CT_ENTIDADES
INSERT INTO SPAC_CT_ENTIDADES VALUES
(20, 0, 'SPAC_DATOS_FIRMA', 'ID', '', '', null, 'Datos de firma de documentos', 'SPAC_SQ_ID_DATOSFIRMA', '', '', null);

