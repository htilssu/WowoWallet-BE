package com.wowo.wowo.contexts.user.domain.valueobject

import com.wowo.wowo.shared.domain.*

/**
 * Value Object representing a Role name
 */
data class RoleName(val value: String) : ValueObject {

    init {
        require(value.isNotBlank()) { "Role name cannot be blank" }
        require(value.length <= 50) { "Role name cannot exceed 50 characters" }
    }

    override fun toString(): String = value

    companion object {
        val USER = RoleName("ROLE_USER")
        val ADMIN = RoleName("ROLE_ADMIN")
        val SUPER_ADMIN = RoleName("ROLE_SUPER_ADMIN")
        val SUPPORT = RoleName("ROLE_SUPPORT")
        val MERCHANT = RoleName("ROLE_MERCHANT")
    }
}
