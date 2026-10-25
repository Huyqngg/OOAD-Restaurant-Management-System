# Hướng dẫn cài đặt đầy đủ

## 1. Cài SQL Server 2022

Chọn chế độ Mixed Mode để có thể sử dụng SQL Login. Ghi nhớ mật khẩu tài khoản `sa`. Cài thêm SSMS 2022 và SQL Server Configuration Manager.

## 2. Khởi tạo cơ sở dữ liệu

Trong SSMS, chạy theo thứ tự:

1. `01_CreateDatabase.sql`
2. `02_CreateTables.sql`
3. `03_IndexesViewsProcedures.sql`
4. `04_SeedData.sql`
5. `05_CreateLoginsPermissions.sql`

Không cần bật SQLCMD Mode khi chạy từng file. File `00_RUN_ALL_SQLCMD.sql` chỉ dùng khi đã bật `Query → SQLCMD Mode`.

## 3. Kiểm tra database

```sql
USE HTQLNhaHang;
SELECT * FROM TaiKhoan;
SELECT * FROM NhanVien;
SELECT * FROM BanAn;
SELECT * FROM MonAn;
SELECT * FROM vw_NguyenLieuSapHet;
```

## 4. Cấu hình ứng dụng

Sửa `src/main/resources/database.properties`:

```properties
db.host=localhost
db.port=1433
db.name=HTQLNhaHang
db.user=restaurant_app
db.password=RestaurantApp@2026
```

Khi các máy khác trong LAN kết nối, `db.host` phải là IPv4 của máy cài SQL Server.

## 5. Chạy bằng Eclipse

- `File → Import → Maven → Existing Maven Projects`.
- Chọn thư mục `HTQLNhaHang`.
- Chọn JRE System Library là JavaSE-17.
- `Maven → Update Project` nếu dependency chưa xuất hiện.
- Run As → Java Application → `vn.hust.restaurant.App`.

## Ghi đè cấu hình bằng biến môi trường

Ứng dụng ưu tiên biến môi trường, sau đó Java system property, cuối cùng mới đọc file `.properties`.
Ví dụ trên Windows PowerShell:

```powershell
$env:DB_HOST="192.168.1.10"
$env:DB_USER="restaurant_app"
$env:DB_PASSWORD="mat-khau-da-doi"
mvn clean package
java -jar target\htql-nha-hang-1.0.0.jar
```

Cách này giúp không phải chia sẻ mật khẩu thật trong `database.properties`.
