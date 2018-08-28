@echo off
echo Dieser Script bereitet die Repository für die Entwicklung und das Bauen vor
mkdir lib
rem bitsadmin.exe /transfer "JobName" "http://deploy.curoerp.de/core/0.0.1/CuroERP-Core_0.0.1_107.jar" "%cd%\lib\CuroERP-Core.jar"

rem keygenerator
mkdir test
mkdir test\config
cd test\config

"C:\Program Files\Java\jdk1.8.0_161\bin\keytool.exe" -keystore curo -genkey

cd ..\..

pause 