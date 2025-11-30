package com.wowo.wowo.shared.domain
}
    fun getDomainEvents(): List<DomainEvent> = domainEvents.toList()

    }
        domainEvents.clear()
    fun clearDomainEvents() {

    }
        domainEvents.add(event)
    fun addDomainEvent(event: DomainEvent) {

    private val domainEvents: MutableList<DomainEvent> = mutableListOf()
abstract class AggregateRoot<ID> : Entity<ID>() {
 */
 * Base class for Aggregate Roots in DDD
/**

import java.util.*
import java.time.LocalDateTime


