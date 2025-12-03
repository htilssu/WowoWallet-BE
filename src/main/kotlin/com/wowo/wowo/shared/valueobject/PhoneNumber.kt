package com.wowo.wowo.shared.valueobject

import com.wowo.wowo.shared.domain.ValueObject

/**
 * Value Object representing a phone number
 */
data class PhoneNumber(var value: String) : ValueObject {
    init {
        require(value.isNotBlank()) { "Phone number cannot be blank" }
        value = cleanPhoneNumber(value)
        require(isValidPhoneNumber(value)) { "Invalid phone number format: $value" }
    }

    private fun isValidPhoneNumber(phone: String): Boolean {
        val phoneRegex = "^\\+?[1-9]\\d{1,14}$".toRegex()
        return phoneRegex.matches(phone.replace("\\s".toRegex(), ""))
    }

    private fun cleanPhoneNumber(phone: String): String {
        return phone.replace("[^\\d+]".toRegex(), "")
    }

    override fun toString(): String = value
}

