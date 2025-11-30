package com.wowo.wowo.contexts.user.domain.repository

import com.wowo.wowo.contexts.user.domain.entity.User
import com.wowo.wowo.contexts.user.domain.valueobject.UserId
import com.wowo.wowo.contexts.user.domain.valueobject.Username
import com.wowo.wowo.shared.valueobject.Email
import java.util.*

/**
 * Repository interface for User aggregate (Domain layer)
 */
interface UserRepository {
    fun save(user: User): User
    fun findById(id: UserId): User?
    fun findByUsername(username: Username): User?
    fun findByEmail(email: Email): User?
    fun existsByUsername(username: Username): Boolean
    fun existsByEmail(email: Email): Boolean
    fun delete(user: User)
    fun findAll(): List<User>
}

