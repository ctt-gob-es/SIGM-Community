

            /\                                                        /\ 
            ||                                                        ||
            ||                                                        ||
            ||                                                        ||
       _.-~" "~-._                                                _.-~" "~-._
      ^           ^                      __                      ^           ^
     '             '                    (  )                    '             '
     Y             Y                 _.~-  -~._                 Y             Y
    ==             ==            __-^          ^-__            ==             ==
     |             |            |                  |            |             |
     |             |____________|                  |____________|             |
     |                                                                        |
     |                 ______________________________________                 |
     |       _________|                                      |_________       |
     |       \        |         DIPUTACIÓN PROVINCIAL        |        /       |
     |        \       |            DE CIUDAD REAL            |       /        |
     |        /       |______________________________________|       \        |
     |       /____________)                              (____________\       |
     |                                                                        |
     |________________________________________________________________________|



- [Dipucr-Manu 28/06/2016] 
     Instrucciones para montar el código de AL-SIGM, compilarlo y generar el despliegue 
completo de la aplicación para poder ser arrancado en un servidor de aplicaciones.

===================
      ÍNDICE 

1.- Pre-requisitos.
2.- Compilación del código.
3.- Despliegue de la aplicación.
4.- Licencia.

===================


1.- Pre-requisitos:
    1.1 Tener instalado el JDK que se indica en la documentación.
    1.2 Tener instalado Maven 2 como se indica en la documentación.
    1.3 Deben estar configuradas las variables del sistema:

        - JAVA_HOME con la ruta del '<<JDK>>'. 
            (Por ejemplo C:\Program Files (x86)\Java\jdk1.6.0_18)
        - M2_HOME con la ruta de instalación del apache de maven. 
            (Por ejemplo C:\apaches\apache-maven-3.2.2)
        - PATH con las rutas %M2_HOME%;%M2_HOME%\bin;%JAVA_HOME%;%JAVA_HOME%\bin;
        - MAVEN_OPTS con el valor -Xmx1024m -XX:MaxPermSize=256m

    1.4 Modificar el archivo settings.xml de la carpeta .m2 del perfil del usuario 
(por ejemplo C:\Users\Manu\.m2) o del apache maven y añadir un perfil con las rutas al JDK
que se utilizará para compilar (Por ejemplo C:\Program Files (x86)\Java\jdk1.6.0_18):

     ...
    <profiles>
        ...
        <profile>
            <id>development</id>
            <properties>
                <JAVA_1_4_HOME>'<<JDK>>'</JAVA_1_4_HOME>
                <JAVA_1_5_HOME>'<<JDK>>'</JAVA_1_5_HOME>
            </properties>            
        </profile>
    </profiles>
        ...
    <activeProfiles>
        ...
        <activeProfile>development</activeProfile>
    </activeProfiles>
    ...

    1.5 Se comprueba que todo esté bien configurado abriendo un terminal nuevo y ejecutando 
los comandos:

     - javac (para comprobar la instalación de jdc)
     - mvn (para comprobar la instalación de mvn)

     En ambos caso debe dar un mensaje distinto a '''javac o mvn (según el caso)" no se reconoce 
como un comando interno o externo, programa o archivo por lotes ejecutable.

2.- Compilación del código:
    Una vez descargado se realiza la primera compilación necesaria para que se generen los 
artefactos necesarios tanto para desplegar directamente la aplicación como para que compile 
el código y se puedan realizar desarrollos.
    En la primera compilación hay que ejecutar los siguientes comandos en el orden indicado desde 
la ruta donde se hay descagado el código (por ejemplo C:\ALSIGM\alsigm):

    mvn clean install -Dmaven.test.skip=true -Dinit
    mvn clean install -Dmaven.test.skip=true -Djars
    mvn clean install -Dmaven.test.skip=true
    mvn clean install -Dmaven.test.skip=true -Dwars
    mvn clean install -Dmaven.test.skip=true -Dears -P generate-distri

3.- Despliegue de la aplicación:
    Si todo ha finalizado correctamente se habrán generado los WARs de los distintos módulos 
de la aplicación en la carpeta ./alsigm/sigem/SIGEM_DIST que se podrán desplegar en un servidor 
de aplicaciones (previamente configurado como se indica en la documentación).


4.- Licencia

    La siguiente «Licencia Pública de la Unión Europea» («European Union Public Licence EUPL») 
se ha elaborado en el marco de IDABC, programa de la Comunidad Europea cuyo objetivo es promover 
la prestación interoperable de servicios de administración electrónica europea a las 
administraciones públicas, las empresas y los ciudadanos. IDABC prolonga y profundiza el anterior
programa IDA («Intercambio de Datos entre Administraciones»).

EUPL 1.1 (https://joinup.ec.europa.eu/system/files/ES/EUPL%20v.1.1%20-%20Licencia.pdf)