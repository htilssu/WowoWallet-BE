package com.wowo.wowo.shared.valueobject

import com.wowo.wowo.shared.domain.ValueObject

/**
 * Value Object representing an email address
 */
data class Email(val value: String) : ValueObject {
    init {
        require(value.isNotBlank()) { "Email cannot be blank" }
        require(isValidEmail(value)) { "Invalid email format: $value" }
    }

    private fun isValidEmail(email: String): Boolean {
        val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$".toRegex()
        return emailRegex.matches(email)
    }

    override fun toString(): String = value
}

