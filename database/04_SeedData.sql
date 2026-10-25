USE HTQLNhaHang;
GO
SET NOCOUNT ON;

DECLARE @DefaultHash VARCHAR(255)='PBKDF2$YcabpxcisJbl+D/q3WtlYQ==$7oIJ6yMFfPy0WX1FhnjrzL5sWGyr11IW5wy7EgMkBqI=';

IF NOT EXISTS(SELECT 1 FROM NhanVien)
BEGIN
    INSERT NhanVien(MaNhanVienCode,HoTen,CCCD,SDT,Email,DiaChi,VaiTro,TrangThaiLamViec) VALUES
    ('NV001',N'Nguyễn Văn Quản Lý','001200000001','0901000001','admin@restaurant.local',N'Hà Nội','CHU_NHA_HANG','DANG_LAM'),
    ('NV002',N'Trần Thị Phục Vụ','001200000002','0901000002','phucvu@restaurant.local',N'Hà Nội','PHUC_VU','DANG_LAM'),
    ('NV003',N'Lê Văn Bếp','001200000003','0901000003','bep@restaurant.local',N'Hà Nội','DAU_BEP','DANG_LAM'),
    ('NV004',N'Phạm Thị Thu Ngân','001200000004','0901000004','thungan@restaurant.local',N'Hà Nội','THU_NGAN','DANG_LAM');

    INSERT TaiKhoan(TenDangNhap,MatKhauHash,TrangThai,MaNhanVien)
    SELECT 'admin',@DefaultHash,'HOAT_DONG',MaNhanVien FROM NhanVien WHERE MaNhanVienCode='NV001'
    UNION ALL SELECT 'phucvu',@DefaultHash,'HOAT_DONG',MaNhanVien FROM NhanVien WHERE MaNhanVienCode='NV002'
    UNION ALL SELECT 'bep',@DefaultHash,'HOAT_DONG',MaNhanVien FROM NhanVien WHERE MaNhanVienCode='NV003'
    UNION ALL SELECT 'thungan',@DefaultHash,'HOAT_DONG',MaNhanVien FROM NhanVien WHERE MaNhanVienCode='NV004';
END
GO

IF NOT EXISTS(SELECT 1 FROM BanAn)
BEGIN
    DECLARE @i INT=1;
    WHILE @i<=20
    BEGIN
        INSERT BanAn(MaBanCode,TenBan,KhuVuc,SucChua,TrangThai)
        VALUES(CONCAT('B',FORMAT(@i,'00')),CONCAT(N'Bàn ',@i),CASE WHEN @i<=10 THEN N'Tầng 1' ELSE N'Tầng 2' END,
               CASE WHEN @i%5=0 THEN 8 WHEN @i%3=0 THEN 6 WHEN @i%2=0 THEN 4 ELSE 2 END,'TRONG');
        SET @i+=1;
    END
END
GO

IF NOT EXISTS(SELECT 1 FROM KhachHang)
BEGIN
    INSERT KhachHang(MaKhachHangCode,HoTen,SDT,Email,DiemTichLuy,TrangThai) VALUES
    ('KH001',N'Nguyễn Minh Anh','0911000001','minhanh@example.com',120,'HOAT_DONG'),
    ('KH002',N'Trần Quốc Bảo','0911000002','quocbao@example.com',45,'HOAT_DONG'),
    ('KH003',N'Lê Thu Hà','0911000003','thuha@example.com',300,'HOAT_DONG');
END
GO

IF NOT EXISTS(SELECT 1 FROM DanhMucMon)
BEGIN
    INSERT DanhMucMon(TenDanhMuc,MoTa) VALUES
    (N'Khai vị',N'Món khai vị'),(N'Món chính',N'Món ăn chính'),(N'Lẩu',N'Các món lẩu'),
    (N'Đồ uống',N'Nước uống'),(N'Tráng miệng',N'Món ngọt và trái cây');
END
GO

