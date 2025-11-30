package com.wowo.wowo.shared.domain

import java.time.LocalDateTime

/**
 * Base class for all domain entities
 */
abstract class Entity<ID> {
    abstract val id: ID
    open val createdAt: LocalDateTime = LocalDateTime.now()
    open val updatedAt: LocalDateTime = LocalDateTime.now()

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Entity<*>) return false
        return id == other.id
    }

    override fun hashCode(): Int = id?.hashCode() ?: 0
}

