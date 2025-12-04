package com.wowo.wowo.contexts.user.infrastructure.persistence

import com.wowo.wowo.contexts.user.domain.entity.Role
import com.wowo.wowo.contexts.user.domain.repository.RoleRepository
import com.wowo.wowo.contexts.user.domain.valueobject.PermissionId
import com.wowo.wowo.contexts.user.domain.valueobject.RoleId
import com.wowo.wowo.contexts.user.domain.valueobject.RoleName
import com.wowo.wowo.contexts.user.domain.valueobject.UserId
import org.springframework.stereotype.Repository

/**
 * JPA implementation of RoleRepository
 */
@Repository
class RoleRepositoryAdapter(
    private val jpaRepository: RoleJpaRepository
) : RoleRepository {

    override fun save(role: Role): Role {
        val jpaEntity = toJpaEntity(role)
        val savedEntity = jpaRepository.save(jpaEntity)
        return toDomainEntity(savedEntity)
    }

    override fun findById(id: RoleId): Role? {
        return jpaRepository.findById(id.value)
            .map { toDomainEntity(it) }
            .orElse(null)
    }

    override fun findByName(name: RoleName): Role? {
        return jpaRepository.findByName(name.value)?.let { toDomainEntity(it) }
    }

    override fun findAll(): List<Role> {
        return jpaRepository.findAll().map { toDomainEntity(it) }
    }

    override fun existsByName(name: RoleName): Boolean {
        return jpaRepository.existsByName(name.value)
    }

    override fun delete(role: Role) {
        jpaRepository.deleteById(role.id.value)
    }

    override fun findAllByUserId(userId: UserId): Set<Role> {
        return jpaRepository.findAllByUserId(userId.value)
            .map { toDomainEntity(it) }
            .toSet()
    }

    // Mappers
    
    private fun toJpaEntity(role: Role): RoleJpaEntity {
        return RoleJpaEntity(
            id = role.id.value,
            name = role.name.value,
            description = role.description,
            createdAt = role.createdAt,
            updatedAt = role.updatedAt
        )
    }

    private fun toDomainEntity(jpaEntity: RoleJpaEntity): Role {
        return Role.reconstitute(
            id = RoleId(jpaEntity.id),
            name = RoleName(jpaEntity.name),
            description = jpaEntity.description,
            permissions = jpaEntity.permissions.map { PermissionId(it.id) }.toSet(),
            createdAt = jpaEntity.createdAt,
            updatedAt = jpaEntity.updatedAt
        )
    }
}
