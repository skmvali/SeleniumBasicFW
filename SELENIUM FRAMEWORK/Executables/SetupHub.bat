pushd %~dp0%..\lib
set SELSERV="selenium-server-standalone-*.jar"
for /f %%i in (%SELSERV%) do set SELSERV=%%~fi
start java -jar "%SELSERV%" -role hub -hubConfig ..\ConfigFiles\HubConfig.json
::echo %~dp0%..lib

