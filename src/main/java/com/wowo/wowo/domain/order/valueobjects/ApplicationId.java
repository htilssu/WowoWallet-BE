package com.wowo.wowo.domain.order.valueobjects;

import lombok.Value;

/**
 * Application ID value object
 */
@Value
public class ApplicationId {
    Long value;
    
    public static ApplicationId of(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Application ID cannot be null");
        }
        return new ApplicationId(id);
    }
    
    public static ApplicationId of(String id) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("Application ID cannot be null or empty");
        }
        try {
            return new ApplicationId(Long.valueOf(id.trim()));
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid Application ID format: " + id);
        }
    }
}