package com.wowo.wowo.contexts.transaction.domain.valueobject

import com.wowo.wowo.shared.domain.ValueObject
import java.util.*

/**
 * Value Object representing Transaction ID
 */
data class TransactionId(val value: UUID = UUID.randomUUID()) : ValueObject {
    override fun toString(): String = value.toString()

    companion object {
        fun fromString(id: String): TransactionId = TransactionId(UUID.fromString(id))
    }
}


