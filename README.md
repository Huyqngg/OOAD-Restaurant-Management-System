# Hệ thống quản lý nhà hàng — OOAD

Ứng dụng desktop quản lý nhà hàng bằng **Java Swing và SQL Server**, tổ chức theo kiến trúc phân lớp `View → Controller → Service → Repository/JDBC → Database`. Dự án phục vụ các nghiệp vụ từ đặt bàn, gọi món, xử lý tại bếp đến thanh toán, quản lý kho và xem báo cáo.

Mã nguồn Java, SQL, cấu hình và các script được giữ nguyên từ gói dự án. README này bổ sung hướng dẫn sử dụng; thư mục build `target/` được loại khỏi Git theo `.gitignore` có sẵn.

## Công nghệ sử dụng

| Thành phần | Công nghệ/phiên bản trong dự án |
| --- | --- |
| Ngôn ngữ | Java 17, mã nguồn UTF-8 |
| Giao diện | Java Swing, FlatLaf 3.7.1 |
| Quản lý và đóng gói | Maven; Maven Shade Plugin 3.6.0 |
| Cơ sở dữ liệu | Microsoft SQL Server; tài liệu gốc hướng dẫn SQL Server 2022 |
| Kết nối dữ liệu | JDBC, Microsoft JDBC Driver 13.4.0.jre11 |
| Mật khẩu | jBCrypt 0.4; hỗ trợ đọc/lưu PBKDF2 trong mã nguồn |
| Xuất Excel | Apache POI 5.5.1 |
| Xuất PDF | JasperReports 7.0.7 |
| Kiểm thử | JUnit Jupiter 5.12.2 |

Điểm khởi chạy ứng dụng: `vn.hust.restaurant.App`.

## Các chức năng chính

| Nhóm nghiệp vụ | Chức năng trong mã nguồn |
| --- | --- |
| Tài khoản | Đăng nhập, đăng xuất, ghi nhận đăng nhập sai, khóa tạm thời, đặt lại mật khẩu bằng OTP |
| Bàn và đặt bàn | Tìm bàn theo trạng thái, tạo phiếu gọi món, đặt bàn trước, kiểm tra sức chứa và thời gian đặt |
| Gọi món | Thêm món, ghi chú, hủy món theo trạng thái, gửi bếp và chuyển phiếu sang chờ thanh toán |
| Bếp | Danh sách món cần chế biến, chuyển trạng thái, tự làm mới theo cấu hình và trừ nguyên liệu khi hoàn thành |
| Thực đơn | Danh mục món, giá bán và trạng thái phục vụ |
| Kho | Nguyên liệu, công thức món, nhập kho, điều chỉnh tồn và theo dõi nguyên liệu sắp hết |
| Khách hàng | Thông tin khách hàng, điểm tích lũy và sử dụng điểm khi thanh toán |
| Thanh toán | Áp dụng khuyến mãi có trong cơ sở dữ liệu, tính VAT, đổi điểm, thanh toán một hoặc nhiều phương thức, in hóa đơn |
| Báo cáo | Doanh thu, món bán chạy, nhập kho, tồn kho thấp; xuất Excel/PDF |

Chuyển khoản và thẻ được thu ngân xác nhận thủ công bằng thông tin giao dịch. Mã nguồn hiện có không kết nối cổng thanh toán hoặc ngân hàng.

## Vai trò và tài khoản demo

Sau khi chạy `database/04_SeedData.sql`, các tài khoản demo dùng chung mật khẩu **`123456`**.

| Tài khoản | Vai trò | Các màn hình được mở theo vai trò |
| --- | --- | --- |
| `admin` | Chủ nhà hàng | Tổng quan và toàn bộ các phân hệ |
| `phucvu` | Phục vụ | Tổng quan, bàn và gọi món, đặt bàn |
| `bep` | Đầu bếp | Tổng quan, bếp, kho |
| `thungan` | Thu ngân | Tổng quan, thanh toán, khách hàng |

