package com.wowo.wowo.contexts.user.infrastructure.persistence

import jakarta.persistence.*
import java.time.Instant
import java.time.LocalDateTime
import java.util.*

/**
 * JPA Entity for Permission
 */
@Entity
@Table(name = "permissions")
class PermissionJpaEntity(
    @Id
    var id: UUID = UUID.randomUUID(),

    @Column(nullable = false, unique = true, length = 100)
    var name: String,

    @Column
    var description: String? = null,

    @Column(nullable = false, length = 50)
    var resource: String,

    @Column(nullable = false, length = 50)
    var action: String,

    @Column(nullable = false, updatable = false)
    var createdAt: Instant = Instant.now(),

    @Column(nullable = false)
    var updatedAt: Instant = Instant.now(),

    @ManyToMany(mappedBy = "permissions")
    var roles: MutableSet<RoleJpaEntity> = mutableSetOf()
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is PermissionJpaEntity) return false
        return id == other.id
    }

    override fun hashCode(): Int = id.hashCode()

    override fun toString(): String = "Permission(id=$id, name='$name', resource='$resource', action='$action')"
}
