Insert into CSV_APLICACIONES
   (ID, CODIGO, NOMBRE, INFO_CONEXION)
 Values
   (1, 'SGM_REG_TEL', 'SIGM - Registro Telemático', '<?xml version=''1.0'' encoding=''UTF-8''?><connection-config><connector>SIGEM_WEB_SERVICE</connector><parameters><parameter name="WSDL_LOCATION">http://localhost:8080/SIGEM_RegistroTelematicoWS/services/AplicacionExternaCSVConnectorWS?wsdl</parameter></parameters></connection-config>');
Insert into CSV_APLICACIONES
   (ID, CODIGO, NOMBRE, INFO_CONEXION)
 Values
   (2, 'SGM_TRAM_EXP', 'SIGM - Tramitación de Expedientes', '<?xml version=''1.0'' encoding=''UTF-8''?><connection-config><connector>SIGEM_WEB_SERVICE</connector><parameters><parameter name="WSDL_LOCATION">http://localhost:8080/SIGEM_TramitacionWS/services/AplicacionExternaCSVConnectorWS?wsdl</parameter></parameters></connection-config>');
COMMIT;

SET DEFINE OFF;
Insert into SGM_PE_AL12NSEC
   (NUMSEC, MINIMO, MAXIMO)
 Values
   (1, 1, 1000);
COMMIT;

SET DEFINE OFF;
Insert into SGM_PE_AL3NSEC
   (MODELO, NUMSEC, MINIMO, MAXIMO)
 Values
   ('001', 1, 1, 1000);
COMMIT;

SET DEFINE OFF;
Insert into SGM_PE_TASAS
   (CODIGO, ID_ENTIDAD_EMISORA, NOMBRE, TIPO, MODELO, 
    CAPTURA, DATOS_ESPECIFICOS)
 Values
   ('000', '000000', 'Liquidacion AL2', 'AL2', NULL, 
    NULL, NULL);
Insert into SGM_PE_TASAS
   (CODIGO, ID_ENTIDAD_EMISORA, NOMBRE, TIPO, MODELO, 
    CAPTURA, DATOS_ESPECIFICOS)
 Values
   ('100', '000000', 'Liquidacion AL1', 'AL1', NULL, 
    NULL, '<NOMBRE>Tributo del Ayuntamiento</NOMBRE>');
Insert into SGM_PE_TASAS
   (CODIGO, ID_ENTIDAD_EMISORA, NOMBRE, TIPO, MODELO, 
    CAPTURA, DATOS_ESPECIFICOS)
 Values
   ('200', '000000', 'Prueba autoliquidacion', 'AL3', '001', 
    '0', '<NOMBRE>AUTOLIQUIDACION DE PRUEBAS</NOMBRE>');
COMMIT;

SET DEFINE OFF;
Insert into SGMAFCONECTOR_AUTENTICACION
   (TRAMITEID, CONECTORID)
 Values
   ('TRAM_1', 'AUTH_CERTIFICADO');
Insert into SGMAFCONECTOR_AUTENTICACION
   (TRAMITEID, CONECTORID)
 Values
   ('TRAM_1', 'AUTH_WEB_USER');
Insert into SGMAFCONECTOR_AUTENTICACION
   (TRAMITEID, CONECTORID)
 Values
   ('TRAM_10', 'AUTH_CERTIFICADO');
Insert into SGMAFCONECTOR_AUTENTICACION
   (TRAMITEID, CONECTORID)
 Values
   ('TRAM_10', 'AUTH_WEB_USER');
Insert into SGMAFCONECTOR_AUTENTICACION
   (TRAMITEID, CONECTORID)
 Values
   ('TRAM_11', 'AUTH_CERTIFICADO');
Insert into SGMAFCONECTOR_AUTENTICACION
   (TRAMITEID, CONECTORID)
 Values
   ('TRAM_11', 'AUTH_WEB_USER');
Insert into SGMAFCONECTOR_AUTENTICACION
   (TRAMITEID, CONECTORID)
 Values
   ('TRAM_12', 'AUTH_CERTIFICADO');
Insert into SGMAFCONECTOR_AUTENTICACION
   (TRAMITEID, CONECTORID)
 Values
   ('TRAM_12', 'AUTH_WEB_USER');
Insert into SGMAFCONECTOR_AUTENTICACION
   (TRAMITEID, CONECTORID)
 Values
   ('TRAM_13', 'AUTH_CERTIFICADO');
Insert into SGMAFCONECTOR_AUTENTICACION
   (TRAMITEID, CONECTORID)
 Values
   ('TRAM_13', 'AUTH_WEB_USER');
Insert into SGMAFCONECTOR_AUTENTICACION
   (TRAMITEID, CONECTORID)
 Values
   ('TRAM_14', 'AUTH_CERTIFICADO');
Insert into SGMAFCONECTOR_AUTENTICACION
   (TRAMITEID, CONECTORID)
 Values
   ('TRAM_14', 'AUTH_WEB_USER');
Insert into SGMAFCONECTOR_AUTENTICACION
   (TRAMITEID, CONECTORID)
 Values
   ('TRAM_15', 'AUTH_CERTIFICADO');
Insert into SGMAFCONECTOR_AUTENTICACION
   (TRAMITEID, CONECTORID)
 Values
   ('TRAM_15', 'AUTH_WEB_USER');
Insert into SGMAFCONECTOR_AUTENTICACION
   (TRAMITEID, CONECTORID)
 Values
   ('TRAM_16', 'AUTH_CERTIFICADO');
Insert into SGMAFCONECTOR_AUTENTICACION
   (TRAMITEID, CONECTORID)
 Values
   ('TRAM_16', 'AUTH_WEB_USER');
Insert into SGMAFCONECTOR_AUTENTICACION
   (TRAMITEID, CONECTORID)
 Values
   ('TRAM_17', 'AUTH_CERTIFICADO');
Insert into SGMAFCONECTOR_AUTENTICACION
   (TRAMITEID, CONECTORID)
 Values
   ('TRAM_17', 'AUTH_WEB_USER');
Insert into SGMAFCONECTOR_AUTENTICACION
   (TRAMITEID, CONECTORID)
 Values
   ('TRAM_18', 'AUTH_CERTIFICADO');
Insert into SGMAFCONECTOR_AUTENTICACION
   (TRAMITEID, CONECTORID)
 Values
   ('TRAM_18', 'AUTH_WEB_USER');
Insert into SGMAFCONECTOR_AUTENTICACION
   (TRAMITEID, CONECTORID)
 Values
   ('TRAM_19', 'AUTH_CERTIFICADO');
Insert into SGMAFCONECTOR_AUTENTICACION
   (TRAMITEID, CONECTORID)
 Values
   ('TRAM_19', 'AUTH_WEB_USER');
Insert into SGMAFCONECTOR_AUTENTICACION
   (TRAMITEID, CONECTORID)
 Values
   ('TRAM_3', 'AUTH_CERTIFICADO');
Insert into SGMAFCONECTOR_AUTENTICACION
   (TRAMITEID, CONECTORID)
 Values
   ('TRAM_3', 'AUTH_WEB_USER');
Insert into SGMAFCONECTOR_AUTENTICACION
   (TRAMITEID, CONECTORID)
 Values
   ('TRAM_4', 'AUTH_CERTIFICADO');
Insert into SGMAFCONECTOR_AUTENTICACION
   (TRAMITEID, CONECTORID)
 Values
   ('TRAM_4', 'AUTH_WEB_USER');
