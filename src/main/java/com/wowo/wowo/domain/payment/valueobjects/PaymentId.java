package com.wowo.wowo.domain.payment.valueobjects;

/**
 * Payment ID value object
 */
public record PaymentId(Long value) {
    
    public PaymentId {
        if (value == null || value <= 0) {
            throw new IllegalArgumentException("Payment ID must be positive");
        }
    }
    
    public static PaymentId of(Long id) {
        return new PaymentId(id);
    }
    
    @Override
    public String toString() {
        return value.toString();
    }
}