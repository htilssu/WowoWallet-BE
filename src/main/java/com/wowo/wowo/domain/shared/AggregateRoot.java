package com.wowo.wowo.domain.shared;

import java.util.List;

/**
 * Base interface for aggregate roots in the domain.
 * Aggregates are clusters of domain objects that can be treated as a single unit.
 */
public interface AggregateRoot {
    
    /**
     * Gets the list of domain events that have been raised by this aggregate.
     */
    List<DomainEvent> getDomainEvents();
    
    /**
     * Marks all domain events as handled by clearing the event list.
     */
    void markEventsAsHandled();
    
    /**
     * Adds a domain event to be published when the aggregate is saved.
     */
    void addDomainEvent(DomainEvent event);
}