Đây là tài khoản đăng nhập ứng dụng. Chúng khác với các SQL Login `restaurant_app` và `restaurant_admin` dùng để kết nối, quản trị cơ sở dữ liệu.

## Cấu trúc dự án

| Đường dẫn | Nội dung |
| --- | --- |
| `pom.xml` | Dependency, Java 17, cấu hình kiểm thử và đóng gói JAR |
| `src/main/java/vn/hust/restaurant/App.java` | Khởi tạo giao diện đăng nhập |
| `src/main/java/vn/hust/restaurant/config/` | Đọc cấu hình, kết nối JDBC, transaction, giao diện và khởi tạo các thành phần |
| `src/main/java/vn/hust/restaurant/model/` | Các đối tượng dữ liệu và trạng thái nghiệp vụ |
| `src/main/java/vn/hust/restaurant/view/` | Các màn hình Java Swing |
| `src/main/java/vn/hust/restaurant/controller/` | Tiếp nhận thao tác từ giao diện và gọi service |
| `src/main/java/vn/hust/restaurant/service/` | Xử lý nghiệp vụ, bảo mật, OTP, in và xuất báo cáo |
| `src/main/java/vn/hust/restaurant/repository/` | Truy vấn và cập nhật SQL bằng JDBC |
| `src/main/resources/` | Cấu hình ứng dụng và kết nối SQL Server |
| `src/test/java/` | Kiểm thử `SecurityUtilTest` có sẵn |
| `database/` | Script khởi tạo, dữ liệu demo, phân quyền, kiểm tra và truy vấn mẫu |
| `docs/` | Hướng dẫn cài đặt, nghiệp vụ, cơ sở dữ liệu và kịch bản demo |
| `package.bat`, `run.bat` | Các script build/chạy Windows gốc |
| `setup_database.bat` | Script thiết lập SQL Server bằng `sqlcmd` |

## Cài đặt và chạy

### 1. Chuẩn bị môi trường

Tài liệu dự án sử dụng Windows 10/11, JDK 17, Maven 3.9 trở lên, SQL Server 2022 và SQL Server Management Studio (SSMS). Có thể dùng Eclipse hỗ trợ Maven để mở mã nguồn.

Kiểm tra Java và Maven trong terminal:

```bat
java -version
mvn -version
```

SQL Server cần cho phép SQL Server Authentication nếu dùng các SQL Login do script dự án tạo. Với nhiều máy trong LAN, các máy chạy ứng dụng kết nối tới cùng máy chủ SQL Server.

### 2. Tạo cơ sở dữ liệu

Trong SSMS, dùng tài khoản có quyền tạo database và login, rồi chạy lần lượt:

| Thứ tự | Tệp | Công việc |
| --- | --- | --- |
| 1 | `database/01_CreateDatabase.sql` | Tạo database `HTQLNhaHang` và cấu hình isolation |
| 2 | `database/02_CreateTables.sql` | Tạo bảng, khóa và ràng buộc |
| 3 | `database/03_IndexesViewsProcedures.sql` | Tạo chỉ mục, view và stored procedure |
| 4 | `database/04_SeedData.sql` | Thêm dữ liệu và tài khoản demo |
| 5 | `database/05_CreateLoginsPermissions.sql` | Tạo SQL Login và cấp quyền |
| 6 | `database/08_VerifyInstallation.sql` | Kiểm tra cài đặt |

Chạy từng tệp theo cách này không cần bật SQLCMD Mode. Nếu dùng `00_RUN_ALL_SQLCMD.sql`, cần bật SQLCMD Mode và bảo đảm các đường dẫn tương đối trỏ đúng tới thư mục `database/`.

Trên Windows có `sqlcmd`, có thể dùng `setup_database.bat` từ thư mục gốc của dự án. Script hỏi tên máy chủ, tài khoản và mật khẩu SQL, sau đó chạy các bước khởi tạo và kiểm tra.