Insert into SGMAFCONECTOR_AUTENTICACION
   (TRAMITEID, CONECTORID)
 Values
   ('TRAM_5', 'AUTH_CERTIFICADO');
Insert into SGMAFCONECTOR_AUTENTICACION
   (TRAMITEID, CONECTORID)
 Values
   ('TRAM_5', 'AUTH_WEB_USER');
Insert into SGMAFCONECTOR_AUTENTICACION
   (TRAMITEID, CONECTORID)
 Values
   ('TRAM_6', 'AUTH_CERTIFICADO');
Insert into SGMAFCONECTOR_AUTENTICACION
   (TRAMITEID, CONECTORID)
 Values
   ('TRAM_6', 'AUTH_WEB_USER');
Insert into SGMAFCONECTOR_AUTENTICACION
   (TRAMITEID, CONECTORID)
 Values
   ('TRAM_7', 'AUTH_CERTIFICADO');
Insert into SGMAFCONECTOR_AUTENTICACION
   (TRAMITEID, CONECTORID)
 Values
   ('TRAM_7', 'AUTH_WEB_USER');
Insert into SGMAFCONECTOR_AUTENTICACION
   (TRAMITEID, CONECTORID)
 Values
   ('TRAM_8', 'AUTH_CERTIFICADO');
Insert into SGMAFCONECTOR_AUTENTICACION
   (TRAMITEID, CONECTORID)
 Values
   ('TRAM_8', 'AUTH_WEB_USER');
Insert into SGMAFCONECTOR_AUTENTICACION
   (TRAMITEID, CONECTORID)
 Values
   ('TRAM_9', 'AUTH_CERTIFICADO');
Insert into SGMAFCONECTOR_AUTENTICACION
   (TRAMITEID, CONECTORID)
 Values
   ('TRAM_9', 'AUTH_WEB_USER');
COMMIT;

SET DEFINE OFF;
Insert into SGMNTINFO_ESTADO_NOTI
   (CGUID, CUIDSISNOT, CDESCRIPCION)
 Values
   (-2, 'Notificacion con errores', 'Error devuelto por conector');
Insert into SGMNTINFO_ESTADO_NOTI
   (CGUID, CUIDSISNOT, CDESCRIPCION)
 Values
   (-1, 'Fallo', 'Fallo');
Insert into SGMNTINFO_ESTADO_NOTI
   (CGUID, CUIDSISNOT, CDESCRIPCION)
 Values
   (0, 'Notificación creada', 'Notificación creada');
Insert into SGMNTINFO_ESTADO_NOTI
   (CGUID, CUIDSISNOT, CDESCRIPCION)
 Values
   (1, 'Notificación enviada', 'Notificación enviada');
Insert into SGMNTINFO_ESTADO_NOTI
   (CGUID, CUIDSISNOT, CDESCRIPCION)
 Values
   (2, 'Notificación puesta a disposición', 'Notificación puesta a disposición');
Insert into SGMNTINFO_ESTADO_NOTI
   (CGUID, CUIDSISNOT, CDESCRIPCION)
 Values
   (3, 'Notificacion leída', 'Notificacion leída');
Insert into SGMNTINFO_ESTADO_NOTI
   (CGUID, CUIDSISNOT, CDESCRIPCION)
 Values
   (4, 'Notificación expirada después de 10 días', 'Notificación expirada después de 10 días');
Insert into SGMNTINFO_ESTADO_NOTI
   (CGUID, CUIDSISNOT, CDESCRIPCION)
 Values
   (5, 'Notificación rechazada', 'Notificación rechazada');
Insert into SGMNTINFO_ESTADO_NOTI
   (CGUID, CUIDSISNOT, CDESCRIPCION)
 Values
   (6, 'Notificación finalizada (Cambio de estado comunicado al sistema emisor)', 'Notificación finalizada (Cambio de estado comunicado al sistema emisor)');
Insert into SGMNTINFO_ESTADO_NOTI
   (CGUID, CUIDSISNOT, CDESCRIPCION)
 Values
   (7, 'Usuario no suscrito', 'Usuario no suscrito');
COMMIT;

