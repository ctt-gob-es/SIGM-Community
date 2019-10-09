INSERT INTO csv_aplicaciones(id, codigo, nombre, info_conexion)
	VALUES (csv_apps_seq.nextval, 'SGM_REG_PRES', 'SIGM - Registro Presencial', '<?xml version=''1.0'' encoding=''UTF-8''?><connection-config><connector>SIGEM_WEB_SERVICE</connector><parameters><parameter name="WSDL_LOCATION">http://localhost:8080/SIGEM_RegistroPresencialWS/services/AplicacionExternaCSVConnectorWS?wsdl</parameter></parameters></connection-config>');

