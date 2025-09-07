package com.wowo.wowo.domain.user.valueobjects;

/**
 * User ID value object
 */
public record UserId(String value) {
    
    public UserId {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("User ID cannot be null or empty");
        }
    }
    
    public static UserId of(String id) {
        return new UserId(id.trim());
    }
    
    @Override
    public String toString() {
        return value;
    }
}