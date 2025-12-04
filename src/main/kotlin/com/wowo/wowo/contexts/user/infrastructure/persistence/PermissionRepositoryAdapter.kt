package com.wowo.wowo.contexts.user.infrastructure.persistence

import com.wowo.wowo.contexts.user.domain.entity.Permission
import com.wowo.wowo.contexts.user.domain.repository.PermissionRepository
import com.wowo.wowo.contexts.user.domain.valueobject.PermissionId
import com.wowo.wowo.contexts.user.domain.valueobject.PermissionName
import com.wowo.wowo.contexts.user.domain.valueobject.UserId
import org.springframework.stereotype.Repository

/**
 * JPA implementation of PermissionRepository
 */
@Repository
class PermissionRepositoryAdapter(
    private val jpaRepository: PermissionJpaRepository
) : PermissionRepository {

    override fun save(permission: Permission): Permission {
        val jpaEntity = toJpaEntity(permission)
        val savedEntity = jpaRepository.save(jpaEntity)
        return toDomainEntity(savedEntity)
    }

    override fun findById(id: PermissionId): Permission? {
        return jpaRepository.findById(id.value)
            .map { toDomainEntity(it) }
            .orElse(null)
    }

    override fun findByName(name: PermissionName): Permission? {
        return jpaRepository.findByName(name.value)?.let { toDomainEntity(it) }
    }

    override fun findByResource(resource: String): List<Permission> {
        return jpaRepository.findByResource(resource).map { toDomainEntity(it) }
    }

    override fun findAll(): List<Permission> {
        return jpaRepository.findAll().map { toDomainEntity(it) }
    }

    override fun existsByName(name: PermissionName): Boolean {
        return jpaRepository.existsByName(name.value)
    }

    override fun delete(permission: Permission) {
        jpaRepository.deleteById(permission.id.value)
    }

    override fun findAllByUserId(userId: UserId): Set<Permission> {
        return jpaRepository.findAllByUserId(userId.value)
            .map { toDomainEntity(it) }
            .toSet()
    }

    // Mappers
    
    private fun toJpaEntity(permission: Permission): PermissionJpaEntity {
        return PermissionJpaEntity(
            id = permission.id.value,
            name = permission.name.value,
            description = permission.description,
            resource = permission.resource,
            action = permission.action,
            createdAt = permission.createdAt,
            updatedAt = permission.updatedAt
        )
    }

    private fun toDomainEntity(jpaEntity: PermissionJpaEntity): Permission {
        return Permission.reconstitute(
            id = PermissionId(jpaEntity.id),
            name = PermissionName(jpaEntity.name),
            description = jpaEntity.description,
            createdAt = jpaEntity.createdAt,
            updatedAt = jpaEntity.updatedAt
        )
    }
}
