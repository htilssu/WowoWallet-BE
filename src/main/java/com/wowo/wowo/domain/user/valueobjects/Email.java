package com.wowo.wowo.domain.user.valueobjects;

/**
 * Email value object with validation
 */
public record Email(String value) {
    
    public Email {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("Email cannot be null or empty");
        }
        if (!isValidEmail(value.trim())) {
            throw new IllegalArgumentException("Invalid email format");
        }
    }
    
    public static Email of(String email) {
        return new Email(email.trim().toLowerCase());
    }
    
    private static boolean isValidEmail(String email) {
        return email.contains("@") && email.contains(".");
    }
    
    @Override
    public String toString() {
        return value;
    }
}