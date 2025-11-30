package com.wowo.wowo.config

import com.wowo.wowo.shared.domain.DomainEventPublisher
import com.wowo.wowo.shared.infrastructure.event.SpringDomainEventPublisher
import org.springframework.context.ApplicationEventPublisher
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

/**
 * Configuration for Domain Event Publishing
 */
@Configuration
class EventConfig {

    @Bean
    fun domainEventPublisher(applicationEventPublisher: ApplicationEventPublisher): DomainEventPublisher {
        return SpringDomainEventPublisher(applicationEventPublisher)
    }
}