SET DEFINE OFF;
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('3dm', 'x-world/x-3dmf');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('3dmf', 'x-world/x-3dmf');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('a', 'application/octet-stream');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('aab', 'application/x-authorware-bin');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('aam', 'application/x-authorware-map');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('aas', 'application/x-authorware-seg');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('abc', 'text/vnd.abc');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('acgi', 'text/html');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('afl', 'video/animaflex');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('ai', 'application/postscript');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('aif', 'audio/aiff');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('aifc', 'audio/aiff');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('aiff', 'audio/aiff');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('aim', 'application/x-aim');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('aip', 'text/x-audiosoft-intra');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('ani', 'application/x-navi-animation');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('aos', 'application/x-nokia-9000-communicator-add-on-software');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('aps', 'application/mime');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('arc', 'application/octet-stream');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('arj', 'application/arj');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('art', 'image/x-jg');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('asf', 'video/x-ms-asf');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('asm', 'text/x-asm');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('asp', 'text/asp');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('asx', 'video/x-ms-asf');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('au', 'audio/basic');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('avi', 'video/avi');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('avs', 'video/avs-video');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('bcpio', 'application/x-bcpio');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('bin', 'application/octet-stream');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('bm', 'image/bmp');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('bmp', 'image/bmp');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('boo', 'application/book');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('book', 'application/book');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('boz', 'application/x-bzip2');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('bsh', 'application/x-bsh');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('bz', 'application/x-bzip');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('bz2', 'application/x-bzip2');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('c', 'text/plain');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('c++', 'text/plain');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('cat', 'application/vnd.ms-pki.seccat');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('cc', 'text/plain');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('ccad', 'application/clariscad');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('cco', 'application/x-cocoa');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('cdf', 'application/cdf');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('cer', 'application/x-x509-ca-cert');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('cha', 'application/x-chat');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('chat', 'application/x-chat');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('class', 'application/java');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('com', 'application/octet-stream');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('conf', 'text/plain');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('cpio', 'application/x-cpio');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('cpp', 'text/x-c');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('cpt', 'application/x-compactpro');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('crl', 'application/pkcs-crl');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('crt', 'application/x-x509-user-cert');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('csh', 'application/x-csh');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('css', 'text/css');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('cxx', 'text/plain');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('dcr', 'application/x-director');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('deepv', 'application/x-deepv');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('def', 'text/plain');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('der', 'application/x-x509-ca-cert');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('dif', 'video/x-dv');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('dir', 'application/x-director');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('dl', 'video/dl');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('doc', 'application/msword');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('dot', 'application/msword');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('dp', 'application/commonground');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('drw', 'application/drafting');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('dump', 'application/octet-stream');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('dv', 'video/x-dv');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('dvi', 'application/x-dvi');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('dwf', 'model/vnd.dwf');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('dwg', 'image/vnd.dwg');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('dxf', 'application/dxf');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('dxr', 'application/x-director');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('el', 'text/x-script.elisp');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('elc', 'application/x-elc');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('env', 'application/x-envoy');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('eps', 'application/postscript');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('es', 'application/x-esrehber');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('etx', 'text/x-setext');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('evy', 'application/envoy');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('exe', 'application/octet-stream');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('f', 'text/plain');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('f77', 'text/x-fortran');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('f90', 'text/plain');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('fdf', 'application/vnd.fdf');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('fif', 'application/fractals');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('fli', 'video/fli');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('flo', 'image/florian');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('flx', 'text/vnd.fmi.flexstor');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('fmf', 'video/x-atomic3d-feature');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('for', 'text/plain');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('fpx', 'image/vnd.fpx');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('frl', 'application/freeloader');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('funk', 'audio/make');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('g', 'text/plain');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('g3', 'image/g3fax');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('gif', 'image/gif');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('gl', 'video/gl');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('gsd', 'audio/x-gsm');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('gsm', 'audio/x-gsm');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('gsp', 'application/x-gsp');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('gss', 'application/x-gss');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('gtar', 'application/x-gtar');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('gz', 'application/x-compressed');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('gzip', 'application/x-gzip');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('h', 'text/plain');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('hdf', 'application/x-hdf');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('help', 'application/x-helpfile');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('hgl', 'application/vnd.hp-hpgl');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('hh', 'text/plain');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('hlb', 'text/x-script');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('hlp', 'application/hlp');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('hpg', 'application/vnd.hp-hpgl');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('hpgl', 'application/vnd.hp-hpgl');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('hqx', 'application/binhex');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('hta', 'application/hta');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('htc', 'text/x-component');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('htm', 'text/html');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('html', 'text/html');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('htmls', 'text/html');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('htt', 'text/webviewhtml');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('htx', 'text/html');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('ice', 'x-conference/x-cooltalk');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('ico', 'image/x-icon');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('idc', 'text/plain');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('ief', 'image/ief');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('iefs', 'image/ief');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('iges', 'application/iges');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('igs', 'application/iges');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('ima', 'application/x-ima');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('imap', 'application/x-httpd-imap');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('inf', 'application/inf');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('ins', 'application/x-internett-signup');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('ip', 'application/x-ip2');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('isu', 'video/x-isvideo');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('it', 'audio/it');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('iv', 'application/x-inventor');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('ivr', 'i-world/i-vrml');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('ivy', 'application/x-livescreen');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('jam', 'audio/x-jam');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('jav', 'text/plain');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('java', 'text/plain');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('jcm', 'application/x-java-commerce');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('jfif', 'image/jpeg');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('jfif-tbn', 'image/jpeg');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('jpe', 'image/jpeg');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('jpeg', 'image/jpeg');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('jpg', 'image/jpeg');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('jps', 'image/x-jps');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('js', 'application/x-javascript');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('jut', 'image/jutvision');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('kar', 'audio/midi');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('ksh', 'application/x-ksh');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('la', 'audio/nspaudio');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('lam', 'audio/x-liveaudio');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('latex', 'application/x-latex');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('lha', 'application/lha');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('lhx', 'application/octet-stream');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('list', 'text/plain');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('lma', 'audio/nspaudio');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('log', 'text/plain');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('lsp', 'application/x-lisp');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('lst', 'text/plain');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('lsx', 'text/x-la-asf');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('ltx', 'application/x-latex');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('lzh', 'application/octet-stream');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('lzx', 'application/lzx');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('m', 'text/plain');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('m1v', 'video/mpeg');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('m2a', 'audio/mpeg');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('m2v', 'video/mpeg');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('m3u', 'audio/x-mpequrl');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('man', 'application/x-troff-man');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('map', 'application/x-navimap');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('mar', 'text/plain');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('mbd', 'application/mbedlet');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('mc$', 'application/x-magic-cap-package-1.0');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('mcd', 'application/mcad');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('mcf', 'image/vasa');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('mcp', 'application/netmc');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('me', 'application/x-troff-me');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('mht', 'message/rfc822');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('mhtml', 'message/rfc822');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('mid', 'audio/midi');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('midi', 'audio/midi');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('mif', 'application/x-frame');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('mime', 'message/rfc822');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('mjf', 'audio/x-vnd.audioexplosion.mjuicemediafile');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('mjpg', 'video/x-motion-jpeg');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('mm', 'application/base64');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('mme', 'application/base64');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('mod', 'audio/mod');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('moov', 'video/quicktime');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('mov', 'video/quicktime');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('movie', 'video/x-sgi-movie');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('mp2', 'audio/mpeg');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('mp3', 'audio/mpeg3');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('mpa', 'audio/mpeg');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('mpc', 'application/x-project');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('mpe', 'video/mpeg');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('mpeg', 'video/mpeg');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('mpg', 'video/mpeg');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('mpga', 'audio/mpeg');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('mpp', 'application/vnd.ms-project');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('mpt', 'application/x-project');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('mpv', 'application/x-project');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('mpx', 'application/x-project');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('mrc', 'application/marc');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('ms', 'application/x-troff-ms');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('mv', 'video/x-sgi-movie');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('my', 'audio/make');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('mzz', 'application/x-vnd.audioexplosion.mzz');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('nap', 'image/naplps');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('naplps', 'image/naplps');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('nc', 'application/x-netcdf');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('ncm', 'application/vnd.nokia.configuration-message');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('nif', 'image/x-niff');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('niff', 'image/x-niff');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('nix', 'application/x-mix-transfer');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('nsc', 'application/x-conference');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('nvd', 'application/x-navidoc');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('o', 'application/octet-stream');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('oda', 'application/oda');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('omc', 'application/x-omc');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('omcd', 'application/x-omcdatamaker');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('omcr', 'application/x-omcregerator');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('p', 'text/x-pascal');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('p10', 'application/pkcs10');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('p12', 'application/pkcs-12');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('p7a', 'application/x-pkcs7-signature');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('p7c', 'application/pkcs7-mime');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('p7m', 'application/pkcs7-mime');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('p7r', 'application/x-pkcs7-certreqresp');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('p7s', 'application/pkcs7-signature');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('part', 'application/pro_eng');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('pas', 'text/pascal');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('pbm', 'image/x-portable-bitmap');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('pcl', 'application/x-pcl');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('pct', 'image/x-pict');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('pcx', 'image/x-pcx');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('pdb', 'chemical/x-pdb');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('pdf', 'application/pdf');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('pfunk', 'audio/make');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('pgm', 'image/x-portable-graymap');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('pic', 'image/pict');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('pict', 'image/pict');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('pkg', 'application/x-newton-compatible-pkg');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('pko', 'application/vnd.ms-pki.pko');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('pl', 'text/plain');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('plx', 'application/x-pixclscript');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('pm', 'image/x-xpixmap');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('pm4', 'application/x-pagemaker');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('pm5', 'application/x-pagemaker');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('png', 'image/png');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('pnm', 'application/x-portable-anymap');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('pot', 'application/mspowerpoint');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('pov', 'model/x-pov');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('ppa', 'application/vnd.ms-powerpoint');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('ppm', 'image/x-portable-pixmap');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('pps', 'application/mspowerpoint');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('ppt', 'application/mspowerpoint');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('ppz', 'application/mspowerpoint');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('pre', 'application/x-freelance');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('prt', 'application/pro_eng');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('ps', 'application/postscript');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('psd', 'application/octet-stream');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('pvu', 'paleovu/x-pv');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('pwz', 'application/vnd.ms-powerpoint');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('py', 'text/x-script.phyton');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('pyc', 'applicaiton/x-bytecode.python');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('qcp', 'audio/vnd.qcelp');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('qd3', 'x-world/x-3dmf');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('qd3d', 'x-world/x-3dmf');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('qif', 'image/x-quicktime');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('qt', 'video/quicktime');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('qtc', 'video/x-qtc');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('qti', 'image/x-quicktime');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('qtif', 'image/x-quicktime');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('ra', 'audio/x-pn-realaudio');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('ram', 'audio/x-pn-realaudio');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('ras', 'application/x-cmu-raster');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('rast', 'image/cmu-raster');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('rexx', 'text/x-script.rexx');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('rf', 'image/vnd.rn-realflash');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('rgb', 'image/x-rgb');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('rm', 'audio/x-pn-realaudio');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('rmi', 'audio/mid');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('rmm', 'audio/x-pn-realaudio');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('rmp', 'audio/x-pn-realaudio');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('rng', 'application/ringing-tones');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('rnx', 'application/vnd.rn-realplayer');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('roff', 'application/x-troff');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('rp', 'image/vnd.rn-realpix');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('rpm', 'audio/x-pn-realaudio-plugin');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('rt', 'text/richtext');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('rtf', 'application/rtf');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('rtx', 'application/rtf');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('rv', 'video/vnd.rn-realvideo');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('s', 'text/x-asm');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('s3m', 'audio/s3m');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('saveme', 'application/octet-stream');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('sbk', 'application/x-tbook');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('scm', 'application/x-lotusscreencam');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('sdml', 'text/plain');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('sdp', 'application/sdp');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('sdp', 'application/x-sdp');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('sdr', 'application/sounder');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('sea', 'application/sea');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('set', 'application/set');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('sgm', 'text/sgml');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('sgml', 'text/sgml');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('sh', 'application/x-bsh');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('shar', 'application/x-bsh');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('shtml', 'text/html');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('sid', 'audio/x-psid');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('sit', 'application/x-sit');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('skd', 'application/x-koan');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('skm', 'application/x-koan');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('skp', 'application/x-koan');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('skt', 'application/x-koan');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('sl', 'application/x-seelogo');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('smi', 'application/smil');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('smil', 'application/smil');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('snd', 'audio/basic');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('sol', 'application/solids');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('spc', 'application/x-pkcs7-certificates');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('spl', 'application/futuresplash');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('spr', 'application/x-sprite');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('sprite', 'application/x-sprite');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('src', 'application/x-wais-source');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('ssi', 'text/x-server-parsed-html');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('ssm', 'application/streamingmedia');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('sst', 'application/vnd.ms-pki.certstore');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('step', 'application/step');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('stl', 'application/sla');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('stp', 'application/step');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('sv4cpio', 'application/x-sv4cpio');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('sv4crc', 'application/x-sv4crc');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('svf', 'image/x-dwg');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('svr', 'application/x-world');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('swf', 'application/x-shockwave-flash');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('t', 'application/x-troff');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('talk', 'text/x-speech');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('tar', 'application/x-tar');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('tbk', 'application/toolbook');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('tcl', 'application/x-tcl');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('tcsh', 'text/x-script.tcsh');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('tex', 'application/x-tex');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('texi', 'application/x-texinfo');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('texinfo', 'application/x-texinfo');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('text', 'application/plain');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('tgz', 'application/gnutar');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('tif', 'image/tiff');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('tiff', 'image/tiff');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('tr', 'application/x-troff');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('tsi', 'audio/tsp-audio');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('tsp', 'application/dsptype');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('tsv', 'text/tab-separated-values');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('turbot', 'image/florian');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('txt', 'text/plain');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('uil', 'text/x-uil');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('uni', 'text/uri-list');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('unis', 'text/uri-list');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('unv', 'application/i-deas');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('uri', 'text/uri-list');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('uris', 'text/uri-list');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('ustar', 'application/x-ustar');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('uu', 'application/octet-stream');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('uue', 'text/x-uuencode');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('vcd', 'application/x-cdlink');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('vcs', 'text/x-vcalendar');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('vda', 'application/vda');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('vdo', 'video/vdo');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('vew', 'application/groupwise');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('viv', 'video/vivo');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('vivo', 'video/vivo');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('vmd', 'application/vocaltec-media-desc');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('vmf', 'application/vocaltec-media-file');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('voc', 'audio/voc');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('vos', 'video/vosaic');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('vox', 'audio/voxware');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('vqe', 'audio/x-twinvq-plugin');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('vqf', 'audio/x-twinvq');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('vql', 'audio/x-twinvq-plugin');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('vrml', 'application/x-vrml');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('vrt', 'x-world/x-vrt');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('vsd', 'application/x-visio');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('vst', 'application/x-visio');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('vsw', 'application/x-visio');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('w60', 'application/wordperfect6.0');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('w61', 'application/wordperfect6.1');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('w6w', 'application/msword');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('wav', 'audio/wav');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('wb1', 'application/x-qpro');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('wbmp', 'image/vnd.wap.wbmp');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('web', 'application/vnd.xara');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('wiz', 'application/msword');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('wk1', 'application/x-123');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('wmf', 'windows/metafile');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('wml', 'text/vnd.wap.wml');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('wmlc', 'application/vnd.wap.wmlc');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('wmls', 'text/vnd.wap.wmlscript');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('wmlsc', 'application/vnd.wap.wmlscriptc');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('word', 'application/msword');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('wp', 'application/wordperfect');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('wp5', 'application/wordperfect');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('wp6', 'application/wordperfect');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('wpd', 'application/wordperfect');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('wq1', 'application/x-lotus');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('wri', 'application/mswrite');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('wrl', 'application/x-world');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('wrz', 'model/vrml');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('wsc', 'text/scriplet');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('wsrc', 'application/x-wais-source');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('wtk', 'application/x-wintalk');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('xbm', 'image/x-xbitmap');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('xdr', 'video/x-amt-demorun');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('xgz', 'xgl/drawing');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('xif', 'image/vnd.xiff');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('xl', 'application/excel');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('xla', 'application/excel');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('xlb', 'application/excel');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('xlc', 'application/excel');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('xld', 'application/excel');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('xlk', 'application/excel');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('xll', 'application/excel');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('xlm', 'application/excel');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('xls', 'application/excel');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('xlt', 'application/excel');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('xlv', 'application/excel');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('xlw', 'application/excel');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('xm', 'audio/xm');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('xml', 'text/xml');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('xmz', 'xgl/movie');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('xpix', 'application/x-vnd.ls-xpix');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('xpm', 'image/x-xpixmap');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('x-png', 'image/png');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('xsr', 'video/x-amt-showrun');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('xwd', 'image/x-xwd');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('xyz', 'chemical/x-pdb');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('z', 'application/x-compress');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('zip', 'application/x-compressed');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('zoo', 'application/octet-stream');
Insert into SGMRDETIPOSMIME
   (EXTENSION, TIPOMIME)
 Values
   ('zsh', 'text/x-script.zsh');
