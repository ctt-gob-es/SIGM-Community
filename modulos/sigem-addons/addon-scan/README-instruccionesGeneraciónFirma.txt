Para generar la aplicación del escáner hay que ejecutar desde el eclipse Run as --> Maven install.
Esto es así porque Sigem usa Java 1.6 antiguo y esto necesita 1.7. 

Una ver generado se firma. Para firmar aplicaciones hay que utilizar la herramienta signtool.exe que viene con el SDK de Windows. Yo lo he descargado de https://go.microsoft.com/fwlink/?LinkID=698771 (https://stackoverflow.com/questions/31869552/how-to-install-signtool-exe-for-windows-10)
Desde la carpeta /modulos/sigem-addons/addon-scan/src/signApp se ejecuta el comando:

  - "C:\Program Files (x86)\Windows Kits\10\bin\x64\signtool.exe" sign /v /f DipucrSampleCA.pfx /t http://timestamp.verisign.com/scripts/timstamp.dll target\nsis\Setup-addon-scan-1.2.0.exe


Una vez hecho esto ya se encuentra firmada la aplicación del escáner. Ahora hay que copiarla en el proyecto de Registro Messsi y commitearla para que se incluya en el despliegue.

alsigm/modulos/sigem-addons/addon-scan/target/nsis/Setur-addon-scan-1.2.0.exe --> alsigm/sigem/SIGEM_RegistroPresencialMSSSI_Modules/SIGEM_RegistroPresencialMSSSIWeb/src/main/webapp/resources/software


Para generar un certificado para firmar aplicaciones basta con ejecutar los siguientes tres comandos desde la carpeta /modulos/sigem-addons/addon-scan/src/signApp (por defecto he usado como contraseña 123456):

  - "C:\Program Files (x86)\Windows Kits\10\bin\x64\makecert.exe" -r -pe -n "CN=Dipucr" -ss CA -sr CurrentUser -a sha256 -cy authority -sky signature -sv DipucrSampleCA.pvk DipucrSampleCA.cer
  - "C:\Program Files (x86)\Windows Kits\10\bin\x64\makecert.exe" -pe -n "CN=Dipucr" -a sha256 -cy end -sky signature -ic DipucrSampleCA.cer -iv DipucrSampleCA.pvk -sv DipucrSampleSPC.pvk DipucrSampleSPC.cer
  - "C:\Program Files (x86)\Windows Kits\10\bin\x64\pvk2pfx.exe" -pvk DipucrSampleCA.pvk -spc DipucrSampleCA.cer -pfx DipucrSampleCA.pfx
