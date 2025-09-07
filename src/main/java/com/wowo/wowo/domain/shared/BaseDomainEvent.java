package com.wowo.wowo.domain.shared;

import java.time.Instant;
import java.util.UUID;

/**
 * Abstract base class for domain events providing common functionality.
 */
public abstract class BaseDomainEvent implements DomainEvent {
    
    private final String eventId;
    private final Instant occurredOn;
    private final String eventType;
    
    protected BaseDomainEvent(String eventType) {
        this.eventId = UUID.randomUUID().toString();
        this.occurredOn = Instant.now();
        this.eventType = eventType;
    }
    
    @Override
    public String getEventId() {
        return eventId;
    }
    
    @Override
    public Instant getOccurredOn() {
        return occurredOn;
    }
    
    @Override
    public String getEventType() {
        return eventType;
    }
}