COMMIT;

SET DEFINE OFF;
Insert into SGMRTCATALOGO_CONECTORES
   (CONECTORID, DESCRIPCION, TIPO)
 Values
   ('AUTH_CERTIFICADO', 'Autenticación con certificado digital', '1');
Insert into SGMRTCATALOGO_CONECTORES
   (CONECTORID, DESCRIPCION, TIPO)
 Values
   ('AUTH_WEB_USER', 'Autenticación con usuario Web de Nivel 2', '2');
COMMIT;

SET DEFINE OFF;
Insert into SGMRTCATALOGO_DOCSTRAMITE
   (TRAMITE_ID, DOCUMENTO_ID, CODIGO_DOCUMENTO, DOCUMENTO_OBLIGATORIO)
 Values
   ('TRAM_1', 'PDF', 'TRAM1D1', '1');
Insert into SGMRTCATALOGO_DOCSTRAMITE
   (TRAMITE_ID, DOCUMENTO_ID, CODIGO_DOCUMENTO, DOCUMENTO_OBLIGATORIO)
 Values
   ('TRAM_1', 'DOC', 'TRAM1D2', '0');
Insert into SGMRTCATALOGO_DOCSTRAMITE
   (TRAMITE_ID, DOCUMENTO_ID, CODIGO_DOCUMENTO, DOCUMENTO_OBLIGATORIO)
 Values
   ('TRAM_3', 'PDF', 'TRAM3D1', '1');
