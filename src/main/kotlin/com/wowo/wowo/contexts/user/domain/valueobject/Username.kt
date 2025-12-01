package com.wowo.wowo.contexts.user.domain.valueobject

import com.wowo.wowo.shared.domain.ValueObject

/**
 * Value Object representing Username
 */
data class Username(val value: String) : ValueObject {
    init {
        require(value.isNotBlank()) { "Username cannot be blank" }
        require(value.length >= 3) { "Username must be at least 3 characters" }
        require(value.length <= 50) { "Username must not exceed 50 characters" }
        require(value.matches(Regex("^[a-zA-Z0-9_.-]+$"))) {
            "Username can only contain letters, numbers, underscore and hyphen"
        }
    }

    override fun toString(): String = value
}

