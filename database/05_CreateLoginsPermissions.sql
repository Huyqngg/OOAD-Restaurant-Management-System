USE master;
GO
IF NOT EXISTS(SELECT 1 FROM sys.server_principals WHERE name=N'restaurant_app')
    CREATE LOGIN restaurant_app WITH PASSWORD='RestaurantApp@2026', CHECK_POLICY=ON, CHECK_EXPIRATION=OFF;
IF NOT EXISTS(SELECT 1 FROM sys.server_principals WHERE name=N'restaurant_admin')
    CREATE LOGIN restaurant_admin WITH PASSWORD='RestaurantAdmin@2026', CHECK_POLICY=ON, CHECK_EXPIRATION=OFF;
GO
USE HTQLNhaHang;
GO
IF NOT EXISTS(SELECT 1 FROM sys.database_principals WHERE name=N'restaurant_app')
    CREATE USER restaurant_app FOR LOGIN restaurant_app;
IF NOT EXISTS(SELECT 1 FROM sys.database_principals WHERE name=N'restaurant_admin')
    CREATE USER restaurant_admin FOR LOGIN restaurant_admin;
GO
ALTER ROLE db_datareader ADD MEMBER restaurant_app;
ALTER ROLE db_datawriter ADD MEMBER restaurant_app;
GRANT EXECUTE TO restaurant_app;
GRANT SELECT ON SCHEMA::dbo TO restaurant_app;
GRANT INSERT,UPDATE,DELETE ON SCHEMA::dbo TO restaurant_app;
ALTER ROLE db_owner ADD MEMBER restaurant_admin;
GO
-- Thay mật khẩu mặc định trước khi triển khai thực tế.
