#SIGM-REGISTRO del Ministerio de Sanidad Servicios Sociales e Igualdad

En esta rama del repositorio tiene a su disposición el código del módulo de Registro de SIGM desarrollado por el Ministerio de Sanidad, Servicios Sociales e Igualdad (en adelante MSSSI). Las principales particularidades frente al módulo de Registro de la rama *master* son:

- Este es el código de la aplicación que, instalada y operativa en el MSSSI, ha superado el proceso de certificación para conectarse al Servicio de Intercambio Registral (SIR).
- Se ha construido una nueva interfaz gráfica que refuerza tanto la usabilidad como la seguridad de la aplicación (se utiliza tecnología más reciente que incluye validaciones de entrada y salida robustas).
- Se han utilizado librerías de manejo de datos, de informes, de tareas programadas, etc, en versiones recientes lo que simplifica el mantenimiento y los futuros desarrollos de la aplicación. 

## Nota importante

El objetivo de esta liberación es ofrecer a los organismos interesados el código fuente de su aplicación para facilitar su reutilización. La aplicación como un todo no es completamente funcional dado que se conecta con elementos comunes de la infraestructura del MSSSI del que el usuario no dispondrá:

- Plataforma de firma del MSSSI
- Servicio de verificación de certificados del MSSSI

Para generar a partir de este código una versión plenamente funcional en otro organismo habría que adaptarlo para utilizar los módulos del SIGM estándar o bien los del organismo en cuestión.

##Licencia

La siguiente «Licencia Pública de la Unión Europea» («European Union Public Licence EUPL») se ha elaborado en el marco de IDABC, programa de la Comunidad Europea cuyo objetivo es promover la prestación interoperable de servicios de administración electrónica europea a las administraciones públicas, las empresas y los ciudadanos. IDABC prolonga y profundiza el anterior programa IDA («Intercambio de Datos entre Administraciones»). 

<a href="https://joinup.ec.europa.eu/system/files/ES/EUPL%20v.1.1%20-%20Licencia.pdf" target="_new">EUPL 1.1</a>

