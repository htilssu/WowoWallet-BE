package com.wowo.wowo.shared.domain

/**
 * Interface for publishing domain events
 */
interface DomainEventPublisher {
    fun publish(event: DomainEvent)
    fun publish(events: List<DomainEvent>)
}

