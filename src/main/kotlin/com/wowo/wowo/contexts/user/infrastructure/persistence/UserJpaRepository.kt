package com.wowo.wowo.contexts.user.infrastructure.persistence

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

/**
 * Spring Data JPA Repository for User
 */
@Repository
interface UserJpaRepository : JpaRepository<UserJpaEntity, UUID> {
    fun findByUsername(username: String): UserJpaEntity?
    fun findByEmail(email: String): UserJpaEntity?
    fun existsByUsername(username: String): Boolean
    fun existsByEmail(email: String): Boolean
}

