package com.wowo.wowo.domain.order.events;

import com.wowo.wowo.domain.order.valueobjects.OrderId;
import com.wowo.wowo.domain.shared.DomainEvent;
import lombok.Value;
import java.time.Instant;
import java.util.UUID;

/**
 * Domain event raised when an order is cancelled
 */
@Value
public class OrderCancelledEvent implements DomainEvent {
    OrderId orderId;
    Instant occurredAt;
    String eventId;
    
    public OrderCancelledEvent(OrderId orderId) {
        this.orderId = orderId;
        this.occurredAt = Instant.now();
        this.eventId = UUID.randomUUID().toString();
    }
    
    @Override
    public Instant getOccurredOn() {
        return occurredAt;
    }
    
    @Override
    public String getEventType() {
        return "OrderCancelled";
    }
}