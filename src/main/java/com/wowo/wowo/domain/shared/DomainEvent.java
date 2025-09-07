package com.wowo.wowo.domain.shared;

import java.time.Instant;

/**
 * Base interface for all domain events in the system.
 * Domain events represent something important that happened in the domain.
 */
public interface DomainEvent {
    
    /**
     * Gets the timestamp when this event occurred.
     */
    Instant getOccurredOn();
    
    /**
     * Gets a unique identifier for this event.
     */
    String getEventId();
    
    /**
     * Gets the type of this event.
     */
    String getEventType();
}