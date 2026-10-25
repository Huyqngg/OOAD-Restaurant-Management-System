USE master;
GO
-- BACKUP: sửa đường dẫn theo máy chủ SQL Server.
-- BACKUP DATABASE HTQLNhaHang TO DISK=N'C:\SQLBackup\HTQLNhaHang.bak' WITH INIT,COMPRESSION,CHECKSUM,STATS=10;
-- RESTORE VERIFYONLY FROM DISK=N'C:\SQLBackup\HTQLNhaHang.bak';
-- RESTORE DATABASE HTQLNhaHang_Test FROM DISK=N'C:\SQLBackup\HTQLNhaHang.bak'
-- WITH MOVE N'HTQLNhaHang' TO N'C:\SQLData\HTQLNhaHang_Test.mdf',
--      MOVE N'HTQLNhaHang_log' TO N'C:\SQLData\HTQLNhaHang_Test_log.ldf',REPLACE,STATS=10;
