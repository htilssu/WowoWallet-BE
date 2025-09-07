package com.wowo.wowo.domain.shared.valueobjects;

import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * Value object representing a monetary amount.
 * Encapsulates business rules for money handling.
 */
@Embeddable
@Getter
@EqualsAndHashCode
@NoArgsConstructor
public class Money {
    
    private Long amount;
    private String currency;
    
    public Money(Long amount, String currency) {
        if (amount == null) {
            throw new IllegalArgumentException("Amount cannot be null");
        }
        if (currency == null || currency.trim().isEmpty()) {
            throw new IllegalArgumentException("Currency cannot be null or empty");
        }
        this.amount = amount;
        this.currency = currency.toUpperCase();
    }
    
    public static Money zero(String currency) {
        return new Money(0L, currency);
    }
    
    public static Money vnd(Long amount) {
        return new Money(amount, "VND");
    }
    
    public Money add(Money other) {
        if (!this.currency.equals(other.currency)) {
            throw new IllegalArgumentException("Cannot add money with different currencies");
        }
        return new Money(this.amount + other.amount, this.currency);
    }
    
    public Money subtract(Money other) {
        if (!this.currency.equals(other.currency)) {
            throw new IllegalArgumentException("Cannot subtract money with different currencies");
        }
        return new Money(this.amount - other.amount, this.currency);
    }
    
    public boolean isGreaterThan(Money other) {
        if (!this.currency.equals(other.currency)) {
            throw new IllegalArgumentException("Cannot compare money with different currencies");
        }
        return this.amount > other.amount;
    }
    
    public boolean isGreaterThanOrEqual(Money other) {
        if (!this.currency.equals(other.currency)) {
            throw new IllegalArgumentException("Cannot compare money with different currencies");
        }
        return this.amount >= other.amount;
    }
    
    public boolean isNegative() {
        return amount < 0;
    }
    
    public boolean isPositive() {
        return amount > 0;
    }
    
    public boolean isZero() {
        return amount.equals(0L);
    }
    
    @Override
    public String toString() {
        return String.format("%d %s", amount, currency);
    }
}