package com.wowo.wowo.domain.shared;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Base class for entities that serve as aggregate roots.
 */
public abstract class BaseAggregateRoot implements AggregateRoot {
    
    private final List<DomainEvent> domainEvents = new ArrayList<>();
    
    @Override
    public List<DomainEvent> getDomainEvents() {
        return Collections.unmodifiableList(domainEvents);
    }
    
    @Override
    public void markEventsAsHandled() {
        domainEvents.clear();
    }
    
    @Override
    public void addDomainEvent(DomainEvent event) {
        domainEvents.add(event);
    }
}