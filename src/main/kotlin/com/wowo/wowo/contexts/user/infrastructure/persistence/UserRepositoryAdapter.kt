package com.wowo.wowo.contexts.user.infrastructure.persistence

import com.wowo.wowo.contexts.user.domain.entity.User
import com.wowo.wowo.contexts.user.domain.repository.UserRepository
import com.wowo.wowo.contexts.user.domain.valueobject.UserId
import com.wowo.wowo.contexts.user.domain.valueobject.Username
import com.wowo.wowo.contexts.user.domain.valueobject.Password
import com.wowo.wowo.shared.valueobject.Email
import com.wowo.wowo.shared.valueobject.PhoneNumber
import org.springframework.stereotype.Component

/**
 * Adapter implementing UserRepository using JPA
 */
@Component
class UserRepositoryAdapter(
    private val jpaRepository: UserJpaRepository
) : UserRepository {

    override fun save(user: User): User {
        val jpaEntity = toJpaEntity(user)
        val savedEntity = jpaRepository.save(jpaEntity)
        return toDomainEntity(savedEntity)
    }

    override fun findById(id: UserId): User? {
        return jpaRepository.findById(id.value).map { toDomainEntity(it) }.orElse(null)
    }

    override fun findByUsername(username: Username): User? {
        return jpaRepository.findByUsername(username.value)?.let { toDomainEntity(it) }
    }

    override fun findByEmail(email: Email): User? {
        return jpaRepository.findByEmail(email.value)?.let { toDomainEntity(it) }
    }

    override fun existsByUsername(username: Username): Boolean {
        return jpaRepository.existsByUsername(username.value)
    }

    override fun existsByEmail(email: Email): Boolean {
        return jpaRepository.existsByEmail(email.value)
    }

    override fun delete(user: User) {
        jpaRepository.deleteById(user.id.value)
    }

    override fun findAll(): List<User> {
        return jpaRepository.findAll().map { toDomainEntity(it) }
    }

    private fun toJpaEntity(user: User): UserJpaEntity {
        return UserJpaEntity(
            id = user.id.value,
            username = user.username.value,
            password = user.getPassword().hashedValue,
            email = user.email.value,
            phoneNumber = user.phoneNumber?.value,
            isVerified = user.isVerified,
            isActive = user.isActive,
            createdAt = user.createdAt,
            updatedAt = user.updatedAt
        )
    }

    private fun toDomainEntity(jpaEntity: UserJpaEntity): User {
        return User(
            id = UserId(jpaEntity.id),
            username = Username(jpaEntity.username),
            password = Password(jpaEntity.password),
            email = Email(jpaEntity.email),
            phoneNumber = jpaEntity.phoneNumber?.let { PhoneNumber(it) },
            isVerified = jpaEntity.isVerified,
            isActive = jpaEntity.isActive,
            createdAt = jpaEntity.createdAt,
            updatedAt = jpaEntity.updatedAt
        )
    }
}

