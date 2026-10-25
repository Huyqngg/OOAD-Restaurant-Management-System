package vn.hust.restaurant.model;

public record UserSession(int accountId, int employeeId, String username, String fullName, String role) {
    public boolean hasRole(String... roles) {
        for (String r : roles) if (role != null && role.equalsIgnoreCase(r)) return true;
        return false;
    }
}
