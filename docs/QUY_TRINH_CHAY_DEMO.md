# Quy trình chạy demo hoàn chỉnh

## 1. Chuẩn bị database

1. Mở SSMS 2022 bằng tài khoản có quyền tạo database và login.
2. Bật **Query → SQLCMD Mode**.
3. Mở `database/00_RUN_ALL_SQLCMD.sql` tại đúng thư mục project và chạy.
4. Chạy `database/08_VerifyInstallation.sql`; tất cả kiểm tra phải trả về trạng thái `OK`.
5. Đổi mật khẩu mặc định trong `05_CreateLoginsPermissions.sql` khi triển khai ngoài môi trường demo.

## 2. Chuẩn bị Eclipse

1. Cài JDK 17.
2. Import bằng `File → Import → Maven → Existing Maven Projects`.
3. Sửa `src/main/resources/database.properties` theo IP SQL Server.
4. Chọn `Maven → Update Project`.
5. Chạy `vn.hust.restaurant.App`.

## 3. Kịch bản demo bốn vai trò

### Phục vụ

1. Đăng nhập `phucvu / 123456`.
2. Chọn một bàn trống, tạo order, thêm món và ghi chú.
3. Gửi order xuống bếp.

### Đầu bếp

1. Mở ứng dụng khác hoặc máy khác, đăng nhập `bep / 123456`.
2. Màn hình bếp tự làm mới khoảng 1,5 giây.
3. Chuyển món từ `CHO_LAM` sang `DANG_LAM`, sau đó `DA_XONG`.
4. Khi hoàn thành, hệ thống trừ nguyên liệu theo công thức đúng một lần.

### Phục vụ

1. Làm mới order.
2. Khi tất cả món hoàn thành, chuyển order sang `CHO_THANH_TOAN`.

### Thu ngân

1. Đăng nhập `thungan / 123456`.
2. Chọn order, khách hàng, khuyến mãi và điểm sử dụng.
3. Nhập tiền mặt/chuyển khoản/thẻ rồi thanh toán.
4. In hóa đơn; bàn tự trở về `TRONG`.

### Chủ nhà hàng

1. Đăng nhập `admin / 123456`.
2. Kiểm tra thực đơn, kho, nhân viên, khách hàng và báo cáo.
3. Xuất báo cáo Excel hoặc PDF.

## 4. Demo SSMS nhiều người dùng

- Dùng `restaurant_admin` để chỉnh sửa cấu hình, dữ liệu danh mục và thực hiện bảo trì.
- Dùng `restaurant_app` cho kết nối từ ứng dụng Java.
- Không chia sẻ tài khoản `sa`.
- Có thể chạy các ví dụ an toàn trong `database/09_SampleQueries.sql` để xem/chỉnh sửa dữ liệu.
