# Chia sẻ SQL Server 2022 trong mạng LAN

## Trên máy chủ SQL Server

1. Đặt IP tĩnh hoặc DHCP reservation cho máy chủ.
2. Mở **SQL Server Configuration Manager**.
3. Vào `SQL Server Network Configuration → Protocols for MSSQLSERVER`.
4. Bật `TCP/IP`.
5. Trong `TCP/IP Properties → IP Addresses → IPAll`, đặt `TCP Port = 1433`, xóa `TCP Dynamic Ports`.
6. Khởi động lại dịch vụ SQL Server.
7. Mở Windows Defender Firewall và cho phép inbound TCP 1433.
8. Bật Mixed Mode Authentication trong SSMS nếu đang chỉ dùng Windows Authentication.

## Trên máy trạm

- Sửa `db.host` thành IP máy chủ, ví dụ `192.168.1.10`.
- Kiểm tra kết nối:

```powershell
Test-NetConnection 192.168.1.10 -Port 1433
```

- Có thể đăng nhập SSMS bằng:
  - `restaurant_admin`: quản trị database.
  - `restaurant_app`: tài khoản ứng dụng, chỉ có quyền đọc/ghi và thực thi procedure.

## Nguyên tắc vận hành

Người dùng nghiệp vụ thao tác qua giao diện Java. Chỉ thành viên nhóm hoặc quản trị viên được cấp tài khoản SSMS. Không chia sẻ tài khoản `sa` cho các máy trạm.
