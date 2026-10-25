package vn.hust.restaurant.model;

import java.math.BigDecimal;
public record PaymentPart(String method, BigDecimal amount, String transactionCode) {}
