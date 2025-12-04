package com.wowo.wowo.contexts.user.infrastructure.persistence

import jakarta.persistence.*
import java.io.Serializable
import java.util.*

/**
 * Composite key for UserPermission
 */
@Embeddable
data class UserPermissionId(
    @Column(name = "user_id")
    var userId: UUID = UUID.randomUUID(),
    
    @Column(name = "permission_id")
    var permissionId: UUID = UUID.randomUUID()
) : Serializable

/**
 * JPA Entity for direct User-Permission relationship
 */
@Entity
@Table(name = "user_permissions")
class UserPermissionJpaEntity(
    @EmbeddedId
    var id: UserPermissionId
) {
    constructor(userId: UUID, permissionId: UUID) : this(UserPermissionId(userId, permissionId))
    
    val userId: UUID get() = id.userId
    val permissionId: UUID get() = id.permissionId

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is UserPermissionJpaEntity) return false
        return id == other.id
    }

    override fun hashCode(): Int = id.hashCode()

    override fun toString(): String = "UserPermission(userId=${id.userId}, permissionId=${id.permissionId})"
}
