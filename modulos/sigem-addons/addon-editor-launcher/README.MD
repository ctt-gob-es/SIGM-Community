Versión 1.3.0
-------------
Se modifica el fichero de configuración de logs para que se generen en el directorio de usuario


Versión 1.3.1
-------------
Se actualiza la versión del framework, para emplear las últimas revisiones de las dependencias de spring y http-client de apache.
Se modifica el procedimiento de envío de ficheros al servidor, creando una copia previa.
Se modifica el procedimiento para comprobar si la aplicación ya terminó de subir los ficheros, buscando un .uploading.

Versión 1.3.2
-------------
Se corrige la comprobación de nueva versión. Había queado comentada.

Versión 1.3.3
-------------
Se modifica la condición de comprobación de fichero bloqueado en windows. Se hará en dos pasos, y de forma similar a como se hace en linux, buscando un fichero "lock".
Se aumenta a dos segundos el tiempo de doble check para asegurase de que un fichero ya está libre.
Se mejoran las trazas de log.