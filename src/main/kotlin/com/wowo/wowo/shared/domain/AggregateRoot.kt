package com.wowo.wowo.shared.domain

/**
 * Base class for Aggregate Roots in DDD
 */
abstract class AggregateRoot<ID> : Entity<ID>() {
    private val domainEvents: MutableList<DomainEvent> = mutableListOf()

    fun addDomainEvent(event: DomainEvent) {
        domainEvents.add(event)
    }

    fun clearDomainEvents() {
        domainEvents.clear()
    }

    fun getDomainEvents(): List<DomainEvent> = domainEvents.toList()
}


