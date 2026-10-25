-- Bật SQLCMD Mode trong SSMS: Query -> SQLCMD Mode, sau đó chạy file này.
:r .\01_CreateDatabase.sql
:r .\02_CreateTables.sql
:r .\03_IndexesViewsProcedures.sql
:r .\04_SeedData.sql
:r .\05_CreateLoginsPermissions.sql
PRINT N'Hoàn tất cài đặt database HTQLNhaHang';
GO
