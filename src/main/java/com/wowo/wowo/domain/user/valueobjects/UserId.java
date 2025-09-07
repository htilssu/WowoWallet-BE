package com.wowo.wowo.domain.user.valueobjects;

import java.util.UUID;

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
    
    public static UserId generate() {
        return new UserId(UUID.randomUUID().toString().replace("-", ""));
    }
    
    @Override
    public String toString() {
        return value;
    }
}