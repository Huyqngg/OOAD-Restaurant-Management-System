USE HTQLNhaHang;
GO
SET NOCOUNT ON;

SELECT N'Database' AS KiemTra,
       CASE WHEN DB_NAME()=N'HTQLNhaHang' THEN N'OK' ELSE N'FAIL' END AS TrangThai,
       DB_NAME() AS ChiTiet;

SELECT N'Số bảng nghiệp vụ' AS KiemTra,
       CASE WHEN COUNT(*)>=23 THEN N'OK' ELSE N'FAIL' END AS TrangThai,
       CAST(COUNT(*) AS NVARCHAR(20)) AS ChiTiet
FROM sys.tables WHERE is_ms_shipped=0;

SELECT N'Tài khoản mẫu' AS KiemTra,
       CASE WHEN COUNT(*)=4 THEN N'OK' ELSE N'FAIL' END AS TrangThai,
       CAST(COUNT(*) AS NVARCHAR(20)) AS ChiTiet
FROM TaiKhoan WHERE TenDangNhap IN ('admin','phucvu','bep','thungan');

SELECT N'Bàn mẫu' AS KiemTra,
       CASE WHEN COUNT(*)>=20 THEN N'OK' ELSE N'FAIL' END AS TrangThai,
       CAST(COUNT(*) AS NVARCHAR(20)) AS ChiTiet
FROM BanAn;

SELECT N'Món mẫu' AS KiemTra,
       CASE WHEN COUNT(*)>=10 THEN N'OK' ELSE N'FAIL' END AS TrangThai,
       CAST(COUNT(*) AS NVARCHAR(20)) AS ChiTiet
FROM MonAn;

SELECT N'Nguyên liệu mẫu' AS KiemTra,
       CASE WHEN COUNT(*)>=10 THEN N'OK' ELSE N'FAIL' END AS TrangThai,
       CAST(COUNT(*) AS NVARCHAR(20)) AS ChiTiet
FROM NguyenLieu;

SELECT N'View bếp' AS KiemTra,
       CASE WHEN OBJECT_ID(N'dbo.vw_HangDoiBep',N'V') IS NOT NULL THEN N'OK' ELSE N'FAIL' END AS TrangThai,
       N'vw_HangDoiBep' AS ChiTiet;

SELECT N'Stored procedure tìm bàn' AS KiemTra,
       CASE WHEN OBJECT_ID(N'dbo.usp_TimBanTrong',N'P') IS NOT NULL THEN N'OK' ELSE N'FAIL' END AS TrangThai,
       N'usp_TimBanTrong' AS ChiTiet;

SELECT name AS DatabaseUser
FROM sys.database_principals
WHERE name IN (N'restaurant_app',N'restaurant_admin');
GO
