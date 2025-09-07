package com.wowo.wowo.domain.order.services;

import com.wowo.wowo.domain.order.entity.OrderAggregate;
import com.wowo.wowo.domain.order.repository.OrderRepository;
import com.wowo.wowo.domain.order.valueobjects.ApplicationId;
import com.wowo.wowo.domain.order.valueobjects.OrderId;
import com.wowo.wowo.domain.shared.valueobjects.Money;
import com.wowo.wowo.domain.transaction.valueobjects.TransactionId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Order Domain Service - Coordinates complex operations spanning multiple aggregates
 * Contains business logic that doesn't naturally fit within a single aggregate
 */
@Service
@RequiredArgsConstructor
public class OrderDomainService {
    
    private final OrderRepository orderRepository;
    
    /**
     * Process order completion with payment validation
     */
    public void processOrderCompletion(OrderId orderId, TransactionId transactionId, Money paidAmount) {
        OrderAggregate order = orderRepository.findById(orderId)
            .orElseThrow(() -> new IllegalArgumentException("Order not found: " + orderId));
        
        // Validate payment amount matches order final amount
        Money expectedAmount = order.getFinalAmount();
        if (!paidAmount.equals(expectedAmount)) {
            throw new IllegalArgumentException(
                String.format("Payment amount %s does not match order amount %s", 
                    paidAmount, expectedAmount)
            );
        }
        
        order.complete(transactionId);
        orderRepository.save(order);
    }
    
    /**
     * Validate and process order refund
     */
    public void processOrderRefund(OrderId orderId, Money refundAmount) {
        OrderAggregate order = orderRepository.findById(orderId)
            .orElseThrow(() -> new IllegalArgumentException("Order not found: " + orderId));
        
        // Business rule: refund amount validation is handled in the aggregate
        order.refund(refundAmount);
        orderRepository.save(order);
    }
    
    /**
     * Calculate total revenue for an application
     */
    public Money calculateApplicationRevenue(ApplicationId applicationId) {
        return orderRepository.findByApplicationId(applicationId)
            .stream()
            .filter(OrderAggregate::isCompleted)
            .map(OrderAggregate::getFinalAmount)
            .reduce(Money.zero("VND"), Money::add);
    }
    
    /**
     * Check if order can be cancelled based on business rules
     */
    public boolean canCancelOrder(OrderId orderId) {
        return orderRepository.findById(orderId)
            .map(OrderAggregate::isPending)
            .orElse(false);
    }
}