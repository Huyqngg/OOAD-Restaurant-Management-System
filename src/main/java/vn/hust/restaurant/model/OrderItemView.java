package vn.hust.restaurant.model;

import java.math.BigDecimal;
public record OrderItemView(long id, long orderId, int menuItemId, String menuItemName,
                            int quantity, BigDecimal unitPrice, String note, String kitchenStatus,
                            boolean stockDeducted) {
    public BigDecimal lineTotal() { return unitPrice.multiply(BigDecimal.valueOf(quantity)); }
}
