package com.wowo.wowo.shared.domain

import java.time.LocalDateTime
import java.util.*

/**
 * Base interface for all domain events
 */
interface DomainEvent {
    val eventId: String
    val occurredOn: LocalDateTime
    val aggregateId: String
}

/**
 * Abstract base implementation for domain events
 */
abstract class BaseDomainEvent(
    override val aggregateId: String
) : DomainEvent {
    override val eventId: String = UUID.randomUUID().toString()
    override val occurredOn: LocalDateTime = LocalDateTime.now()
}

