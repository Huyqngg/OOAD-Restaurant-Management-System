@echo off
chcp 65001 > nul
mvn clean test package
pause
