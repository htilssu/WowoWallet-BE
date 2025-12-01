package com.wowo.wowo.shared.infrastructure.event

import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Profile
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component
import org.springframework.transaction.event.TransactionPhase
import org.springframework.transaction.event.TransactionalEventListener

/**
 * Debug listener to log all events published in the application.
 * - `onAnyEvent` logs immediately when ApplicationEventPublisher.publishEvent is called.
 * - `onAnyEventAfterCommit` logs transactional AFTER_COMMIT events to see when they fire.
 */
@Component
@Profile("development", "staging")
class DebugDomainEventListener {
    private val logger = LoggerFactory.getLogger(DebugDomainEventListener::class.java)

    @EventListener
    fun onAnyEvent(event: Any) {
        logger.info("[DebugEventListener] Event published immediately: ${event::class.qualifiedName} -> $event")
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    fun onAnyEventAfterCommit(event: Any) {
        logger.info("[DebugEventListener] Transactional AFTER_COMMIT event: ${event::class.qualifiedName} -> $event")
    }
}