IF NOT EXISTS(SELECT 1 FROM MonAn)
BEGIN
    DECLARE @KhaiVi INT=(SELECT MaDanhMucMon FROM DanhMucMon WHERE TenDanhMuc=N'Khai vị');
    DECLARE @MonChinh INT=(SELECT MaDanhMucMon FROM DanhMucMon WHERE TenDanhMuc=N'Món chính');
    DECLARE @Lau INT=(SELECT MaDanhMucMon FROM DanhMucMon WHERE TenDanhMuc=N'Lẩu');
    DECLARE @DoUong INT=(SELECT MaDanhMucMon FROM DanhMucMon WHERE TenDanhMuc=N'Đồ uống');
    DECLARE @TrangMieng INT=(SELECT MaDanhMucMon FROM DanhMucMon WHERE TenDanhMuc=N'Tráng miệng');
    INSERT MonAn(MaMonCode,MaDanhMucMon,TenMon,DonViTinh,DonGia,MoTa,TrangThaiPhucVu) VALUES
    ('M001',@KhaiVi,N'Nem rán',N'đĩa',85000,N'Nem rán truyền thống','CON_MON'),
    ('M002',@KhaiVi,N'Gỏi cuốn tôm thịt',N'đĩa',95000,N'Gỏi cuốn tươi','CON_MON'),
    ('M003',@MonChinh,N'Bò sốt vang',N'phần',165000,N'Bò hầm sốt vang','CON_MON'),
    ('M004',@MonChinh,N'Cơm rang hải sản',N'đĩa',120000,N'Cơm rang tôm mực','CON_MON'),
    ('M005',@MonChinh,N'Gà nướng mật ong',N'con',320000,N'Gà ta nướng','CON_MON'),
    ('M006',@MonChinh,N'Cá hồi áp chảo',N'phần',245000,N'Cá hồi sốt chanh','CON_MON'),
    ('M007',@Lau,N'Lẩu thái hải sản',N'nồi',450000,N'Lẩu thái chua cay','CON_MON'),
    ('M008',@Lau,N'Lẩu riêu cua bắp bò',N'nồi',480000,N'Lẩu riêu cua','CON_MON'),
    ('M009',@DoUong,N'Nước cam',N'ly',45000,N'Nước cam tươi','CON_MON'),
    ('M010',@DoUong,N'Trà đào',N'ly',40000,N'Trà đào cam sả','CON_MON'),
    ('M011',@DoUong,N'Nước suối',N'chai',15000,N'Chai 500ml','CON_MON'),
    ('M012',@TrangMieng,N'Chè khúc bạch',N'cốc',45000,N'Chè khúc bạch','CON_MON'),
    ('M013',@TrangMieng,N'Đĩa hoa quả',N'đĩa',90000,N'Hoa quả theo mùa','CON_MON');
END
GO

IF NOT EXISTS(SELECT 1 FROM DanhMucNguyenLieu)
BEGIN
    INSERT DanhMucNguyenLieu(TenDanhMuc,MoTa) VALUES
    (N'Thịt',N'Thịt các loại'),(N'Hải sản',N'Tôm cá mực'),(N'Rau củ',N'Rau củ quả'),
    (N'Gia vị',N'Gia vị và sốt'),(N'Đồ uống',N'Nguyên liệu pha chế'),(N'Khác',N'Nguyên liệu khác');
END
GO

IF NOT EXISTS(SELECT 1 FROM NguyenLieu)
BEGIN
    DECLARE @Thit INT=(SELECT MaDanhMucNL FROM DanhMucNguyenLieu WHERE TenDanhMuc=N'Thịt');
    DECLARE @HaiSan INT=(SELECT MaDanhMucNL FROM DanhMucNguyenLieu WHERE TenDanhMuc=N'Hải sản');
    DECLARE @Rau INT=(SELECT MaDanhMucNL FROM DanhMucNguyenLieu WHERE TenDanhMuc=N'Rau củ');
    DECLARE @GiaVi INT=(SELECT MaDanhMucNL FROM DanhMucNguyenLieu WHERE TenDanhMuc=N'Gia vị');
    DECLARE @DU INT=(SELECT MaDanhMucNL FROM DanhMucNguyenLieu WHERE TenDanhMuc=N'Đồ uống');
    INSERT NguyenLieu(MaNLCode,MaDanhMucNL,TenNL,DonViTinh,SoLuongTon,DinhMucToiThieu,DonGiaNhapGanNhat,TrangThai) VALUES
    ('NL001',@Thit,N'Thịt bò',N'kg',25,5,220000,'HOAT_DONG'),
    ('NL002',@Thit,N'Thịt gà',N'kg',30,5,95000,'HOAT_DONG'),
    ('NL003',@HaiSan,N'Tôm',N'kg',18,4,260000,'HOAT_DONG'),
    ('NL004',@HaiSan,N'Mực',N'kg',14,3,240000,'HOAT_DONG'),
    ('NL005',@HaiSan,N'Cá hồi',N'kg',10,2,380000,'HOAT_DONG'),
    ('NL006',@Rau,N'Khoai tây',N'kg',35,6,25000,'HOAT_DONG'),
    ('NL007',@Rau,N'Rau thơm',N'kg',8,1,65000,'HOAT_DONG'),
    ('NL008',@GiaVi,N'Dầu ăn',N'lít',20,4,52000,'HOAT_DONG'),
    ('NL009',@GiaVi,N'Sốt vang',N'lít',8,2,90000,'HOAT_DONG'),
    ('NL010',@DU,N'Cam tươi',N'kg',20,4,45000,'HOAT_DONG'),
    ('NL011',@DU,N'Trà đào',N'kg',7,1,110000,'HOAT_DONG');
