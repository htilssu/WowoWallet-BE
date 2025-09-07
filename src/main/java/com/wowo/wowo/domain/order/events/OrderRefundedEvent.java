package com.wowo.wowo.domain.order.events;

import com.wowo.wowo.domain.order.valueobjects.OrderId;
import com.wowo.wowo.domain.shared.DomainEvent;
import com.wowo.wowo.domain.shared.valueobjects.Money;
import lombok.Value;
import java.time.Instant;
import java.util.UUID;

/**
 * Domain event raised when an order is refunded
 */
@Value
public class OrderRefundedEvent implements DomainEvent {
    OrderId orderId;
    Money refundAmount;
    Instant occurredAt;
    String eventId;
    
    public OrderRefundedEvent(OrderId orderId, Money refundAmount) {
        this.orderId = orderId;
        this.refundAmount = refundAmount;
        this.occurredAt = Instant.now();
        this.eventId = UUID.randomUUID().toString();
    }
    
    @Override
    public Instant getOccurredOn() {
        return occurredAt;
    }
    
    @Override
    public String getEventType() {
        return "OrderRefunded";
    }
}