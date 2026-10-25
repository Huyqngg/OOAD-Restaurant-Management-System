package vn.hust.restaurant.model;

public record DiningTable(int id, String code, String name, String area, int capacity, String status) {
    @Override public String toString() { return code + " - " + name + " (" + status + ")"; }
}
