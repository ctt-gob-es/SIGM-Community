SET DEFINE OFF;
Insert into CONFIGURACION
   (PARAMETRO, VALOR)
 Values
   ('O00000000_SICRES', '3.0');
Insert into CONFIGURACION
   (PARAMETRO, VALOR)
 Values
   ('O00000000_Integracion', 'WS');
Insert into CONFIGURACION
   (PARAMETRO, VALOR)
 Values
   ('O00000000_Attachments', 'S');
COMMIT;

SET DEFINE OFF;
Insert into QRTZ_LOCKS
   (LOCK_NAME)
 Values
   ('CALENDAR_ACCESS');
Insert into QRTZ_LOCKS
   (LOCK_NAME)
 Values
   ('JOB_ACCESS');
Insert into QRTZ_LOCKS
   (LOCK_NAME)
 Values
   ('MISFIRE_ACCESS');
Insert into QRTZ_LOCKS
   (LOCK_NAME)
 Values
   ('STATE_ACCESS');
Insert into QRTZ_LOCKS
   (LOCK_NAME)
 Values
   ('TRIGGER_ACCESS');
COMMIT;

SET DEFINE OFF;
Insert into SIR_CONFIGURACION
   (ID, NOMBRE, DESCRIPCION, VALOR)
 Values
   (1, 'WS_SIR1.url', 'URL del servicio web WS_SIR1 del SIR por defecto para todas las entidades registrales', 'http://127.0.0.1/services/WS_SIR1');
Insert into SIR_CONFIGURACION
   (ID, NOMBRE, DESCRIPCION, VALOR)
 Values
   (2, 'WS_SIR6_A.url', 'URL del servicio web WS_SIR6_A del CIR por defecto para todas las entidades registrales', 'http://127.0.0.1/services/WS_SIR6_A');
Insert into SIR_CONFIGURACION
   (ID, NOMBRE, DESCRIPCION, VALOR)
 Values
   (3, 'WS_SIR6_B.url', 'URL del servicio web WS_SIR6_B del CIR por defecto para todas las entidades registrales', 'http://127.0.0.1/services/WS_SIR6_B');
Insert into SIR_CONFIGURACION
   (ID, NOMBRE, DESCRIPCION, VALOR)
 Values
   (4, 'WS_SIR7.url', 'URL del servicio web WS_SIR7 del CIR por defecto para todas las entidades registrales', 'http://127.0.0.1/services/WS_SIR7');
Insert into SIR_CONFIGURACION
   (ID, NOMBRE, DESCRIPCION, VALOR)
 Values
   (5, 'envios.time-out', 'Time-out (en milisegundos) para recibir el mensaje de respuesta (ACK o ERROR) tras el envío de ficheros de datos de intercambio (ENVÍO, REENVÍO o RECHAZO).', '86400000');
Insert into SIR_CONFIGURACION
   (ID, NOMBRE, DESCRIPCION, VALOR)
 Values
   (6, 'envios.numeroReintentos', 'Número de reintentos de envío si no se recibe respuesta.', '5');
Insert into SIR_CONFIGURACION
   (ID, NOMBRE, DESCRIPCION, VALOR)
 Values
   (7, 'algoritmo.hash', 'Algoritmo de generación del código hash. Los valores posibles son: SHA-1, MD5', 'SHA-1');
Insert into SIR_CONFIGURACION
   (ID, NOMBRE, DESCRIPCION, VALOR)
 Values
   (8, 'auditoria.ficherosIntercambio.activado', 'Indica si está activada la auditoría de ficheros de datos de intercambio entrantes y salientes. Los valores posibles son: true o false.', 'false');
Insert into SIR_CONFIGURACION
   (ID, NOMBRE, DESCRIPCION, VALOR)
 Values
   (9, 'auditoria.ficherosIntercambio.contentTypeId', 'Identificador del tipo de contenido utilizado para almacenar los XMLs de los ficheros de intercambio en el gestor documental. Debe estár configurado en el fichero "fwktd-dm-config.xml".', 'fwktdsir-auditoria-ficherosIntercambio');
Insert into SIR_CONFIGURACION
   (ID, NOMBRE, DESCRIPCION, VALOR)
 Values
   (10, 'auditoria.mensajes.activado', 'Indica si está activada la auditoría de ficheros de datos de control entrantes y salientes. Los valores posibles son: true o false.', 'false');
Insert into SIR_CONFIGURACION
   (ID, NOMBRE, DESCRIPCION, VALOR)
 Values
   (11, 'auditoria.mensajes.contentTypeId', 'Identificador del tipo de contenido utilizado para almacenar los XMLs de los mensajes intercambiados en el gestor documental. Debe estár configurado en el fichero "fwktd-dm-config.xml".', 'fwktdsir-auditoria-mensajes');
Insert into SIR_CONFIGURACION
   (ID, NOMBRE, DESCRIPCION, VALOR)
 Values
   (12, 'anexos.contentTypeId', 'Identificador del tipo de contenido utilizado en la gestión documental. Debe estár configurado en el fichero de configuración fwktd-dm-config.xml.', 'fwktdsir');
Insert into SIR_CONFIGURACION
   (ID, NOMBRE, DESCRIPCION, VALOR)
 Values
   (13, 'validar.codigos.directorio.comun', 'Indica si hay que validar los códigos de entidades registrales y unidades de tramitación contra el directorio común. Valores: true o false (por defecto)', 'true');
Insert into SIR_CONFIGURACION
   (ID, NOMBRE, DESCRIPCION, VALOR)
 Values
   (14, 'max.tamaño.anexos', 'Tamaño máximo en bytes de cada fichero de intercambio. Si no se indica o el valor es 0, no se aplica límite alguno.', '3145728');
Insert into SIR_CONFIGURACION
   (ID, NOMBRE, DESCRIPCION, VALOR)
 Values
   (15, 'max.num.anexos', 'Número máximo de anexos de tipo 02 que puede tener un fichero de intercambio. Si no se indica o el valor es 0, no se aplica límite alguno.', '5');
COMMIT;

SET DEFINE OFF;
Insert into SIR_CONTADORES
   (CD_ENT_REG, TIPO, CONTADOR)
 Values
   ('O00002061', 1, 0);
Insert into SIR_CONTADORES
   (CD_ENT_REG, TIPO, CONTADOR)
 Values
   ('O00000250', 1, 1);
Insert into SIR_CONTADORES
   (CD_ENT_REG, TIPO, CONTADOR)
 Values
   ('O00002062', 1, 1);
COMMIT;

SET DEFINE OFF;
Insert into URL_APLICACIONES
   (CD_OFICINA_REGISTRO, DS_OFICINA_REGISTRO, URL)
 Values
   ('O00000000_registro', 'DENOMINACION DE LA OFICINA', 'http://_');
Insert into URL_APLICACIONES
   (CD_OFICINA_REGISTRO, DS_OFICINA_REGISTRO, URL)
 Values
   ('O00000000_mensaje', 'DENOMINACION DE LA OFICINA', 'http://_');
COMMIT;
