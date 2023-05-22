# SIGM5.0-QUIJOTE

SIGM5.0-QUIJOTE es una rama creada a partir de la versión SIGM4.0-QUIJOTE donde se incluyen el código desarrollado por la Diputación Provincial de Ciudad Real (en adelante Dipucr) y se ha integrado con la rama .
REGISTR-v3.0-MSSSSI2.0.0

#### Configuración del entorno de desarrollo (Instalación y compilación)

El documento **README.txt** contiene las instrucciones de configuración del entorno Maven de desarrollo así como las instrucciones para realizar la **primera compilación e instalación** del código fuente recién descargado.
Dicho fichero se encuentran en el directorio raiz del proyecto.

## Mejoras incluidas en la rama
 * Integración con la rama del Registro Presencial del MSSSI.
 * Modificaciones sobre el registro nuevo para hacerlo multientidad y añadirle las funcionalidades que tenía el registro presencial de AL-SIGM (distribución manual a grupos y usuarios, gestión de terceros, etc.)
 * Integración de la mejora de la Diputación de A Coruña para el escáneo de documentos en el registro nuevo sin uso de applets.
 * Integración con AutoFirma en el registro nuevo sin uso de applets.
 * Metadatado de documentos en el registro nuevo conforme al ENI.
 * Integración con los servicios web de ORVE.
 * Se completa la integración con la BDNS.
 * Inclusión de los firmantes visibles en el pdf.
 * Se ha externalizado la configuración de libreoffice para poder asignar hilos distintos a cada entidad.
 * Se han separado las carpetas temporales por entidad.
 * Integración con la Licitación Electrónica de la Plataforma de Contratación del Sector Público.
 * Se ha configurado el Portafirmas externo del MINHAP para que sea multientidad.
 * Integración con la plataforma Notifica / Carpeta Ciudadana.
 * Inclusión de una nueva opción de Repositorio Común de plantillas de documentos a todas las entidades.
 * Edición de plantillas de documentos desde el tramitador de expedientes.
 * Inclusión de una búsqueda rápida de expedientes en la pantalla de inicio.
 * Conexión con @firma mediante aunteticación con certificado.
 * Integración con DIR3 para interesados.
 * Posibilidad de indicación del código DIR3, CIF, SIA, DEH para entidades.
 * Se ha comenzado la migración de los módulos a java 7.
 * Correción de errores.
 * Mejoras generales del rendimiento.

## Licencia

La siguiente «Licencia Pública de la Unión Europea» («European Union Public Licence EUPL») se ha elaborado en el marco de IDABC, programa de la Comunidad Europea cuyo objetivo es promover la prestación interoperable de servicios de administración electrónica europea a las administraciones públicas, las empresas y los ciudadanos. IDABC prolonga y profundiza el anterior programa IDA («Intercambio de Datos entre Administraciones»). 

<a href="[https://joinup.ec.europa.eu/system/files/ES/EUPL%20v.1.1%20-%20Licencia.pdf](https://joinup.ec.europa.eu/sites/default/files/custom-page/attachment/eupl1.1.-licence-es_0.pdf)" target="_new">EUPL 1.1</a>

