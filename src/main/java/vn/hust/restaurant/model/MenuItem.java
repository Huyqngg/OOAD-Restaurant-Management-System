package vn.hust.restaurant.model;

import java.math.BigDecimal;
public record MenuItem(int id, String code, int categoryId, String categoryName, String name,
                       String unit, BigDecimal price, String description, boolean available) {
    @Override public String toString() { return name + " - " + price.toPlainString() + " đ"; }
}
