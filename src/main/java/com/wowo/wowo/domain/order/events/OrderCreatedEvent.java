package com.wowo.wowo.domain.order.events;

import com.wowo.wowo.domain.order.valueobjects.OrderId;
import com.wowo.wowo.domain.order.valueobjects.ApplicationId;
import com.wowo.wowo.domain.shared.DomainEvent;
import com.wowo.wowo.domain.shared.valueobjects.Money;
import lombok.Value;
import java.time.Instant;
import java.util.UUID;

/**
 * Domain event raised when an order is created
 */
@Value
public class OrderCreatedEvent implements DomainEvent {
    OrderId orderId;
    ApplicationId applicationId;
    Money amount;
    String serviceName;
    Instant occurredAt;
    String eventId;
    
    public OrderCreatedEvent(OrderId orderId, ApplicationId applicationId, Money amount, String serviceName) {
        this.orderId = orderId;
        this.applicationId = applicationId;
        this.amount = amount;
        this.serviceName = serviceName;
        this.occurredAt = Instant.now();
        this.eventId = UUID.randomUUID().toString();
    }
    
    @Override
    public Instant getOccurredOn() {
        return occurredAt;
    }
    
    @Override
    public String getEventType() {
        return "OrderCreated";
    }
}