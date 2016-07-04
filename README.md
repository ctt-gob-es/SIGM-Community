#SIGM

SIGM (Sistema Integrado de Gestión Modular) es una aplicación desarrollada para el inicio, tramitación, resolución y archivado del procedimiento administrativo. SIGM es un proyecto de modernización y actualización de las administraciones públicas, dotándolas de un sistema que puede reunir en formato electrónico toda la documentación de un expediente, integrando los tradicionales subsistemas de Registro, Motor de Expedientes (Flujos de procedimientos) y Archivo.

Este repositorio se ha creado en el espacio del CTT de Github desde <a href="https://github.com/e-admin/alsigm">el antiguo repositorio creado en su momento por CENATIC</a>.

####Descarga SIGM

**SIGM 3.0.1:** VM no completa, contiene fuentes y wars de los módulos modificados respecto de la 3.0.

**SIGM 3.0:** VM completa, incluye Postgres, Tomcat y Wars desplegados. Contiene trámites y procedimientos modelados.

Descarga las <a href="https://github.com/Cenatic/alsigm/releases" target="_new">VMs y el código</a>

####Configuración del entorno de desarrollo (Instalación y compilación)

Aquí tienes disponibles las instrucciones de configuración del entorno Maven de desarrollo así como las instrucciones de compilación e instalación.

<a href="https://github.com/Cenatic/alsigm/wiki/Configuraci%C3%B3n-del-entorno-de-desarrollo-(Instalaci%C3%B3n-y-compilaci%C3%B3n)">Instrucciones</a>

##Documentación histórica y nueva documentación

Hay disponible un archivo .rar con documentación de SIGM en <a href="https://github.com/e-admin/alsigm/releases">la página de Releases de este proyecto</a>.

##Bugs, foros y petición de nuevas funcionalidades

Actualmente el proyecto no está ya auspiciado por el Ministerio de Industria, Energía y Turismo. Diversas organismos continuan utilizando SIGM y han decidido liberar el código resultante de su esfuerzo en este proyecto de Github. Si tienes sugerencias, incidencias, etc, puedes utilizar la seccion de issues pero has de tener en cuenta que ninguno de estos organismos ofrece garantías de ningún tipo de soporte.

##Versionado, tags y releases

Con el objetivo principal de asegurar transparencia entre nuestro ciclo de lanzamiento y el esfuerzo por mantener la compatibilidad con versiones anteriores, este proyecto se mantendrá bajo las directrices de versiones semánticas. A veces cuesta un poco mantener un orden en el versionado, pero vamos a adherimos a estas reglas siempre que sea posible.

Se numerarán Releases con el siguiente formato:

"major". "menor". "parche"

Y será construido con las siguientes pautas:

* Romper la compatibilidad hacia atrás choca con el principal al restablecer menor y parche

* Nuevos añadidos sin romper la compatibilidad hacia atrás choca con el menor mientras se restaura el parche

* Corregir errores, y cambios en general, choca sólo con el parche

Para obtener más información sobre Versionamiento Semántico visita <a href="http://semver.org/lang/es/" target="_new">Versionado Semántico 2.0.0-rc.2</a>

####Tags y releases

Tenéis disponibles los tags, las releases de las versiones 3.0 y 3.0.1 en este <a href="https://github.com/Cenatic/alsigm/releases" target="_new">enlace.</a>

##Contribuye

Toda aportación de código o documentación es bienvenida, puedes utilizar las herramietnas de Github como en cualquier otro proyecto.

Con el objetivo de homogeneizar el estilo del código de SIGM escrito en distintos IDEs recomendamos seguir las pautas que nos proponen desde <a href="editorconfig.org" target="_new">editorconfig.org</a>

Y muy importante, respeta nuestro <a href="https://github.com/Cenatic/alsigm/wiki/C%C3%B3digo-conducta" target="_new">código de conducta y buenas prácticas</a>

##Licencia

La siguiente «Licencia Pública de la Unión Europea» («European Union Public Licence EUPL») se ha elaborado en el marco de IDABC, programa de la Comunidad Europea cuyo objetivo es promover la prestación interoperable de servicios de administración electrónica europea a las administraciones públicas, las empresas y los ciudadanos. IDABC prolonga y profundiza el anterior programa IDA («Intercambio de Datos entre Administraciones»). 

<a href="https://joinup.ec.europa.eu/system/files/ES/EUPL%20v.1.1%20-%20Licencia.pdf" target="_new">EUPL 1.1</a>