Insert into SGMRTCATALOGO_DOCSTRAMITE
   (TRAMITE_ID, DOCUMENTO_ID, CODIGO_DOCUMENTO, DOCUMENTO_OBLIGATORIO)
 Values
   ('TRAM_4', 'PDF', 'TRAM4D1', '1');
Insert into SGMRTCATALOGO_DOCSTRAMITE
   (TRAMITE_ID, DOCUMENTO_ID, CODIGO_DOCUMENTO, DOCUMENTO_OBLIGATORIO)
 Values
   ('TRAM_13', 'PDF', 'TRAM13D1', '1');
Insert into SGMRTCATALOGO_DOCSTRAMITE
   (TRAMITE_ID, DOCUMENTO_ID, CODIGO_DOCUMENTO, DOCUMENTO_OBLIGATORIO)
 Values
   ('TRAM_13', 'PDF', 'TRAM13D2', '1');
Insert into SGMRTCATALOGO_DOCSTRAMITE
   (TRAMITE_ID, DOCUMENTO_ID, CODIGO_DOCUMENTO, DOCUMENTO_OBLIGATORIO)
 Values
   ('TRAM_13', 'PDF', 'TRAM13D3', '1');
Insert into SGMRTCATALOGO_DOCSTRAMITE
   (TRAMITE_ID, DOCUMENTO_ID, CODIGO_DOCUMENTO, DOCUMENTO_OBLIGATORIO)
 Values
   ('TRAM_13', 'PDF', 'TRAM13D4', '1');
Insert into SGMRTCATALOGO_DOCSTRAMITE
   (TRAMITE_ID, DOCUMENTO_ID, CODIGO_DOCUMENTO, DOCUMENTO_OBLIGATORIO)
 Values
   ('TRAM_13', 'PDF', 'TRAM13D5', '1');
Insert into SGMRTCATALOGO_DOCSTRAMITE
   (TRAMITE_ID, DOCUMENTO_ID, CODIGO_DOCUMENTO, DOCUMENTO_OBLIGATORIO)
 Values
   ('TRAM_13', 'PDF', 'TRAM13D6', '0');
Insert into SGMRTCATALOGO_DOCSTRAMITE
   (TRAMITE_ID, DOCUMENTO_ID, CODIGO_DOCUMENTO, DOCUMENTO_OBLIGATORIO)
 Values
   ('TRAM_12', 'PDF', 'TRAM12D1', '1');
Insert into SGMRTCATALOGO_DOCSTRAMITE
   (TRAMITE_ID, DOCUMENTO_ID, CODIGO_DOCUMENTO, DOCUMENTO_OBLIGATORIO)
 Values
   ('TRAM_12', 'PDF', 'TRAM12D2', '1');
Insert into SGMRTCATALOGO_DOCSTRAMITE
   (TRAMITE_ID, DOCUMENTO_ID, CODIGO_DOCUMENTO, DOCUMENTO_OBLIGATORIO)
 Values
   ('TRAM_12', 'PDF', 'TRAM12D3', '1');
Insert into SGMRTCATALOGO_DOCSTRAMITE
   (TRAMITE_ID, DOCUMENTO_ID, CODIGO_DOCUMENTO, DOCUMENTO_OBLIGATORIO)
 Values
   ('TRAM_12', 'PDF', 'TRAM12D4', '1');
Insert into SGMRTCATALOGO_DOCSTRAMITE
   (TRAMITE_ID, DOCUMENTO_ID, CODIGO_DOCUMENTO, DOCUMENTO_OBLIGATORIO)
 Values
   ('TRAM_12', 'PDF', 'TRAM12D5', '0');
Insert into SGMRTCATALOGO_DOCSTRAMITE
   (TRAMITE_ID, DOCUMENTO_ID, CODIGO_DOCUMENTO, DOCUMENTO_OBLIGATORIO)
 Values
   ('TRAM_12', 'PDF', 'TRAM12D6', '0');
Insert into SGMRTCATALOGO_DOCSTRAMITE
   (TRAMITE_ID, DOCUMENTO_ID, CODIGO_DOCUMENTO, DOCUMENTO_OBLIGATORIO)
 Values
   ('TRAM_15', 'PDF', 'TRAM15D1', '1');
Insert into SGMRTCATALOGO_DOCSTRAMITE
   (TRAMITE_ID, DOCUMENTO_ID, CODIGO_DOCUMENTO, DOCUMENTO_OBLIGATORIO)
 Values
   ('TRAM_15', 'PDF', 'TRAM15D2', '0');
Insert into SGMRTCATALOGO_DOCSTRAMITE
   (TRAMITE_ID, DOCUMENTO_ID, CODIGO_DOCUMENTO, DOCUMENTO_OBLIGATORIO)
 Values
   ('TRAM_9', 'PDF', 'TRAM9D2', '1');
Insert into SGMRTCATALOGO_DOCSTRAMITE
   (TRAMITE_ID, DOCUMENTO_ID, CODIGO_DOCUMENTO, DOCUMENTO_OBLIGATORIO)
 Values
   ('TRAM_9', 'PDF', 'TRAM9D3', '1');
Insert into SGMRTCATALOGO_DOCSTRAMITE
   (TRAMITE_ID, DOCUMENTO_ID, CODIGO_DOCUMENTO, DOCUMENTO_OBLIGATORIO)
 Values
   ('TRAM_9', 'PDF', 'TRAM9D4', '1');
Insert into SGMRTCATALOGO_DOCSTRAMITE
   (TRAMITE_ID, DOCUMENTO_ID, CODIGO_DOCUMENTO, DOCUMENTO_OBLIGATORIO)
 Values
   ('TRAM_9', 'PDF', 'TRAM9D1', '0');
Insert into SGMRTCATALOGO_DOCSTRAMITE
   (TRAMITE_ID, DOCUMENTO_ID, CODIGO_DOCUMENTO, DOCUMENTO_OBLIGATORIO)
 Values
   ('TRAM_11', 'PDF', 'TRAM11D2', '0');
Insert into SGMRTCATALOGO_DOCSTRAMITE
   (TRAMITE_ID, DOCUMENTO_ID, CODIGO_DOCUMENTO, DOCUMENTO_OBLIGATORIO)
 Values
   ('TRAM_11', 'PDF', 'TRAM11D1', '0');
Insert into SGMRTCATALOGO_DOCSTRAMITE
   (TRAMITE_ID, DOCUMENTO_ID, CODIGO_DOCUMENTO, DOCUMENTO_OBLIGATORIO)
 Values
   ('TRAM_11', 'PDF', 'TRAM11D3', '0');
Insert into SGMRTCATALOGO_DOCSTRAMITE
   (TRAMITE_ID, DOCUMENTO_ID, CODIGO_DOCUMENTO, DOCUMENTO_OBLIGATORIO)
 Values
   ('TRAM_11', 'PDF', 'TRAM11D4', '0');
