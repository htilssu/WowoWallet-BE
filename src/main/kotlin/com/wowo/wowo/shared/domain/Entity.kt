package com.wowo.wowo.shared.domain

import java.time.*

/**
 * Base class for all domain entities
 */
abstract class Entity<ID> {
    abstract val id: ID
    open val createdAt: Instant = Instant.now()
    open var updatedAt: Instant = Instant.now()

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Entity<*>) return false
        return id == other.id
    }

    override fun hashCode(): Int = id?.hashCode() ?: 0
}

