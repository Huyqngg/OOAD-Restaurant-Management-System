# Báo cáo kiểm tra gói mã nguồn

## Kiểm tra đã thực hiện trong môi trường tạo project

- Biên dịch toàn bộ mã nguồn `src/main/java` bằng `javac --release 17 -encoding UTF-8`: **đạt**.
- Kiểm tra đọc file cấu hình UTF-8: **đạt**.
- Kiểm tra hash/verify mật khẩu PBKDF2 seed `123456`: **đạt**.
- Kiểm tra hash mới và verify vòng kín: **đạt**.
- Kiểm tra OTP gồm 6 chữ số: **đạt**.
- Kiểm tra cấu trúc Eclipse/Maven, main class và resource: **đạt ở mức tĩnh**.
- Kiểm tra đối chiếu tên bảng/cột chính giữa SQL và repository JDBC: **đã thực hiện**.

## Kiểm tra cần chạy trên máy Windows của nhóm

Môi trường tạo project không có Microsoft SQL Server và Maven, nên chưa thể thực hiện kiểm thử tích hợp trực tiếp. Sau khi cài đặt trên máy của nhóm, cần chạy:

1. `database/08_VerifyInstallation.sql` trong SSMS.
2. `mvn clean test package`.
3. Kịch bản trong `docs/QUY_TRINH_CHAY_DEMO.md` với ít nhất hai máy/tiến trình đồng thời.
4. Kiểm tra in hóa đơn bằng máy in thực tế.
5. Kiểm tra gửi Gmail OTP sau khi cấu hình App Password.
6. Kiểm tra xuất Excel/PDF sau khi Maven tải đầy đủ dependency.

## Tiêu chí nghiệm thu khuyến nghị

- Phục vụ gửi món và màn hình bếp nhìn thấy thay đổi trong dưới 2 giây.
- Không thể tạo hai order đang hoạt động trên cùng một bàn.
- Không thể thanh toán cùng một order hai lần.
- Hoàn thành món chỉ trừ kho đúng một lần.
- Transaction phải rollback khi không đủ nguyên liệu hoặc điểm khách hàng thay đổi đồng thời.
- Sau thanh toán, order thành `DA_THANH_TOAN` và bàn trở về `TRONG`.
