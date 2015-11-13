pushd %~dp0%..\lib
set SELSERV="selenium-server-standalone-*.jar"
for /f %%i in (%SELSERV%) do set SELSERV=%%~fi

start java -jar "%SELSERV%" -role webdriver -nodeConfig ..\ConfigFiles\NodeConfig.json