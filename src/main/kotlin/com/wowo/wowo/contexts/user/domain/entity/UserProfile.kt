package com.wowo.wowo.contexts.user.domain.entity

import com.wowo.wowo.shared.domain.Entity
import com.wowo.wowo.contexts.user.domain.valueobject.UserId
import java.time.LocalDate

/**
 * User Profile Entity
 */
data class UserProfile(
    override val id: UserId,
    val firstName: String?,
    val lastName: String?,
    val dateOfBirth: LocalDate?,
    val address: String?,
    val city: String?,
    val country: String?,
    val avatarUrl: String?
) : Entity<UserId>()

