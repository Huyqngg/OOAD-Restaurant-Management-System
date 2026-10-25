USE HTQLNhaHang;
GO

-- 1. Xem sơ đồ bàn
SELECT MaBanCode,TenBan,KhuVuc,SucChua,TrangThai
FROM BanAn ORDER BY KhuVuc,MaBanCode;

-- 2. Xem món đang phục vụ
SELECT m.MaMonCode,m.TenMon,d.TenDanhMuc,m.DonViTinh,m.DonGia,m.TrangThaiPhucVu
FROM MonAn m JOIN DanhMucMon d ON d.MaDanhMucMon=m.MaDanhMucMon
ORDER BY d.TenDanhMuc,m.TenMon;

-- 3. Chỉnh giá một món bằng mã nghiệp vụ
-- UPDATE MonAn SET DonGia=175000 WHERE MaMonCode='M003';

-- 4. Đánh dấu hết món / mở bán lại
-- UPDATE MonAn SET TrangThaiPhucVu='HET_MON' WHERE MaMonCode='M003';
-- UPDATE MonAn SET TrangThaiPhucVu='CON_MON' WHERE MaMonCode='M003';

-- 5. Xem hàng đợi bếp
SELECT * FROM vw_HangDoiBep ORDER BY ThoiGianGuiBep,MaCTGoiMon;

-- 6. Xem tồn kho và cảnh báo
SELECT n.MaNLCode,n.TenNL,n.DonViTinh,n.SoLuongTon,n.DinhMucToiThieu,n.DonGiaNhapGanNhat,
       CASE WHEN n.SoLuongTon<=n.DinhMucToiThieu THEN N'Sắp hết' ELSE N'Đủ' END AS CanhBao
FROM NguyenLieu n ORDER BY CanhBao DESC,n.TenNL;

-- 7. Điều chỉnh tồn kho có ghi lịch sử (khuyến nghị thao tác qua ứng dụng)
-- BEGIN TRAN;
-- UPDATE NguyenLieu SET SoLuongTon=SoLuongTon+5 WHERE MaNLCode='NL001';
-- INSERT DieuChinhKho(MaNL,MaNhanVien,SoLuongThayDoi,LyDo)
-- SELECT MaNL,(SELECT MaNhanVien FROM NhanVien WHERE MaNhanVienCode='NV001'),5,N'Điều chỉnh từ SSMS'
-- FROM NguyenLieu WHERE MaNLCode='NL001';
-- COMMIT;

-- 8. Doanh thu theo ngày
SELECT * FROM vw_DoanhThuNgay ORDER BY Ngay DESC;

-- 9. Danh sách hóa đơn và tiền thừa
SELECT h.MaHoaDonCode,h.NgayGioLap,b.MaBanCode,h.TongThanhToan,h.TienKhachTra,
       h.TienKhachTra-h.TongThanhToan AS TienThua,h.HinhThucThanhToan
FROM HoaDon h
JOIN PhieuGoiMon p ON p.MaPhieu=h.MaPhieu
JOIN BanAn b ON b.MaBan=p.MaBan
ORDER BY h.NgayGioLap DESC;

-- 10. Không xóa trực tiếp dữ liệu đã phát sinh hóa đơn. Ưu tiên đổi trạng thái.
GO
