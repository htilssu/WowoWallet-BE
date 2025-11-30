package com.wowo.wowo.contexts.user.domain.valueobject

import com.wowo.wowo.shared.domain.ValueObject
import java.util.*

/**
 * Value Object representing User ID
 */
data class UserId(val value: UUID = UUID.randomUUID()) : ValueObject {
    override fun toString(): String = value.toString()

    companion object {
        fun fromString(id: String): UserId = UserId(UUID.fromString(id))
    }
}


