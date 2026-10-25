# Danh sách toàn bộ file Java

Tổng số file Java pha triển khai chính: **77**.

Mã nguồn được tổ chức theo kiến trúc `View → Controller → Service → Repository/JDBC → SQL Server`.

## Điểm khởi động ứng dụng

- `src/main/java/vn/hust/restaurant/App.java`

## Cấu hình và khởi tạo hệ thống

- `src/main/java/vn/hust/restaurant/config/AppConfig.java`
- `src/main/java/vn/hust/restaurant/config/AppContext.java`
- `src/main/java/vn/hust/restaurant/config/Database.java`
- `src/main/java/vn/hust/restaurant/config/Theme.java`

## Mô hình dữ liệu và DTO

- `src/main/java/vn/hust/restaurant/model/Customer.java`
- `src/main/java/vn/hust/restaurant/model/DashboardStats.java`
- `src/main/java/vn/hust/restaurant/model/DiningTable.java`
- `src/main/java/vn/hust/restaurant/model/DomainEnums.java`
- `src/main/java/vn/hust/restaurant/model/Employee.java`
- `src/main/java/vn/hust/restaurant/model/Ingredient.java`
- `src/main/java/vn/hust/restaurant/model/InvoicePreview.java`
- `src/main/java/vn/hust/restaurant/model/MenuItem.java`
- `src/main/java/vn/hust/restaurant/model/OrderItemView.java`
- `src/main/java/vn/hust/restaurant/model/OrderSummary.java`
- `src/main/java/vn/hust/restaurant/model/PaymentPart.java`
- `src/main/java/vn/hust/restaurant/model/Promotion.java`
- `src/main/java/vn/hust/restaurant/model/ReportTable.java`
- `src/main/java/vn/hust/restaurant/model/Reservation.java`
- `src/main/java/vn/hust/restaurant/model/UserSession.java`

## Truy cập dữ liệu JDBC

- `src/main/java/vn/hust/restaurant/repository/AccountRepository.java`
- `src/main/java/vn/hust/restaurant/repository/AuditRepository.java`
- `src/main/java/vn/hust/restaurant/repository/CustomerRepository.java`
- `src/main/java/vn/hust/restaurant/repository/EmployeeRepository.java`
- `src/main/java/vn/hust/restaurant/repository/InventoryRepository.java`
- `src/main/java/vn/hust/restaurant/repository/Jdbc.java`
- `src/main/java/vn/hust/restaurant/repository/KitchenRepository.java`
- `src/main/java/vn/hust/restaurant/repository/MenuRepository.java`
- `src/main/java/vn/hust/restaurant/repository/OrderRepository.java`
- `src/main/java/vn/hust/restaurant/repository/PaymentRepository.java`
- `src/main/java/vn/hust/restaurant/repository/PromotionRepository.java`
- `src/main/java/vn/hust/restaurant/repository/ReportRepository.java`
- `src/main/java/vn/hust/restaurant/repository/ReservationRepository.java`
- `src/main/java/vn/hust/restaurant/repository/TableRepository.java`

## Xử lý nghiệp vụ

- `src/main/java/vn/hust/restaurant/service/AuthService.java`
- `src/main/java/vn/hust/restaurant/service/CustomerService.java`
- `src/main/java/vn/hust/restaurant/service/EmployeeService.java`
- `src/main/java/vn/hust/restaurant/service/ExportService.java`
- `src/main/java/vn/hust/restaurant/service/InventoryService.java`
- `src/main/java/vn/hust/restaurant/service/InvoicePrintService.java`
- `src/main/java/vn/hust/restaurant/service/KitchenService.java`
- `src/main/java/vn/hust/restaurant/service/MenuService.java`
- `src/main/java/vn/hust/restaurant/service/OrderService.java`
- `src/main/java/vn/hust/restaurant/service/OtpService.java`
- `src/main/java/vn/hust/restaurant/service/PaymentService.java`
- `src/main/java/vn/hust/restaurant/service/ReportService.java`
- `src/main/java/vn/hust/restaurant/service/ReservationService.java`
- `src/main/java/vn/hust/restaurant/service/SecurityUtil.java`
- `src/main/java/vn/hust/restaurant/service/SimulatedOtpService.java`
- `src/main/java/vn/hust/restaurant/service/SmtpOtpService.java`
- `src/main/java/vn/hust/restaurant/service/TableService.java`

## Điều phối giao diện – nghiệp vụ

- `src/main/java/vn/hust/restaurant/controller/AuthController.java`
- `src/main/java/vn/hust/restaurant/controller/CustomerController.java`
- `src/main/java/vn/hust/restaurant/controller/EmployeeController.java`
- `src/main/java/vn/hust/restaurant/controller/InventoryController.java`
- `src/main/java/vn/hust/restaurant/controller/KitchenController.java`
- `src/main/java/vn/hust/restaurant/controller/MenuController.java`
- `src/main/java/vn/hust/restaurant/controller/OrderController.java`
- `src/main/java/vn/hust/restaurant/controller/PaymentController.java`
- `src/main/java/vn/hust/restaurant/controller/ReportController.java`
- `src/main/java/vn/hust/restaurant/controller/ReservationController.java`
- `src/main/java/vn/hust/restaurant/controller/TableController.java`

## Giao diện Java Swing

- `src/main/java/vn/hust/restaurant/view/CustomerPanel.java`
- `src/main/java/vn/hust/restaurant/view/DashboardPanel.java`
- `src/main/java/vn/hust/restaurant/view/EmployeePanel.java`
- `src/main/java/vn/hust/restaurant/view/ForgotPasswordDialog.java`
- `src/main/java/vn/hust/restaurant/view/InventoryPanel.java`
- `src/main/java/vn/hust/restaurant/view/KitchenPanel.java`
- `src/main/java/vn/hust/restaurant/view/LoginFrame.java`
- `src/main/java/vn/hust/restaurant/view/MainFrame.java`
- `src/main/java/vn/hust/restaurant/view/MenuPanel.java`
- `src/main/java/vn/hust/restaurant/view/PaymentPanel.java`
- `src/main/java/vn/hust/restaurant/view/Refreshable.java`
- `src/main/java/vn/hust/restaurant/view/ReportPanel.java`
- `src/main/java/vn/hust/restaurant/view/ReservationPanel.java`
- `src/main/java/vn/hust/restaurant/view/TableOrderPanel.java`
- `src/main/java/vn/hust/restaurant/view/Ui.java`

## File kiểm thử

- `src/test/java/vn/hust/restaurant/service/SecurityUtilTest.java`

## Điểm chạy chính

- Main class: `vn.hust.restaurant.App`
- Lệnh Maven: `mvn clean test package`
- JAR sau khi build: `target/htql-nha-hang-1.0.0.jar`
