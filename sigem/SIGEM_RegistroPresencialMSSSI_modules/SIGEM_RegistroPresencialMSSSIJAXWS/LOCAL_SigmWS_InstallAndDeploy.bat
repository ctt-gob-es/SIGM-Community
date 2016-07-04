@echo off

cd /D %~dp0

FOR /F "tokens=1,2 delims==" %%G IN (envConfig.properties) DO (set %%G=%%H)

set SIGMEAR_PATH=%SIGM_PATH%/sigm.ear

FOR /F "usebackq" %%i IN (`hostname`) DO SET MYVAR=%%i


call mvn -f "%SIGM_PATH%/pom.xml" clean install -P %MYVAR% -Dmaven.test.skip=true

call mvn -f "%SIGMEAR_PATH%/pom.xml" com.oracle.weblogic:weblogic-maven-plugin:undeploy -P %MYVAR%Undeploy -Dmaven.test.skip=true
call mvn -f "%SIGMEAR_PATH%/pom.xml" wagon:upload assembly:assembly com.oracle.weblogic:weblogic-maven-plugin:deploy -P %MYVAR%Deploy -Dmaven.test.skip=true

pause