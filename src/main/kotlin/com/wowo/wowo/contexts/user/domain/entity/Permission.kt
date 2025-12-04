package com.wowo.wowo.contexts.user.domain.entity

import com.wowo.wowo.shared.domain.Entity
import com.wowo.wowo.contexts.user.domain.valueobject.PermissionId
import com.wowo.wowo.contexts.user.domain.valueobject.PermissionName
import java.time.LocalDateTime

/**
 * Permission Domain Entity
 * Represents a specific permission that can be granted to roles or users
 */
class Permission private constructor(
    override val id: PermissionId,
    val name: PermissionName,
    val description: String? = null,
    override val createdAt: LocalDateTime = LocalDateTime.now(),
    override var updatedAt: LocalDateTime = LocalDateTime.now()
) : Entity<PermissionId>() {

    val resource: String
        get() = name.resource

    val action: String
        get() = name.action

    fun updateDescription(newDescription: String?) {
        // Domain logic can be added here
        this.updatedAt = LocalDateTime.now()
    }

    companion object {
        fun create(
            name: PermissionName,
            description: String? = null
        ): Permission {
            return Permission(
                id = PermissionId(),
                name = name,
                description = description
            )
        }
        
        /**
         * Reconstitute a Permission from persistence
         */
        fun reconstitute(
            id: PermissionId,
            name: PermissionName,
            description: String?,
            createdAt: LocalDateTime,
            updatedAt: LocalDateTime
        ): Permission {
            return Permission(
                id = id,
                name = name,
                description = description,
                createdAt = createdAt,
                updatedAt = updatedAt
            )
        }
    }
}
