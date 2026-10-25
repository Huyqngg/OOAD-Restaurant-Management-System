package vn.hust.restaurant.model;

import java.math.BigDecimal;
import java.time.LocalDate;
public record Promotion(int id, String code, String name, String type, BigDecimal value,
                        BigDecimal minimumAmount, BigDecimal maximumDiscount,
                        LocalDate startDate, LocalDate endDate, boolean active,
                        Integer targetMenuItemId, Integer buyQuantity, Integer getQuantity) {
    @Override public String toString() { return code + " - " + name; }
}
