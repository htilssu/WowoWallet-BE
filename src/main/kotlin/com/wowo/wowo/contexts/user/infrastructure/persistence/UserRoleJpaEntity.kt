package com.wowo.wowo.contexts.user.infrastructure.persistence

import jakarta.persistence.*
import java.io.Serializable
import java.util.*

/**
 * Composite key for UserRole
 */
@Embeddable
data class UserRoleId(
    @Column(name = "user_id")
    var userId: UUID = UUID.randomUUID(),
    
    @Column(name = "role_id")
    var roleId: UUID = UUID.randomUUID()
) : Serializable

/**
 * JPA Entity for User-Role relationship
 */
@Entity
@Table(name = "user_roles")
class UserRoleJpaEntity(
    @EmbeddedId
    var id: UserRoleId
) {
    constructor(userId: UUID, roleId: UUID) : this(UserRoleId(userId, roleId))
    
    val userId: UUID get() = id.userId
    val roleId: UUID get() = id.roleId

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is UserRoleJpaEntity) return false
        return id == other.id
    }

    override fun hashCode(): Int = id.hashCode()

    override fun toString(): String = "UserRole(userId=${id.userId}, roleId=${id.roleId})"
}
