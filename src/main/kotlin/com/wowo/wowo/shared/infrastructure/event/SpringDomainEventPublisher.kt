package com.wowo.wowo.shared.infrastructure.event

import com.wowo.wowo.shared.domain.DomainEvent
import com.wowo.wowo.shared.domain.DomainEventPublisher
import org.slf4j.LoggerFactory
import org.springframework.context.ApplicationEventPublisher

/**
 * Spring implementation of DomainEventPublisher
 */
class SpringDomainEventPublisher(
    private val applicationEventPublisher: ApplicationEventPublisher
) : DomainEventPublisher {

    private val logger = LoggerFactory.getLogger(SpringDomainEventPublisher::class.java)

    override fun publish(event: DomainEvent) {
        logger.debug("Publishing domain event: ${event::class.qualifiedName} for aggregateId=${event.aggregateId}")
        applicationEventPublisher.publishEvent(event)
    }

    override fun publish(events: List<DomainEvent>) {
        events.forEach {
            logger.debug("Publishing domain event from list: ${it::class.qualifiedName} for aggregateId=${it.aggregateId}")
            publish(it)
        }
    }
}
