package vn.hust.restaurant.model;

import java.time.LocalDateTime;
public record Reservation(long id, int tableId, String tableCode, Integer customerId, int employeeId,
                          String guestName, String phone, LocalDateTime reservedAt, int partySize,
                          String status, String note) {}