END
GO

IF NOT EXISTS(SELECT 1 FROM NhaCungCap)
BEGIN
    INSERT NhaCungCap(MaNCCCode,TenNCC,SDT,Email,DiaChi,TrangThai) VALUES
    ('NCC001',N'Công ty Thực phẩm Xanh','0988000001','xanh@example.com',N'Hà Nội','HOAT_DONG'),
    ('NCC002',N'Hải sản Biển Đông','0988000002','biendong@example.com',N'Quảng Ninh','HOAT_DONG'),
    ('NCC003',N'Nông sản An Việt','0988000003','anviet@example.com',N'Hưng Yên','HOAT_DONG');
END
GO

IF NOT EXISTS(SELECT 1 FROM CongThucMonAn)
BEGIN
    INSERT CongThucMonAn(MaMon,MaNL,DinhLuong)
    SELECT m.MaMon,n.MaNL,x.DinhLuong
    FROM (VALUES
      ('M003','NL001',CAST(0.25 AS DECIMAL(18,3))),('M003','NL006',0.15),('M003','NL009',0.05),
      ('M004','NL003',0.08),('M004','NL004',0.08),('M005','NL002',1.20),
      ('M006','NL005',0.22),('M007','NL003',0.20),('M007','NL004',0.20),('M008','NL001',0.25),
      ('M009','NL010',0.30),('M010','NL011',0.05)
    ) x(MaMonCode,MaNLCode,DinhLuong)
    JOIN MonAn m ON m.MaMonCode=x.MaMonCode JOIN NguyenLieu n ON n.MaNLCode=x.MaNLCode;
END
GO

IF NOT EXISTS(SELECT 1 FROM KhuyenMai)
BEGIN
    INSERT KhuyenMai(MaKMCode,TenKM,LoaiKM,GiaTriGiam,DieuKienToiThieu,MucGiamToiDa,NgayBatDau,NgayKetThuc,TrangThai) VALUES
    ('KM10',N'Giảm 10% hóa đơn','PHAN_TRAM',10,300000,100000,DATEADD(DAY,-30,CAST(GETDATE() AS DATE)),DATEADD(DAY,365,CAST(GETDATE() AS DATE)),'HOAT_DONG'),
    ('KM50K',N'Giảm trực tiếp 50.000đ','SO_TIEN',50000,500000,50000,DATEADD(DAY,-30,CAST(GETDATE() AS DATE)),DATEADD(DAY,365,CAST(GETDATE() AS DATE)),'HOAT_DONG');
END
GO

MERGE CauHinh AS t
USING (VALUES
 ('VAT_RATE',N'0.08',N'Tỷ lệ VAT'),
 ('POINT_EARN_AMOUNT',N'10000',N'Mỗi 10.000đ được 1 điểm'),
 ('POINT_REDEEM_VALUE',N'1000',N'1 điểm giảm 1.000đ'),
 ('RESTAURANT_NAME',N'Nhà hàng HUST',N'Tên nhà hàng')
) s(Khoa,GiaTri,MoTa)
ON t.Khoa=s.Khoa
WHEN MATCHED THEN UPDATE SET GiaTri=s.GiaTri,MoTa=s.MoTa,NgayCapNhat=SYSDATETIME()
WHEN NOT MATCHED THEN INSERT(Khoa,GiaTri,MoTa) VALUES(s.Khoa,s.GiaTri,s.MoTa);
GO
