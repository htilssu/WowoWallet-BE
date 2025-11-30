package com.wowo.wowo.contexts.user.domain.valueobject

import com.wowo.wowo.shared.domain.ValueObject

/**
 * Value Object representing hashed password
 */
data class Password(val hashedValue: String) : ValueObject {
    init {
        require(hashedValue.isNotBlank()) { "Password cannot be blank" }
    }

    companion object {
        fun fromRaw(rawPassword: String): Password {
            require(rawPassword.length >= 8) { "Password must be at least 8 characters" }
            require(rawPassword.any { it.isUpperCase() }) { "Password must contain at least one uppercase letter" }
            require(rawPassword.any { it.isLowerCase() }) { "Password must contain at least one lowercase letter" }
            require(rawPassword.any { it.isDigit() }) { "Password must contain at least one digit" }

            // This should use actual password encoder (BCrypt) in infrastructure layer
            return Password(rawPassword) // Placeholder
        }
    }
}

