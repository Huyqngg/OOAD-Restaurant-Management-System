package vn.hust.restaurant.model;

public record Employee(int id, String code, String fullName, String citizenId, String phone,
                       String email, String address, String role, String workStatus,
                       String username, String accountStatus) {}
