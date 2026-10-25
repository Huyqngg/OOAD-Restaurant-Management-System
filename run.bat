@echo off
chcp 65001 > nul
mvn -q clean package -DskipTests
if errorlevel 1 pause & exit /b 1
java -jar target\htql-nha-hang-1.0.0.jar
pause
