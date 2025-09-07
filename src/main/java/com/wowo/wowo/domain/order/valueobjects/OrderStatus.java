package com.wowo.wowo.domain.order.valueobjects;

/**
 * Order Status enumeration
 * Represents the lifecycle states of an order
 */
public enum OrderStatus {
    PENDING,      // Order created, awaiting payment
    SUCCESS,      // Payment completed successfully
    CANCELLED,    // Order cancelled before payment
    FAILED,       // Payment failed
    REFUNDED      // Order was refunded after successful payment
}