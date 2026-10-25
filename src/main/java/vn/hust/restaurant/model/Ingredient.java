package vn.hust.restaurant.model;

import java.math.BigDecimal;
public record Ingredient(int id, String code, int categoryId, String categoryName, String name,
                         String unit, BigDecimal stock, BigDecimal minimumStock, BigDecimal lastCost,
                         String status) {
    public boolean lowStock() { return stock.compareTo(minimumStock) <= 0; }
}