Insert into SGMRTCATALOGO_DOCSTRAMITE
   (TRAMITE_ID, DOCUMENTO_ID, CODIGO_DOCUMENTO, DOCUMENTO_OBLIGATORIO)
 Values
   ('TRAM_11', 'PDF', 'TRAM11D5', '0');
Insert into SGMRTCATALOGO_DOCSTRAMITE
   (TRAMITE_ID, DOCUMENTO_ID, CODIGO_DOCUMENTO, DOCUMENTO_OBLIGATORIO)
 Values
   ('TRAM_11', 'PDF', 'TRAM11D6', '0');
Insert into SGMRTCATALOGO_DOCSTRAMITE
   (TRAMITE_ID, DOCUMENTO_ID, CODIGO_DOCUMENTO, DOCUMENTO_OBLIGATORIO)
 Values
   ('TRAM_8', 'PDF', 'TRAM8D1', '0');
Insert into SGMRTCATALOGO_DOCSTRAMITE
   (TRAMITE_ID, DOCUMENTO_ID, CODIGO_DOCUMENTO, DOCUMENTO_OBLIGATORIO)
 Values
   ('TRAM_8', 'PDF', 'TRAM8D2', '0');
Insert into SGMRTCATALOGO_DOCSTRAMITE
   (TRAMITE_ID, DOCUMENTO_ID, CODIGO_DOCUMENTO, DOCUMENTO_OBLIGATORIO)
 Values
   ('TRAM_8', 'PDF', 'TRAM8D3', '0');
Insert into SGMRTCATALOGO_DOCSTRAMITE
   (TRAMITE_ID, DOCUMENTO_ID, CODIGO_DOCUMENTO, DOCUMENTO_OBLIGATORIO)
 Values
   ('TRAM_8', 'PDF', 'TRAM8D4', '0');
Insert into SGMRTCATALOGO_DOCSTRAMITE
   (TRAMITE_ID, DOCUMENTO_ID, CODIGO_DOCUMENTO, DOCUMENTO_OBLIGATORIO)
 Values
   ('TRAM_16', 'PDF', 'TRAM16D1', '0');
Insert into SGMRTCATALOGO_DOCSTRAMITE
   (TRAMITE_ID, DOCUMENTO_ID, CODIGO_DOCUMENTO, DOCUMENTO_OBLIGATORIO)
 Values
   ('TRAM_16', 'PDF', 'TRAM16D2', '0');
Insert into SGMRTCATALOGO_DOCSTRAMITE
   (TRAMITE_ID, DOCUMENTO_ID, CODIGO_DOCUMENTO, DOCUMENTO_OBLIGATORIO)
 Values
   ('TRAM_16', 'PDF', 'TRAM16D3', '0');
Insert into SGMRTCATALOGO_DOCSTRAMITE
   (TRAMITE_ID, DOCUMENTO_ID, CODIGO_DOCUMENTO, DOCUMENTO_OBLIGATORIO)
 Values
   ('TRAM_16', 'PDF', 'TRAM16D4', '0');
Insert into SGMRTCATALOGO_DOCSTRAMITE
   (TRAMITE_ID, DOCUMENTO_ID, CODIGO_DOCUMENTO, DOCUMENTO_OBLIGATORIO)
 Values
   ('TRAM_16', 'PDF', 'TRAM16D5', '0');
Insert into SGMRTCATALOGO_DOCSTRAMITE
   (TRAMITE_ID, DOCUMENTO_ID, CODIGO_DOCUMENTO, DOCUMENTO_OBLIGATORIO)
 Values
   ('TRAM_16', 'PDF', 'TRAM16D6', '0');
Insert into SGMRTCATALOGO_DOCSTRAMITE
   (TRAMITE_ID, DOCUMENTO_ID, CODIGO_DOCUMENTO, DOCUMENTO_OBLIGATORIO)
 Values
   ('TRAM_16', 'PDF', 'TRAM16D7', '0');
Insert into SGMRTCATALOGO_DOCSTRAMITE
   (TRAMITE_ID, DOCUMENTO_ID, CODIGO_DOCUMENTO, DOCUMENTO_OBLIGATORIO)
 Values
   ('TRAM_16', 'PDF', 'TRAM16D8', '0');
Insert into SGMRTCATALOGO_DOCSTRAMITE
   (TRAMITE_ID, DOCUMENTO_ID, CODIGO_DOCUMENTO, DOCUMENTO_OBLIGATORIO)
 Values
   ('TRAM_16', 'PDF', 'TRAM16D9', '0');
Insert into SGMRTCATALOGO_DOCSTRAMITE
   (TRAMITE_ID, DOCUMENTO_ID, CODIGO_DOCUMENTO, DOCUMENTO_OBLIGATORIO)
 Values
   ('TRAM_16', 'PDF', 'TRAM16D10', '0');
Insert into SGMRTCATALOGO_DOCSTRAMITE
   (TRAMITE_ID, DOCUMENTO_ID, CODIGO_DOCUMENTO, DOCUMENTO_OBLIGATORIO)
 Values
   ('TRAM_16', 'PDF', 'TRAM16D11', '0');
Insert into SGMRTCATALOGO_DOCSTRAMITE
   (TRAMITE_ID, DOCUMENTO_ID, CODIGO_DOCUMENTO, DOCUMENTO_OBLIGATORIO)
 Values
   ('TRAM_16', 'PDF', 'TRAM16D12', '0');
Insert into SGMRTCATALOGO_DOCSTRAMITE
   (TRAMITE_ID, DOCUMENTO_ID, CODIGO_DOCUMENTO, DOCUMENTO_OBLIGATORIO)
 Values
   ('TRAM_10', 'PDF', 'TRAM10D2', '1');
Insert into SGMRTCATALOGO_DOCSTRAMITE
   (TRAMITE_ID, DOCUMENTO_ID, CODIGO_DOCUMENTO, DOCUMENTO_OBLIGATORIO)
 Values
   ('TRAM_10', 'PDF', 'TRAM10D3', '1');
Insert into SGMRTCATALOGO_DOCSTRAMITE
   (TRAMITE_ID, DOCUMENTO_ID, CODIGO_DOCUMENTO, DOCUMENTO_OBLIGATORIO)
 Values
   ('TRAM_10', 'PDF', 'TRAM10D4', '1');
Insert into SGMRTCATALOGO_DOCSTRAMITE
   (TRAMITE_ID, DOCUMENTO_ID, CODIGO_DOCUMENTO, DOCUMENTO_OBLIGATORIO)
 Values
   ('TRAM_10', 'PDF', 'TRAM10D5', '1');
Insert into SGMRTCATALOGO_DOCSTRAMITE
   (TRAMITE_ID, DOCUMENTO_ID, CODIGO_DOCUMENTO, DOCUMENTO_OBLIGATORIO)
 Values
   ('TRAM_10', 'PDF', 'TRAM10D6', '0');
Insert into SGMRTCATALOGO_DOCSTRAMITE
   (TRAMITE_ID, DOCUMENTO_ID, CODIGO_DOCUMENTO, DOCUMENTO_OBLIGATORIO)
 Values
   ('TRAM_10', 'PDF', 'TRAM10D1', '1');
Insert into SGMRTCATALOGO_DOCSTRAMITE
   (TRAMITE_ID, DOCUMENTO_ID, CODIGO_DOCUMENTO, DOCUMENTO_OBLIGATORIO)
 Values
   ('TRAM_14', 'PDF', 'TRAM14D1', '1');
