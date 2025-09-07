package com.wowo.wowo.domain.transaction.valueobjects;

import lombok.Getter;

/**
 * Enumeration representing the type of transaction flow.
 */
@Getter
public enum TransactionType {
    TRANSFER_MONEY("Transfer Money"),
    TOP_UP("Top Up"),
    WITHDRAW("Withdraw"),
    PAYMENT("Payment"),
    REFUND("Refund"),
    DEPOSIT("Deposit");
    
    private final String description;
    
    TransactionType(String description) {
        this.description = description;
    }
}