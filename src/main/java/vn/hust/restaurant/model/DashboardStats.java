package vn.hust.restaurant.model;

import java.math.BigDecimal;
public record DashboardStats(BigDecimal todayRevenue, int todayInvoices, int occupiedTables,
                             int waitingKitchenItems, int lowStockItems) {}