Insert into SGMRTCATALOGO_DOCSTRAMITE
   (TRAMITE_ID, DOCUMENTO_ID, CODIGO_DOCUMENTO, DOCUMENTO_OBLIGATORIO)
 Values
   ('TRAM_14', 'PDF', 'TRAM14D2', '0');
Insert into SGMRTCATALOGO_DOCSTRAMITE
   (TRAMITE_ID, DOCUMENTO_ID, CODIGO_DOCUMENTO, DOCUMENTO_OBLIGATORIO)
 Values
   ('TRAM_14', 'PDF', 'TRAM14D3', '1');
Insert into SGMRTCATALOGO_DOCSTRAMITE
   (TRAMITE_ID, DOCUMENTO_ID, CODIGO_DOCUMENTO, DOCUMENTO_OBLIGATORIO)
 Values
   ('TRAM_14', 'PDF', 'TRAM14D4', '1');
Insert into SGMRTCATALOGO_DOCSTRAMITE
   (TRAMITE_ID, DOCUMENTO_ID, CODIGO_DOCUMENTO, DOCUMENTO_OBLIGATORIO)
 Values
   ('TRAM_14', 'PDF', 'TRAM14D6', '1');
Insert into SGMRTCATALOGO_DOCSTRAMITE
   (TRAMITE_ID, DOCUMENTO_ID, CODIGO_DOCUMENTO, DOCUMENTO_OBLIGATORIO)
 Values
   ('TRAM_14', 'PDF', 'TRAM14D7', '0');
Insert into SGMRTCATALOGO_DOCSTRAMITE
   (TRAMITE_ID, DOCUMENTO_ID, CODIGO_DOCUMENTO, DOCUMENTO_OBLIGATORIO)
 Values
   ('TRAM_14', 'PDF', 'TRAM14D8', '1');
Insert into SGMRTCATALOGO_DOCSTRAMITE
   (TRAMITE_ID, DOCUMENTO_ID, CODIGO_DOCUMENTO, DOCUMENTO_OBLIGATORIO)
 Values
   ('TRAM_14', 'PDF', 'TRAM14D9', '1');
Insert into SGMRTCATALOGO_DOCSTRAMITE
   (TRAMITE_ID, DOCUMENTO_ID, CODIGO_DOCUMENTO, DOCUMENTO_OBLIGATORIO)
 Values
   ('TRAM_14', 'PDF', 'TRAM14D10', '0');
Insert into SGMRTCATALOGO_DOCSTRAMITE
   (TRAMITE_ID, DOCUMENTO_ID, CODIGO_DOCUMENTO, DOCUMENTO_OBLIGATORIO)
 Values
   ('TRAM_14', 'PDF', 'TRAM14D11', '0');
Insert into SGMRTCATALOGO_DOCSTRAMITE
   (TRAMITE_ID, DOCUMENTO_ID, CODIGO_DOCUMENTO, DOCUMENTO_OBLIGATORIO)
 Values
   ('TRAM_14', 'JPEG', 'TRAM14D5', '1');
Insert into SGMRTCATALOGO_DOCSTRAMITE
   (TRAMITE_ID, DOCUMENTO_ID, CODIGO_DOCUMENTO, DOCUMENTO_OBLIGATORIO)
 Values
   ('TRAM_5', 'PDF', 'TRAM5D1', '1');
Insert into SGMRTCATALOGO_DOCSTRAMITE
   (TRAMITE_ID, DOCUMENTO_ID, CODIGO_DOCUMENTO, DOCUMENTO_OBLIGATORIO)
 Values
   ('TRAM_6', 'PDF', 'TRAM6D1', '1');
Insert into SGMRTCATALOGO_DOCSTRAMITE
   (TRAMITE_ID, DOCUMENTO_ID, CODIGO_DOCUMENTO, DOCUMENTO_OBLIGATORIO)
 Values
   ('TRAM_17', 'PDF', 'TRAM17D1', '1');
Insert into SGMRTCATALOGO_DOCSTRAMITE
   (TRAMITE_ID, DOCUMENTO_ID, CODIGO_DOCUMENTO, DOCUMENTO_OBLIGATORIO)
 Values
   ('TRAM_18', 'PDF', 'TRAM18D1', '1');
Insert into SGMRTCATALOGO_DOCSTRAMITE
   (TRAMITE_ID, DOCUMENTO_ID, CODIGO_DOCUMENTO, DOCUMENTO_OBLIGATORIO)
 Values
   ('TRAM_18', 'DOC', 'TRAM18D2', '1');
COMMIT;

SET DEFINE OFF;
Insert into SGMRTCATALOGO_DOCUMENTOS
   (ID, DESCRIPCION, EXTENSION, CONECTOR_FIRMA, CONECTOR_CONTENIDO)
 Values
   ('PDF', 'Documento PDF', 'PDF', NULL, NULL);
Insert into SGMRTCATALOGO_DOCUMENTOS
   (ID, DESCRIPCION, EXTENSION, CONECTOR_FIRMA, CONECTOR_CONTENIDO)
 Values
   ('JPEG', 'Documento imagen con formato JPEG', 'JPG', NULL, NULL);
Insert into SGMRTCATALOGO_DOCUMENTOS
   (ID, DESCRIPCION, EXTENSION, CONECTOR_FIRMA, CONECTOR_CONTENIDO)
 Values
   ('DOC', 'Documento DOC', 'DOC', NULL, NULL);
Insert into SGMRTCATALOGO_DOCUMENTOS
   (ID, DESCRIPCION, EXTENSION, CONECTOR_FIRMA, CONECTOR_CONTENIDO)
 Values
   ('DOC_GIF', 'Fotografía', 'GIF', NULL, NULL);
COMMIT;

SET DEFINE OFF;
Insert into SGMRTCATALOGO_ORGANOS
   (ORGANO, DESCRIPCION)
 Values
   ('001', 'Servicio de Relaciones con el Ciudadano');
Insert into SGMRTCATALOGO_ORGANOS
   (ORGANO, DESCRIPCION)
 Values
   ('002', 'Servicio de Tramitación de Licencias');
Insert into SGMRTCATALOGO_ORGANOS
   (ORGANO, DESCRIPCION)
 Values
   ('003', 'Servicio de Secretaría');
COMMIT;

SET DEFINE OFF;
Insert into SGMRTCATALOGO_TIPOCONECTOR
   (TIPOID, DESCRIPCION)
 Values
   ('1', 'Autenticación con certificado digital');
Insert into SGMRTCATALOGO_TIPOCONECTOR
   (TIPOID, DESCRIPCION)
 Values
   ('2', 'Autenticación con usuario Web de nivel 2');
Insert into SGMRTCATALOGO_TIPOCONECTOR
   (TIPOID, DESCRIPCION)
 Values
   ('3', 'Creación de firma');
Insert into SGMRTCATALOGO_TIPOCONECTOR
   (TIPOID, DESCRIPCION)
 Values
   ('4', 'Verificación de firma');
Insert into SGMRTCATALOGO_TIPOCONECTOR
   (TIPOID, DESCRIPCION)
 Values
   ('5', 'Validación del contenido de un documento');
Insert into SGMRTCATALOGO_TIPOCONECTOR
   (TIPOID, DESCRIPCION)
 Values
   ('6', 'Obtención del certificado');
Insert into SGMRTCATALOGO_TIPOCONECTOR
   (TIPOID, DESCRIPCION)
 Values
   ('7', 'Autenticación con certificado digital vía web');
