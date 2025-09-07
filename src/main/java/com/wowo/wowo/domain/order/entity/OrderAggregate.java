package com.wowo.wowo.domain.order.entity;

import com.wowo.wowo.domain.order.events.OrderCreatedEvent;
import com.wowo.wowo.domain.order.events.OrderCompletedEvent;
import com.wowo.wowo.domain.order.events.OrderCancelledEvent;
import com.wowo.wowo.domain.order.events.OrderRefundedEvent;
import com.wowo.wowo.domain.order.valueobjects.OrderId;
import com.wowo.wowo.domain.order.valueobjects.OrderStatus;
import com.wowo.wowo.domain.order.valueobjects.OrderUrls;
import com.wowo.wowo.domain.order.valueobjects.ApplicationId;
import com.wowo.wowo.domain.shared.BaseAggregateRoot;
import com.wowo.wowo.domain.shared.valueobjects.Money;
import com.wowo.wowo.domain.transaction.valueobjects.TransactionId;
import lombok.Getter;

import java.time.Instant;

/**
 * Order Aggregate - Core business entity for order management
 * Encapsulates order business rules and state management
 */
@Getter
public class OrderAggregate extends BaseAggregateRoot {
    
    private final OrderId id;
    private final ApplicationId applicationId;
    private final Money amount;
    private Money discountAmount;
    private OrderStatus status;
    private TransactionId transactionId;
    private final OrderUrls urls;
    private final String serviceName;
    private final Instant createdAt;
    private Instant updatedAt;
    
    // Constructor for new orders
    public OrderAggregate(OrderId id, ApplicationId applicationId, Money amount, 
                         OrderUrls urls, String serviceName) {
        super();
        this.id = id;
        this.applicationId = applicationId;
        this.amount = amount;
        this.discountAmount = Money.zero(amount.getCurrency());
        this.status = OrderStatus.PENDING;
        this.urls = urls;
        this.serviceName = serviceName;
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
        
        // Raise domain event
        addDomainEvent(new OrderCreatedEvent(id, applicationId, amount, serviceName));
    }
    
    // Constructor for loading from persistence
    public OrderAggregate(OrderId id, ApplicationId applicationId, Money amount, Money discountAmount,
                         OrderStatus status, TransactionId transactionId, OrderUrls urls, 
                         String serviceName, Instant createdAt, Instant updatedAt) {
        super();
        this.id = id;
        this.applicationId = applicationId;
        this.amount = amount;
        this.discountAmount = discountAmount;
        this.status = status;
        this.transactionId = transactionId;
        this.urls = urls;
        this.serviceName = serviceName;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
    
    /**
     * Apply discount to the order
     */
    public void applyDiscount(Money discount) {
        if (!isPending()) {
            throw new IllegalStateException("Cannot apply discount to non-pending order");
        }
        
        if (discount.getAmount().compareTo(amount.getAmount()) > 0) {
            throw new IllegalArgumentException("Discount cannot exceed order amount");
        }
        
        this.discountAmount = discount;
        this.updatedAt = Instant.now();
    }
    
    /**
     * Complete the order with transaction
     */
    public void complete(TransactionId transactionId) {
        if (!isPending()) {
            throw new IllegalStateException("Only pending orders can be completed");
        }
        
        this.status = OrderStatus.SUCCESS;
        this.transactionId = transactionId;
        this.updatedAt = Instant.now();
        
        addDomainEvent(new OrderCompletedEvent(id, transactionId));
    }
    
    /**
     * Cancel the order
     */
    public void cancel() {
        if (!isPending()) {
            throw new IllegalStateException("Only pending orders can be cancelled");
        }
        
        this.status = OrderStatus.CANCELLED;
        this.updatedAt = Instant.now();
        
        addDomainEvent(new OrderCancelledEvent(id));
    }
    
    /**
     * Refund the order
     */
    public void refund(Money refundAmount) {
        if (!isCompleted()) {
            throw new IllegalStateException("Only completed orders can be refunded");
        }
        
        Money finalAmount = getFinalAmount();
        if (refundAmount.getAmount().compareTo(finalAmount.getAmount()) > 0) {
            throw new IllegalArgumentException("Refund amount cannot exceed order final amount");
        }
        
        this.status = OrderStatus.REFUNDED;
        this.updatedAt = Instant.now();
        
        addDomainEvent(new OrderRefundedEvent(id, refundAmount));
    }
    
    /**
     * Fail the order
     */
    public void fail() {
        if (!isPending()) {
            throw new IllegalStateException("Only pending orders can fail");
        }
        
        this.status = OrderStatus.FAILED;
        this.updatedAt = Instant.now();
    }
    
    /**
     * Get final amount after discount
     */
    public Money getFinalAmount() {
        return amount.subtract(discountAmount);
    }
    
    /**
     * Check if order is pending
     */
    public boolean isPending() {
        return status == OrderStatus.PENDING;
    }
    
    /**
     * Check if order is completed
     */
    public boolean isCompleted() {
        return status == OrderStatus.SUCCESS;
    }
    
    /**
     * Check if order is cancelled
     */
    public boolean isCancelled() {
        return status == OrderStatus.CANCELLED;
    }
    
    /**
     * Check if order is refunded
     */
    public boolean isRefunded() {
        return status == OrderStatus.REFUNDED;
    }
    
    /**
     * Check if order has failed
     */
    public boolean hasFailed() {
        return status == OrderStatus.FAILED;
    }
    
    /**
     * Check if order can be processed for payment
     */
    public boolean canBeProcessed() {
        return isPending() && getFinalAmount().getAmount().longValue() > 0;
    }
}