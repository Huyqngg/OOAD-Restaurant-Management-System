package vn.hust.restaurant.model;

public record Customer(int id, String code, String fullName, String phone, String email, int points, String status) {
    @Override public String toString() { return fullName + " - " + phone + " (" + points + " điểm)"; }
}
