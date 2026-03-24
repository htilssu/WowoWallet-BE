package com.wowo.wowo.contexts.user.domain.entity

import com.wowo.wowo.shared.domain.AggregateRoot
import com.wowo.wowo.shared.valueobject.Email
import com.wowo.wowo.shared.valueobject.PhoneNumber
import com.wowo.wowo.contexts.user.domain.valueobject.UserId
import com.wowo.wowo.contexts.user.domain.valueobject.Username
import com.wowo.wowo.contexts.user.domain.valueobject.Password
import com.wowo.wowo.contexts.user.domain.event.UserRegisteredEvent
import java.time.Instant
import java.time.LocalDateTime

/**
 * User Aggregate Root
 */
class User(
    override val id: UserId,
    val username: Username,
    private var password: Password,
    val email: Email,
    val phoneNumber: PhoneNumber?,
    var isVerified: Boolean = false,
    var isActive: Boolean = true,
    override val createdAt: Instant = Instant.now(),
    override var updatedAt: Instant = Instant.now()
) : AggregateRoot<UserId>() {

    private var profile: UserProfile? = null

    fun updatePassword(newPassword: Password) {
        this.password = newPassword
        this.updatedAt = Instant.now()
    }

    fun verify() {
        this.isVerified = true
        this.updatedAt = Instant.now()
    }

    fun deactivate() {
        this.isActive = false
        this.updatedAt = Instant.now()
    }

    fun activate() {
        this.isActive = true
        this.updatedAt = Instant.now()
    }

    fun updateProfile(profile: UserProfile) {
        this.profile = profile
        this.updatedAt = Instant.now()
    }

    fun getPassword(): Password = password

    fun getProfile(): UserProfile? = profile

    companion object {
        fun register(
            username: Username,
            password: Password,
            email: Email,
            phoneNumber: PhoneNumber?
        ): User {
            val user = User(
                id = UserId(),
                username = username,
                password = password,
                email = email,
                phoneNumber = phoneNumber
            )
            user.addDomainEvent(UserRegisteredEvent(user.id.toString(), username.value, email.value))
            return user
        }
    }
}