`06_BackupRestoreExamples.sql` chứa ví dụ sao lưu/khôi phục cần điều chỉnh đường dẫn. **`07_ResetDatabase.sql` xóa database `HTQLNhaHang`**; tệp này dành cho việc đặt lại dữ liệu, không thuộc quy trình cài đặt thông thường. `09_SampleQueries.sql` chứa các truy vấn tham khảo.

### 3. Cấu hình kết nối SQL Server

Mở `src/main/resources/database.properties` và điều chỉnh theo môi trường chạy. Có thể tham khảo `database.properties.example` trong cùng thư mục.

```properties
db.host=localhost
db.port=1433
db.name=HTQLNhaHang
db.user=restaurant_app
db.password=MAT_KHAU_SQL_CUA_BAN
db.encrypt=true
db.trustServerCertificate=true
db.loginTimeoutSeconds=8
```

`db.user` và `db.password` phải khớp với SQL Login đã tạo. `localhost` dùng khi ứng dụng và SQL Server nằm trên cùng máy; khi chạy qua LAN, đặt `db.host` thành địa chỉ máy chủ. Tệp cấu hình gốc trong dự án giữ địa chỉ của môi trường phát triển, nên cần kiểm tra trước khi chạy trên máy khác.

Cấu hình được đọc theo thứ tự ưu tiên:

1. Biến môi trường, ví dụ `DB_HOST`, `DB_USER`, `DB_PASSWORD`.
2. Java system property, ví dụ `-Ddb.host=localhost`.
3. Giá trị trong tệp `.properties`.

Ví dụ trên PowerShell:

```powershell
$env:DB_HOST = "localhost"
$env:DB_USER = "restaurant_app"
$env:DB_PASSWORD = "MAT_KHAU_SQL_CUA_BAN"
java -jar target/htql-nha-hang-1.0.0.jar
```

Các tệp cấu hình vẫn phải có trong classpath vì `AppConfig` nạp chúng khi khởi tạo. Mã nguồn hiện không tự nạp `database.local.properties`; dù tên tệp này có trong `.gitignore`, việc chỉ tạo tệp đó chưa đủ để ghi đè cấu hình.

### 4. Build và chạy bằng Maven

Từ thư mục chứa `pom.xml`:

```bat
mvn clean test package
java -jar target/htql-nha-hang-1.0.0.jar
```

Maven Shade Plugin đóng gói dependency vào `target/htql-nha-hang-1.0.0.jar`.

Các script `.bat` gốc được giữ trong kho. Khi cần theo dõi trực tiếp kết quả build và lỗi khởi động, có thể chạy các lệnh Maven/Java ở trên.

### 5. Chạy bằng Eclipse

1. Chọn **File → Import → Maven → Existing Maven Projects**.
2. Chọn thư mục chứa `pom.xml`.
3. Kiểm tra project dùng **JavaSE-17**.
4. Chọn **Maven → Update Project** nếu cần tải lại dependency.
5. Chạy `vn.hust.restaurant.App` bằng **Run As → Java Application**.

## Cấu hình ứng dụng

Tệp `src/main/resources/application.properties` chứa các giá trị demo sau:

| Khóa | Giá trị gốc | Ý nghĩa |
| --- | --- | --- |
| `vat.rate` | `0.08` | Tỷ lệ VAT dùng trong phép tính của ứng dụng |
| `point.earn.amount` | `10000` | Số tiền thanh toán tương ứng một điểm tích lũy |
| `point.redeem.value` | `1000` | Giá trị quy đổi của một điểm |
| `kitchen.refresh.ms` | `1500` | Khoảng tự làm mới màn hình bếp, tính bằng mili giây |
| `otp.mode` | `SIMULATED` | Chế độ OTP mô phỏng |
| `otp.expiry.minutes` | `5` | Thời hạn OTP, tính bằng phút |
| `app.devMode` | `true` | Cho phép hiển thị OTP mô phỏng trong luồng khôi phục mật khẩu |

Thông tin tên, địa chỉ và số điện thoại nhà hàng cũng nằm trong tệp này.

