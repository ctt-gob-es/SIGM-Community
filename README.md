#SIGM4.0-QUIJOTE

SIGM4.0-QUIJOTE es una rama creada a partir de la última versión de SIGM (Sistema Integrado de Gestión Modular) donde se incluyen el código desarrollado por la Diputación Provincial de Ciudad Real (en adelante Dipucr).

####Configuración del entorno de desarrollo (Instalación y compilación)

El documento **README.txt** contiene las instrucciones de configuración del entorno Maven de desarrollo así como las instrucciones para realizar la **primera compilación e instalación** del código fuente recién descargado.
Dicho fichero se encuentran en el directorio raiz del proyecto.

##Mejoras incluidas en la rama
###Mejoras Técnicas
* Rendimiento en general:
  * Lista de trabajo.
  * Datos de trámites.
  * Expedientes relacionados.
  * Auditorías.
* Eliminación de java:
  * En la edición de documentos.
  * En la firma de documentos desde el tramitador.
  * En la firma de solicitudes del registro telemático.
* Firma 3 fases en servidor externo.
* Histórico de expedientes, trámites, participantes, documentos e hitos.
* Registro Telemático, no almacenar los documentos anexos y justificantes de registro en BD, almacenarlos en repositorio.
* Solucionado el problema al trabajar con varios OpenOffice.
* Conexiones con aplicaciones externas:
  * Comparece.
  * Servicio de Verificación de Datos.
  * Plataforma de Contratación del Estado.
  * Factura Electrónica.
  * Boletín Oficial de la Provincia.
  * Tablón de anuncios electrónico.
  * TEU.
  * OwnCloud.
  * Web del Empleado.
  * Web del Diputado.
  * PortaFirmas - MINHAP.
  * BDNS
  * …
* Compilación del código y despliegue de las aplicaciones mediante maven, tanto para linux como para windows.
* Indicar en el log la entidad a la que se corresponde la traza.
* Adaptación para trabajar con ODSs.

###Mejoras Funcionales
* **Tramitador**:
  * Lista de trabajo.
  * Cambios apariencia Avisos electrónicos.
  * Registros distribuidos, añadir pestaña para consultar los documentos del registro.
  * Filtro de año de inicio de los expedientes.
  * Ordenación de los procedimientos alfabéticamente.
  * Manuales de usuario globales
* Expediente:
  * Reabrir expediente.
  * Manuales de usuario.
  * Árbol de ayuda.
  * Legislación y jurisprudencia.
  * Datos de trámite mostrar únicamente los abiertos y/o los 5 últimos trámites. Añadir opción ver todos.
  * Datos de trámites anteriores mostrar la fase a la que pertenecían los trámites anteriores.
  * Expedientes relacionados mostrar opción de ver todos.
  * Indicar el asunto del expediente en la cabecera de la página.
  * Edición de documentos desde el tramitador controlado por permisos al usuario.
* Trámite:
  * Reabrir Trámite
  * Datos de Trámite oculto por defecto.
  * Cambios en la botonera (Registrar Todo, Firmar Todo, etc.)
  * Cambios en la apariencia del listado de documentos (Universidad de Cartagena).
* Datos del Documento:
  * Mostrar el CVE.
  * Mostrar el motivo de rechazo de la firma.
  * Anular circuito de firma.
* Participantes:
  * Importar participantes de otros expedientes.
  * Borrar todos los participantes.
* Firma:
  * Rechazo de firma.
  * Histórico de firmas rechazadas.
* Módulo de avisos a todos los usuarios o a todos los usuarios conectados.
* **Catálogo de Procedimientos**:
* Inventario / Procedimientos:
  * Asociación de manuales de usuarios específicos por procedimiento.
  * Circuitos de firma específicos por trámite.
  * Datos específicos y plantilla por defecto de un trámite.
  * Nuevo evento al Relacionar expedientes.
* Componentes:
  * Mantenimiento de Manuales de Usuario.
* Consulta de documentos por CVE, mostrar los datos del registro de salida.

##Licencia

La siguiente «Licencia Pública de la Unión Europea» («European Union Public Licence EUPL») se ha elaborado en el marco de IDABC, programa de la Comunidad Europea cuyo objetivo es promover la prestación interoperable de servicios de administración electrónica europea a las administraciones públicas, las empresas y los ciudadanos. IDABC prolonga y profundiza el anterior programa IDA («Intercambio de Datos entre Administraciones»). 

<a href="https://joinup.ec.europa.eu/system/files/ES/EUPL%20v.1.1%20-%20Licencia.pdf" target="_new">EUPL 1.1</a>

