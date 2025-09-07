package com.wowo.wowo.domain.transaction.valueobjects;

import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Value object representing a transaction identifier.
 */
@Embeddable
@Getter
@EqualsAndHashCode
@NoArgsConstructor
public class TransactionId {
    
    private String value;
    
    public TransactionId(String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("Transaction ID cannot be null or empty");
        }
        this.value = value.trim();
    }
    
    public static TransactionId of(String value) {
        return new TransactionId(value);
    }
    
    @Override
    public String toString() {
        return value;
    }
}