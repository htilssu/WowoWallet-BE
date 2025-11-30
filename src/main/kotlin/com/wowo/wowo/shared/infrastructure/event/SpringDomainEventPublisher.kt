package com.wowo.wowo.shared.infrastructure.event

import com.wowo.wowo.shared.domain.DomainEvent
import com.wowo.wowo.shared.domain.DomainEventPublisher
import org.springframework.context.ApplicationEventPublisher

/**
 * Spring implementation of DomainEventPublisher
 */
class SpringDomainEventPublisher(
    private val applicationEventPublisher: ApplicationEventPublisher
) : DomainEventPublisher {

    override fun publish(event: DomainEvent) {
        applicationEventPublisher.publishEvent(event)
    }

    override fun publish(events: List<DomainEvent>) {
        events.forEach { publish(it) }
    }
}


