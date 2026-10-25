# Kịch bản kiểm thử gợi ý

1. Đăng nhập đúng/sai; thử sai 5 lần để kiểm tra khóa 15 phút.
2. Yêu cầu OTP, nhập OTP sai/hết hạn và đặt lại mật khẩu.
3. Tạo order trên một bàn; thử tạo order thứ hai trên cùng bàn.
4. Thêm món hết hàng và kiểm tra hệ thống từ chối.
5. Gửi order xuống bếp; xác nhận màn hình bếp nhận trong khoảng 1,5 giây.
6. Hoàn thành món; kiểm tra tồn nguyên liệu giảm theo công thức.
7. Thử hoàn thành khi nguyên liệu không đủ; transaction phải rollback.
8. Đặt bàn vượt sức chứa hoặc trùng khung giờ.
9. Thanh toán bằng tiền mặt, chuyển khoản và kết hợp.
10. Dùng điểm vượt quá số điểm khách có.
11. Áp dụng khuyến mãi chưa đủ điều kiện tối thiểu.
12. Hai máy cùng thanh toán một order; chỉ một giao dịch thành công.
13. Nhập kho và điều chỉnh khiến tồn âm; hệ thống phải từ chối.
14. Xuất báo cáo Excel/PDF và in hóa đơn.
