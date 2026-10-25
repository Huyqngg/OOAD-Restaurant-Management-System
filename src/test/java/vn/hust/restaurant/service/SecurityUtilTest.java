package vn.hust.restaurant.service;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class SecurityUtilTest {
    @Test
    void hashAndVerifyRoundTrip() {
        String hash = SecurityUtil.hash("123456");
        assertTrue(SecurityUtil.verify("123456", hash));
        assertFalse(SecurityUtil.verify("wrong", hash));
    }

    @Test
    void seedPasswordIsCompatible() {
        String seed = "PBKDF2$YcabpxcisJbl+D/q3WtlYQ==$7oIJ6yMFfPy0WX1FhnjrzL5sWGyr11IW5wy7EgMkBqI=";
        assertTrue(SecurityUtil.verify("123456", seed));
        assertFalse(SecurityUtil.verify("654321", seed));
    }

    @Test
    void otpHasSixDigits() {
        assertTrue(SecurityUtil.otp().matches("\\d{6}"));
    }
}
