# Hướng dẫn sử dụng Eclipse

1. Cài JDK 17 và Eclipse IDE for Java Developers.
2. Chọn `File → Import → Maven → Existing Maven Projects`.
3. Chọn thư mục chứa `pom.xml`.
4. Kiểm tra `Project → Properties → Java Build Path` dùng JavaSE-17.
5. Nhấn chuột phải project → `Maven → Update Project`.
6. Chạy file `src/main/java/vn/hust/restaurant/App.java`.

## Cấu trúc package

- `config`: cấu hình, kết nối SQL Server, khởi tạo phụ thuộc.
- `model`: các đối tượng và DTO nghiệp vụ.
- `repository`: câu lệnh JDBC và transaction.
- `service`: kiểm tra nghiệp vụ, tính tiền, OTP, báo cáo.
- `controller`: điều phối giữa View và Service.
- `view`: Java Swing và phân quyền giao diện.

## Lỗi thường gặp

- `No suitable driver`: Maven chưa tải `mssql-jdbc`; Update Project.
- `Login failed for user restaurant_app`: chưa chạy file quyền hoặc sai mật khẩu.
- `Connection refused`: SQL Server chưa bật TCP/IP/cổng 1433.
- Chữ tiếng Việt lỗi: giữ encoding project là UTF-8.
