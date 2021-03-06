--
-- Secuencias
--

CREATE SEQUENCE csv_apps_seq;
CREATE SEQUENCE csv_docs_seq;


--
-- Tablas
--

CREATE TABLE csv_aplicaciones (
  id integer NOT NULL,
  codigo character varying(32) NOT NULL,
  nombre character varying(1024) NOT NULL,
  info_conexion text
);

COMMENT ON COLUMN csv_aplicaciones.id IS 'Identificador de la aplicación';
COMMENT ON COLUMN csv_aplicaciones.codigo IS 'Código de la aplicación';
COMMENT ON COLUMN csv_aplicaciones.nombre IS 'Nombre de la aplicación';
COMMENT ON COLUMN csv_aplicaciones.info_conexion IS 'Información de la conexión a la aplicación';

CREATE TABLE csv_documentos (
  id integer NOT NULL,
  nombre character varying(1024) NOT NULL,
  descripcion text,
  tipo_mime character varying(256) NOT NULL,
  fecha_creacion timestamp without time zone,
  fecha_caducidad timestamp without time zone,
  csv character varying(128) NOT NULL,
  fecha_csv timestamp without time zone NOT NULL,
  disponibilidad integer NOT NULL,
  id_aplicacion integer NOT NULL
);

COMMENT ON COLUMN csv_documentos.id IS 'Identificador del documento';
COMMENT ON COLUMN csv_documentos.nombre IS 'Nombre del documento';
COMMENT ON COLUMN csv_documentos.descripcion IS 'Descripcion del documento';
COMMENT ON COLUMN csv_documentos.tipo_mime IS 'Tipo MIME del documento';
COMMENT ON COLUMN csv_documentos.fecha_creacion IS 'Fecha de creacion del documento';
COMMENT ON COLUMN csv_documentos.fecha_caducidad IS 'Fecha de caducidad del documento';
COMMENT ON COLUMN csv_documentos.csv IS 'CSV del documento';
COMMENT ON COLUMN csv_documentos.fecha_csv IS 'Fecha de generación del CSV del documento';
COMMENT ON COLUMN csv_documentos.disponibilidad IS 'Disponibilidad del documento';
COMMENT ON COLUMN csv_documentos.id_aplicacion IS 'Identificador de la aplicación que contiene el documento';


--
-- Restricciones
--

ALTER TABLE csv_aplicaciones ADD CONSTRAINT pk_csv_aplicaciones PRIMARY KEY(id);

ALTER TABLE csv_documentos ADD CONSTRAINT pk_csv_documentos PRIMARY KEY(id);
ALTER TABLE csv_documentos ADD CONSTRAINT fk_csv_documentos_aplicacion
	FOREIGN KEY (id_aplicacion) REFERENCES csv_aplicaciones(id);


--
-- Índices
--

CREATE UNIQUE INDEX ix_csv_aplicaciones_codigo ON csv_aplicaciones(codigo);
CREATE UNIQUE INDEX ix_csv_documentos_csv ON csv_documentos(csv);



--
-- Alta de aplicación para el Registro Telemático
--

INSERT INTO csv_aplicaciones(id, codigo, nombre, info_conexion)
	VALUES (nextval('csv_apps_seq'), 'SGM_REG_TEL', 'SIGM - Registro Telemático', '<?xml version=''1.0'' encoding=''UTF-8''?><connection-config><connector>SIGEM_WEB_SERVICE</connector><parameters><parameter name="WSDL_LOCATION">http://localhost:8080/SIGEM_RegistroTelematicoWS/services/AplicacionExternaCSVConnectorWS?wsdl</parameter></parameters></connection-config>');


--
-- Alta de aplicación para la Tramitación de Expedientes
--

INSERT INTO csv_aplicaciones(id, codigo, nombre, info_conexion)
	VALUES (nextval('csv_apps_seq'), 'SGM_TRAM_EXP', 'SIGM - Tramitación de Expedientes', '<?xml version=''1.0'' encoding=''UTF-8''?><connection-config><connector>SIGEM_WEB_SERVICE</connector><parameters><parameter name="WSDL_LOCATION">http://localhost:8080/SIGEM_TramitacionWS/services/AplicacionExternaCSVConnectorWS?wsdl</parameter></parameters></connection-config>');


