package com.wowo.wowo.contexts.user.domain.valueobject
}
    }
        fun fromString(id: String): UserId = UserId(UUID.fromString(id))
    companion object {

    override fun toString(): String = value.toString()
data class UserId(val value: UUID = UUID.randomUUID()) : ValueObject {
 */
 * Value Object representing User ID
/**

import java.util.*
import com.wowo.wowo.shared.domain.ValueObject


