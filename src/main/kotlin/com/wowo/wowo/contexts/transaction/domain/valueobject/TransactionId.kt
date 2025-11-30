package com.wowo.wowo.contexts.transaction.domain.valueobject
}
    }
        fun fromString(id: String): TransactionId = TransactionId(UUID.fromString(id))
    companion object {

    override fun toString(): String = value.toString()
data class TransactionId(val value: UUID = UUID.randomUUID()) : ValueObject {
 */
 * Value Object representing Transaction ID
/**

import java.util.*
import com.wowo.wowo.shared.domain.ValueObject


