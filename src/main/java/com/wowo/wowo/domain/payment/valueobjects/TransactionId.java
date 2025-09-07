package com.wowo.wowo.domain.payment.valueobjects;

/**
 * Transaction ID value object
 */
public record TransactionId(String value) {
    
    public TransactionId {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("Transaction ID cannot be null or empty");
        }
    }
    
    public static TransactionId of(String id) {
        return new TransactionId(id.trim());
    }
    
    @Override
    public String toString() {
        return value;
    }
}