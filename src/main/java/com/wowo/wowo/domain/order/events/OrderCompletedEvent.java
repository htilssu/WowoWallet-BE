package com.wowo.wowo.domain.order.events;

import com.wowo.wowo.domain.order.valueobjects.OrderId;
import com.wowo.wowo.domain.shared.DomainEvent;
import com.wowo.wowo.domain.transaction.valueobjects.TransactionId;
import lombok.Value;
import java.time.Instant;
import java.util.UUID;

/**
 * Domain event raised when an order is completed
 */
@Value
public class OrderCompletedEvent implements DomainEvent {
    OrderId orderId;
    TransactionId transactionId;
    Instant occurredAt;
    String eventId;
    
    public OrderCompletedEvent(OrderId orderId, TransactionId transactionId) {
        this.orderId = orderId;
        this.transactionId = transactionId;
        this.occurredAt = Instant.now();
        this.eventId = UUID.randomUUID().toString();
    }
    
    @Override
    public Instant getOccurredOn() {
        return occurredAt;
    }
    
    @Override
    public String getEventType() {
        return "OrderCompleted";
    }
}