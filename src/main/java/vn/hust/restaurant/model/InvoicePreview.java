package vn.hust.restaurant.model;

import java.math.BigDecimal;
import java.util.List;
public record InvoicePreview(long orderId, String orderCode, String tableCode, List<OrderItemView> items,
                             BigDecimal subtotal, BigDecimal promotionDiscount, BigDecimal vat,
                             int pointsUsed, BigDecimal pointDiscount, BigDecimal total,
                             int pointsEarned, Integer customerId, Integer promotionId) {}
