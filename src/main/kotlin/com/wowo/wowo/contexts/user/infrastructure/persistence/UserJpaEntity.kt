package com.wowo.wowo.contexts.user.infrastructure.persistence

import jakarta.persistence.*
import java.time.LocalDateTime
import java.util.*

/**
 * JPA Entity for User
 */
@Entity
@Table(name = "users")
class UserJpaEntity(
    @Id
    var id: UUID = UUID.randomUUID(),

    @Column(nullable = false, unique = true, length = 50)
    var username: String,

    @Column(nullable = false)
    var password: String,

    @Column(nullable = false, unique = true)
    var email: String,

    @Column
    var phoneNumber: String? = null,

    @Column(nullable = false)
    var isVerified: Boolean = false,

    @Column(nullable = false)
    var isActive: Boolean = true,

    @Column(nullable = false, updatable = false)
    var createdAt: LocalDateTime = LocalDateTime.now(),

    @Column(nullable = false)
    var updatedAt: LocalDateTime = LocalDateTime.now()
)


