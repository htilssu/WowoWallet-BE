package com.wowo.wowo.contexts.user.domain.entity

import com.wowo.wowo.shared.domain.Entity
import com.wowo.wowo.contexts.user.domain.valueobject.RoleId
import com.wowo.wowo.contexts.user.domain.valueobject.RoleName
import com.wowo.wowo.contexts.user.domain.valueobject.PermissionId
import java.time.Instant
import java.time.LocalDateTime

/**
 * Role Domain Entity
 * Represents a role that can be assigned to users
 */
class Role private constructor(
    override val id: RoleId,
    val name: RoleName,
    val description: String? = null,
    initialPermissions: Set<PermissionId> = emptySet(),
    override val createdAt: Instant = Instant.now(),
    override var updatedAt: Instant = Instant.now()
) : Entity<RoleId>() {

    private val _permissions: MutableSet<PermissionId> = initialPermissions.toMutableSet()
    
    val permissions: Set<PermissionId>
        get() = _permissions.toSet()

    fun addPermission(permissionId: PermissionId) {
        _permissions.add(permissionId)
        this.updatedAt = Instant.now()
    }

    fun removePermission(permissionId: PermissionId) {
        _permissions.remove(permissionId)
        this.updatedAt = Instant.now()
    }

    fun hasPermission(permissionId: PermissionId): Boolean {
        return _permissions.contains(permissionId)
    }

    fun clearPermissions() {
        _permissions.clear()
        this.updatedAt = Instant.now()
    }

    companion object {
        fun create(
            name: RoleName,
            description: String? = null,
            permissions: Set<PermissionId> = emptySet()
        ): Role {
            return Role(
                id = RoleId(),
                name = name,
                description = description,
                initialPermissions = permissions
            )
        }
        
        /**
         * Reconstitute a Role from persistence
         */
        fun reconstitute(
            id: RoleId,
            name: RoleName,
            description: String?,
            permissions: Set<PermissionId>,
            createdAt: Instant,
            updatedAt: Instant
        ): Role {
            return Role(
                id = id,
                name = name,
                description = description,
                initialPermissions = permissions,
                createdAt = createdAt,
                updatedAt = updatedAt
            )
        }
    }
}
