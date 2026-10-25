@echo off
setlocal
chcp 65001 > nul
set /p SERVER=Nhap ten SQL Server (vi du localhost hoac 192.168.1.10): 
set /p USER=Tai khoan SQL co quyen sysadmin (vi du sa): 
set /p PASS=Mat khau: 

for %%F in (
  database\01_CreateDatabase.sql
  database\02_CreateTables.sql
  database\03_IndexesViewsProcedures.sql
  database\04_SeedData.sql
  database\05_CreateLoginsPermissions.sql
  database\08_VerifyInstallation.sql
) do (
  echo Dang chay %%F ...
  sqlcmd -S "%SERVER%" -U "%USER%" -P "%PASS%" -C -b -i "%%F"
  if errorlevel 1 goto error
)

echo Hoan tat cai dat va kiem tra database HTQLNhaHang.
pause
exit /b 0

:error
echo Cai dat that bai. Kiem tra SQL Server, TCP/IP, SQLCMD va quyen tai khoan.
pause
exit /b 1
