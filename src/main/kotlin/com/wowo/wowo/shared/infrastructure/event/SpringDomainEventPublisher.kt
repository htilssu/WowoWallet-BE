package com.wowo.wowo.shared.infrastructure.event
}
    }
        events.forEach { publish(it) }
    override fun publish(events: List<DomainEvent>) {

    }
        applicationEventPublisher.publishEvent(event)
    override fun publish(event: DomainEvent) {

) : DomainEventPublisher {
    private val applicationEventPublisher: ApplicationEventPublisher
class SpringDomainEventPublisher(
@Component
 */
 * Spring implementation of DomainEventPublisher
/**

import org.springframework.stereotype.Component
import org.springframework.context.ApplicationEventPublisher
import com.wowo.wowo.shared.domain.DomainEventPublisher
import com.wowo.wowo.shared.domain.DomainEvent


