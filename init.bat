@echo off
echo Dieser Script bereitet die Repository für die Entwicklung und das Bauen vor
mkdir lib
bitsadmin.exe /transfer "JobName" "http://deploy.curoerp.de/core/0.0.1/CuroERP-Core_0.0.1_107.jar" "%cd%\lib\CuroERP-Core.jar"
pause 