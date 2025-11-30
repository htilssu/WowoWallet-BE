package com.wowo.wowo.contexts.transaction.domain.valueobject

import com.wowo.wowo.shared.domain.ValueObject

/**
 * Value Object representing transaction status
 */
enum class TransactionStatus : ValueObject {
    PENDING,
    PROCESSING,
    COMPLETED,
    FAILED,
    CANCELLED,
    REFUNDED
}

