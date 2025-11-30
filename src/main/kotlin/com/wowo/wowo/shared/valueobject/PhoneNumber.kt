package com.wowo.wowo.shared.valueobject

import com.wowo.wowo.shared.domain.ValueObject

/**
 * Value Object representing a phone number
 */
data class PhoneNumber(val value: String) : ValueObject {
    init {
        require(value.isNotBlank()) { "Phone number cannot be blank" }
        require(isValidPhoneNumber(value)) { "Invalid phone number format: $value" }
    }

    private fun isValidPhoneNumber(phone: String): Boolean {
        val phoneRegex = "^\\+?[1-9]\\d{1,14}$".toRegex()
        return phoneRegex.matches(phone.replace("\\s".toRegex(), ""))
    }

    override fun toString(): String = value
}

