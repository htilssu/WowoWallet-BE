package com.wowo.wowo.domain.payment.valueobjects;

/**
 * Application ID value object
 */
public record ApplicationId(Long value) {
    
    public ApplicationId {
        if (value == null || value <= 0) {
            throw new IllegalArgumentException("Application ID must be positive");
        }
    }
    
    public static ApplicationId of(Long id) {
        return new ApplicationId(id);
    }
    
    @Override
    public String toString() {
        return value.toString();
    }
}