COMMIT;

SET DEFINE OFF;
Insert into SGMRTCATALOGO_TRAMITES
   (ID, ASUNTO, DESCRIPCION, ORGANO, LIMITE_DOCUMENTOS, 
    FIRMA, OFICINA, ID_PROCEDIMIENTO)
 Values
   ('TRAM_1', 'TRQS', 'Solicitud de Reclamación, queja y sugerencias', '001', '0', 
    '1', '999', 'PCD-3');
Insert into SGMRTCATALOGO_TRAMITES
   (ID, ASUNTO, DESCRIPCION, ORGANO, LIMITE_DOCUMENTOS, 
    FIRMA, OFICINA, ID_PROCEDIMIENTO)
 Values
   ('TRAM_3', 'TSUB', 'Subvención', '003', '0', 
    '1', '999', 'PCD-4');
Insert into SGMRTCATALOGO_TRAMITES
   (ID, ASUNTO, DESCRIPCION, ORGANO, LIMITE_DOCUMENTOS, 
    FIRMA, OFICINA, ID_PROCEDIMIENTO)
 Values
   ('TRAM_4', 'TLIC', 'Licencia de Obra Menor', '002', '0', 
    '1', '999', 'PCD-5');
Insert into SGMRTCATALOGO_TRAMITES
   (ID, ASUNTO, DESCRIPCION, ORGANO, LIMITE_DOCUMENTOS, 
    FIRMA, OFICINA, ID_PROCEDIMIENTO)
 Values
   ('TRAM_13', 'TLAANC', 'Licencia de Apertura de Actividad No Clasificada ', '003', '0', 
    '1', '999', '024');
Insert into SGMRTCATALOGO_TRAMITES
   (ID, ASUNTO, DESCRIPCION, ORGANO, LIMITE_DOCUMENTOS, 
    FIRMA, OFICINA, ID_PROCEDIMIENTO)
 Values
   ('TRAM_12', 'TSLV', 'Licencia de Vado', '003', '0', 
    '1', '999', '023');
Insert into SGMRTCATALOGO_TRAMITES
   (ID, ASUNTO, DESCRIPCION, ORGANO, LIMITE_DOCUMENTOS, 
    FIRMA, OFICINA, ID_PROCEDIMIENTO)
 Values
   ('TRAM_15', 'TSRT', 'Reclamación Tributos', '003', '0', 
    '1', '999', 'PCD-16');
Insert into SGMRTCATALOGO_TRAMITES
   (ID, ASUNTO, DESCRIPCION, ORGANO, LIMITE_DOCUMENTOS, 
    FIRMA, OFICINA, ID_PROCEDIMIENTO)
 Values
   ('TRAM_9', 'TSCU', 'Solicitud de Certificado Urbanístico', '003', '0', 
    '1', '999', 'PCD-11');
Insert into SGMRTCATALOGO_TRAMITES
   (ID, ASUNTO, DESCRIPCION, ORGANO, LIMITE_DOCUMENTOS, 
    FIRMA, OFICINA, ID_PROCEDIMIENTO)
 Values
   ('TRAM_11', 'TSAA', 'Solicitud de Acometida de Agua', '001', '0', 
    '1', '999', 'PCD-15');
Insert into SGMRTCATALOGO_TRAMITES
   (ID, ASUNTO, DESCRIPCION, ORGANO, LIMITE_DOCUMENTOS, 
    FIRMA, OFICINA, ID_PROCEDIMIENTO)
 Values
   ('TRAM_8', 'TCTLA', 'Cambio de Titularidad de Licencia de Apertura', '003', '0', 
    '1', '999', '025');
Insert into SGMRTCATALOGO_TRAMITES
   (ID, ASUNTO, DESCRIPCION, ORGANO, LIMITE_DOCUMENTOS, 
    FIRMA, OFICINA, ID_PROCEDIMIENTO)
 Values
   ('TRAM_16', 'TCAPN', 'Contrato Negociado', '003', '0', 
    '1', '999', 'PCD-10');
Insert into SGMRTCATALOGO_TRAMITES
   (ID, ASUNTO, DESCRIPCION, ORGANO, LIMITE_DOCUMENTOS, 
    FIRMA, OFICINA, ID_PROCEDIMIENTO)
 Values
   ('TRAM_7', 'TEXS', 'Expediente Sancionador', '003', '0', 
    '1', '999', '019');
Insert into SGMRTCATALOGO_TRAMITES
   (ID, ASUNTO, DESCRIPCION, ORGANO, LIMITE_DOCUMENTOS, 
    FIRMA, OFICINA, ID_PROCEDIMIENTO)
 Values
   ('TRAM_10', 'TLAAC', 'Licencia de Apertura de Actividad Clasificada (RAMINP) ', '003', '0', 
    '1', '999', '026');
Insert into SGMRTCATALOGO_TRAMITES
   (ID, ASUNTO, DESCRIPCION, ORGANO, LIMITE_DOCUMENTOS, 
    FIRMA, OFICINA, ID_PROCEDIMIENTO)
 Values
   ('TRAM_14', 'TSTEM', 'Solicitud de Tarjeta de Estacionamiento para Minusválidos', '003', '0', 
    '1', '999', '013');
Insert into SGMRTCATALOGO_TRAMITES
   (ID, ASUNTO, DESCRIPCION, ORGANO, LIMITE_DOCUMENTOS, 
    FIRMA, OFICINA, ID_PROCEDIMIENTO)
 Values
   ('TRAM_5', 'TBOEX', 'Bonificaciones y Exenciones ', '003', '0', 
    '1', '999', 'PCD-27');
Insert into SGMRTCATALOGO_TRAMITES
   (ID, ASUNTO, DESCRIPCION, ORGANO, LIMITE_DOCUMENTOS, 
    FIRMA, OFICINA, ID_PROCEDIMIENTO)
 Values
   ('TRAM_6', 'TODP', 'Ocupación del Dominio Público ', '003', '0', 
    '1', '999', 'PCD-29');
Insert into SGMRTCATALOGO_TRAMITES
   (ID, ASUNTO, DESCRIPCION, ORGANO, LIMITE_DOCUMENTOS, 
    FIRMA, OFICINA, ID_PROCEDIMIENTO)
 Values
   ('TRAM_17', 'TCEUR', 'Cédula Urbanística ', '003', '0', 
    '1', '999', 'PCD-25');
Insert into SGMRTCATALOGO_TRAMITES
   (ID, ASUNTO, DESCRIPCION, ORGANO, LIMITE_DOCUMENTOS, 
    FIRMA, OFICINA, ID_PROCEDIMIENTO)
 Values
   ('TRAM_18', 'TCERU', 'Certificaciones Urbanísticas ', '003', '0', 
    '1', '999', 'PCD-4');
Insert into SGMRTCATALOGO_TRAMITES
   (ID, ASUNTO, DESCRIPCION, ORGANO, LIMITE_DOCUMENTOS, 
    FIRMA, OFICINA, ID_PROCEDIMIENTO)
 Values
   ('TRAM_19', 'TSOCP', 'Solicitud de Cita Previa ', '003', '0', 
    '1', '999', 'PCD-192');
COMMIT;

SET DEFINE OFF;
Insert into SGMRTREGISTRO_SECUENCIA
   (ANYO, ETIQUETA, SECUENCIA)
 Values
   ('2012', '99900', 0);
COMMIT;
