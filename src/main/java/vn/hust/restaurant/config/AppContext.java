package vn.hust.restaurant.config;

import vn.hust.restaurant.controller.*;
import vn.hust.restaurant.repository.*;
import vn.hust.restaurant.service.*;

public class AppContext {
    public final AuthController auth;
    public final TableController tables;
    public final ReservationController reservations;
    public final MenuController menu;
    public final OrderController orders;
    public final KitchenController kitchen;
    public final CustomerController customers;
    public final EmployeeController employees;
    public final InventoryController inventory;
    public final PaymentController payments;
    public final ReportController reports;
    public final ExportService exporter = new ExportService();
    public final InvoicePrintService printer = new InvoicePrintService();

    public AppContext() {
        AccountRepository accountRepo = new AccountRepository();
        TableRepository tableRepo = new TableRepository();
        ReservationRepository reservationRepo = new ReservationRepository();
        MenuRepository menuRepo = new MenuRepository();
        OrderRepository orderRepo = new OrderRepository();
        KitchenRepository kitchenRepo = new KitchenRepository();
        CustomerRepository customerRepo = new CustomerRepository();
        EmployeeRepository employeeRepo = new EmployeeRepository();
        InventoryRepository inventoryRepo = new InventoryRepository();
        PromotionRepository promotionRepo = new PromotionRepository();
        PaymentRepository paymentRepo = new PaymentRepository();
        ReportRepository reportRepo = new ReportRepository();

        auth = new AuthController(new AuthService(accountRepo));
        tables = new TableController(new TableService(tableRepo));
        reservations = new ReservationController(new ReservationService(reservationRepo));
        menu = new MenuController(new MenuService(menuRepo));
        orders = new OrderController(new OrderService(orderRepo));
        kitchen = new KitchenController(new KitchenService(kitchenRepo));
        customers = new CustomerController(new CustomerService(customerRepo));
        employees = new EmployeeController(new EmployeeService(employeeRepo));
        inventory = new InventoryController(new InventoryService(inventoryRepo));
        payments = new PaymentController(new PaymentService(orderRepo, promotionRepo, paymentRepo));
        reports = new ReportController(new ReportService(reportRepo));
    }
}
