package com.wowo.wowo.contexts.user.application.dto

import com.wowo.wowo.contexts.user.domain.entity.User
import java.time.LocalDateTime

/**
 * Data Transfer Object for User
 */
data class UserDTO(
    val id: String,
    val username: String,
    val email: String,
    val phoneNumber: String?,
    val isVerified: Boolean,
    val isActive: Boolean,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime
) {
    companion object {
        fun fromDomain(user: User): UserDTO {
            return UserDTO(
                id = user.id.toString(),
                username = user.username.value,
                email = user.email.value,
                phoneNumber = user.phoneNumber?.value,
                isVerified = user.isVerified,
                isActive = user.isActive,
                createdAt = user.createdAt,
                updatedAt = user.updatedAt
            )
        }
    }
}

