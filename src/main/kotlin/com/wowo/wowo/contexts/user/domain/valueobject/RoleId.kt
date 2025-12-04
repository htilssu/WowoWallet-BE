package com.wowo.wowo.contexts.user.domain.valueobject

import com.wowo.wowo.shared.domain.ValueObject


/**
 * Value Object representing a Role identifier
 */
data class RoleId(val value: java.util.UUID = java.util.UUID.randomUUID()) : ValueObject {
    
    constructor(value: String) : this(java.util.UUID.fromString(value))
    
    override fun toString(): String = value.toString()
}
