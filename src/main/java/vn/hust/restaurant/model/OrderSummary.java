package vn.hust.restaurant.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
public record OrderSummary(long id, String code, int tableId, String tableCode, String waiterName,
                           LocalDateTime createdAt, String status, BigDecimal total) {
    @Override public String toString() { return code + " - " + tableCode + " - " + total.toPlainString() + " đ"; }
}
