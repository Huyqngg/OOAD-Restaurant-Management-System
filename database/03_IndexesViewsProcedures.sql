USE HTQLNhaHang;
GO
CREATE INDEX IX_TaiKhoan_TenDangNhap ON TaiKhoan(TenDangNhap);
CREATE INDEX IX_KhachHang_SDT ON KhachHang(SDT);
CREATE INDEX IX_BanAn_TrangThai ON BanAn(TrangThai,KhuVuc,SucChua);
CREATE INDEX IX_DatBan_ThoiGian ON DatBan(ThoiGianDat,TrangThai,MaBan);
CREATE INDEX IX_MonAn_TenTrangThai ON MonAn(TenMon,TrangThaiPhucVu);
CREATE INDEX IX_PhieuGoiMon_BanTrangThai ON PhieuGoiMon(MaBan,TrangThaiPhieu,ThoiGianTao);
CREATE INDEX IX_ChiTietGoiMon_Bep ON ChiTietGoiMon(TrangThaiCheBien,MaPhieu);
CREATE INDEX IX_HoaDon_Ngay ON HoaDon(NgayGioLap,TrangThaiThanhToan);
CREATE INDEX IX_NguyenLieu_Ton ON NguyenLieu(TrangThai,SoLuongTon,DinhMucToiThieu);
CREATE INDEX IX_PhieuNhap_Ngay ON PhieuNhapKho(NgayNhap);
GO

CREATE OR ALTER VIEW vw_HangDoiBep AS
SELECT ct.MaCTGoiMon,p.MaPhieuCode,b.MaBanCode,m.TenMon,ct.SoLuong,ct.GhiChuRieng,
       ct.TrangThaiCheBien,p.ThoiGianGuiBep,ct.ThoiGianBatDau,ct.ThoiGianHoanThanh
FROM ChiTietGoiMon ct
JOIN PhieuGoiMon p ON p.MaPhieu=ct.MaPhieu
JOIN BanAn b ON b.MaBan=p.MaBan
JOIN MonAn m ON m.MaMon=ct.MaMon
WHERE p.TrangThaiPhieu IN ('DA_GUI_BEP','DANG_PHUC_VU','CHO_THANH_TOAN')
  AND ct.TrangThaiCheBien <> 'DA_HUY';
GO

CREATE OR ALTER VIEW vw_NguyenLieuSapHet AS
SELECT n.MaNL,n.MaNLCode,n.TenNL,n.DonViTinh,n.SoLuongTon,n.DinhMucToiThieu,d.TenDanhMuc
FROM NguyenLieu n JOIN DanhMucNguyenLieu d ON d.MaDanhMucNL=n.MaDanhMucNL
WHERE n.TrangThai='HOAT_DONG' AND n.SoLuongTon<=n.DinhMucToiThieu;
GO

CREATE OR ALTER VIEW vw_DoanhThuNgay AS
SELECT CAST(NgayGioLap AS DATE) Ngay,COUNT(*) SoHoaDon,SUM(TongThanhToan) DoanhThu,
       SUM(TienGiamKhuyenMai) TienGiamKhuyenMai,SUM(TienGiamDiem) TienGiamDiem
FROM HoaDon WHERE TrangThaiThanhToan='DA_THANH_TOAN'
GROUP BY CAST(NgayGioLap AS DATE);
GO

CREATE OR ALTER PROCEDURE usp_TimBanTrong
    @KhuVuc NVARCHAR(80)=NULL,
    @SucChuaToiThieu INT=1
AS
BEGIN
    SET NOCOUNT ON;
    SELECT * FROM BanAn
    WHERE TrangThai='TRONG' AND (@KhuVuc IS NULL OR KhuVuc=@KhuVuc) AND SucChua>=@SucChuaToiThieu
    ORDER BY SucChua,MaBanCode;
END
GO

CREATE OR ALTER PROCEDURE usp_CanhBaoDatBanQuaHan
AS
BEGIN
    SET NOCOUNT ON;
    UPDATE d SET TrangThai='QUA_HAN'
    FROM DatBan d
    WHERE d.TrangThai IN ('DA_DAT','DA_XAC_NHAN') AND d.ThoiGianDat<DATEADD(MINUTE,-30,SYSDATETIME());

    UPDATE b SET TrangThai='TRONG'
    FROM BanAn b
    WHERE b.TrangThai='DA_DAT'
      AND NOT EXISTS(SELECT 1 FROM DatBan d WHERE d.MaBan=b.MaBan AND d.TrangThai IN ('DA_DAT','DA_XAC_NHAN'));
END
GO