Để dùng chức năng gửi OTP qua email có sẵn, đặt `otp.mode=EMAIL` và cấu hình `smtp.host`, `smtp.port`, `smtp.username`, `smtp.appPassword`, `smtp.fromName`. Các trường tài khoản SMTP trong gói gốc đang để trống. Mật khẩu và tài khoản demo cần được thay đổi trên môi trường triển khai thực tế.

## Luồng demo cơ bản

1. **Phục vụ:** đăng nhập `phucvu`, chọn bàn trống, tạo phiếu, thêm món và gửi bếp.
2. **Đầu bếp:** đăng nhập `bep` ở cửa sổ hoặc máy khác, chuyển món từ `CHO_LAM` sang `DANG_LAM`, rồi `DA_XONG`.
3. **Phục vụ:** làm mới phiếu và chuyển sang chờ thanh toán sau khi các món đã hoàn thành hoặc bị hủy.
4. **Thu ngân:** đăng nhập `thungan`, chọn phiếu, khách hàng, khuyến mãi và điểm sử dụng; nhập các phương thức thanh toán, xác nhận và in hóa đơn.
5. **Chủ nhà hàng:** đăng nhập `admin`, xem báo cáo, kiểm tra kho và xuất dữ liệu.

Chi tiết các bước xem tại [Quy trình chạy demo](docs/QUY_TRINH_CHAY_DEMO.md).

## Quy tắc nghiệp vụ

- Một bàn có tối đa một phiếu gọi món đang hoạt động.
- Phiếu chưa gửi bếp có thể hủy; món chỉ được hủy trực tiếp khi còn ở trạng thái `CHO_LAM`.
- Món hoàn thành được trừ nguyên liệu theo công thức, sử dụng cờ `DaTruKho` để ghi nhận việc đã trừ.
- Khi không đủ nguyên liệu, thao tác hoàn thành món bị từ chối.
- Một hóa đơn áp dụng tối đa một khuyến mãi; các loại có trong mã nguồn gồm giảm phần trăm, giảm số tiền, giảm theo món và mua X tặng Y.
- Thanh toán có thể kết hợp tiền mặt, chuyển khoản và thẻ.
- Khách vãng lai được thanh toán nhưng không tích hoặc đổi điểm.
- Sau khi thanh toán thành công, phiếu chuyển sang `DA_THANH_TOAN` và bàn trở về `TRONG`.

Các repository sử dụng transaction và khóa bản ghi cho một số thao tác như tạo phiếu, hoàn thành món và thanh toán. Kịch bản nhiều máy trạm cần được kiểm tra với SQL Server thực tế.

## Kiểm thử và xuất báo cáo

`SecurityUtilTest` có ba kiểm thử có sẵn: hash/verify mật khẩu, tương thích mật khẩu seed và định dạng OTP sáu chữ số. Chạy bằng:

```bat
mvn test
```

Các kiểm thử này tập trung vào tiện ích bảo mật; không thay thế kiểm thử giao diện, SQL Server, in hóa đơn, SMTP hay nhiều máy trạm.

Xuất Excel dùng Apache POI; khi thiếu thư viện này, mã nguồn có nhánh xuất CSV. Xuất PDF dùng JasperReports. Cần build với dependency trong `pom.xml` và kiểm tra các chức năng xuất/in trong môi trường chạy.

## Tài liệu tham khảo trong dự án

- [Hướng dẫn cài đặt](docs/HUONG_DAN_CAI_DAT.md)
- [Hướng dẫn Eclipse](docs/HUONG_DAN_ECLIPSE.md)
- [Kết nối SQL Server trong LAN](docs/HUONG_DAN_SQL_SERVER_LAN.md)
- [Sơ đồ và cấu trúc cơ sở dữ liệu](docs/SO_DO_DATABASE.md)
- [Nghiệp vụ và quy tắc](docs/NGHIEP_VU_VA_QUY_TAC.md)
- [Kịch bản kiểm thử gợi ý](docs/KIEM_THU_GOI_Y.md)
- [Tài khoản demo](docs/TAI_KHOAN_DEMO.md)
