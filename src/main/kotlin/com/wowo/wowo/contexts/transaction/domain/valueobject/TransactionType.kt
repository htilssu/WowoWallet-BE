package com.wowo.wowo.contexts.transaction.domain.valueobject

import com.wowo.wowo.shared.domain.ValueObject

/**
 * Value Object representing transaction type
 */
enum class TransactionType : ValueObject {
    CREDIT,     // Money coming in
    DEBIT,      // Money going out
    TRANSFER,   // Transfer between wallets
    PAYMENT,    // Payment to merchant
    REFUND      // Refund from merchant
}

