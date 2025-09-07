package com.wowo.wowo.application.order;

import com.wowo.wowo.domain.order.entity.OrderAggregate;
import com.wowo.wowo.domain.order.repository.OrderRepository;
import com.wowo.wowo.domain.order.services.OrderDomainService;
import com.wowo.wowo.domain.order.valueobjects.*;
import com.wowo.wowo.domain.shared.valueobjects.Money;
import com.wowo.wowo.domain.transaction.valueobjects.TransactionId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Order Application Service - Orchestrates order domain operations
 * Provides use cases for order management with proper transaction boundaries
 */
@Service
@RequiredArgsConstructor
@Transactional
public class OrderApplicationService {
    
    private final OrderRepository orderRepository;
    private final OrderDomainService orderDomainService;
    
    /**
     * Create a new order
     */
    public OrderId createOrder(String applicationId, Long amount, String currency, 
                              String returnUrl, String successUrl, String serviceName) {
        
        OrderId orderId = OrderId.generate();
        ApplicationId appId = ApplicationId.of(applicationId);
        Money orderAmount = new Money(amount, currency);
        OrderUrls urls = new OrderUrls(returnUrl, successUrl);
        
        OrderAggregate order = new OrderAggregate(orderId, appId, orderAmount, urls, serviceName);
        
        orderRepository.save(order);
        return orderId;
    }
    
    /**
     * Get order by ID
     */
    @Transactional(readOnly = true)
    public Optional<OrderAggregate> getOrderById(OrderId orderId) {
        return orderRepository.findById(orderId);
    }
    
    /**
     * Apply discount to order
     */
    public void applyDiscount(OrderId orderId, Long discountAmount, String currency) {
        OrderAggregate order = orderRepository.findById(orderId)
            .orElseThrow(() -> new IllegalArgumentException("Order not found: " + orderId));
        
        Money discount = new Money(discountAmount, currency);
        order.applyDiscount(discount);
        
        orderRepository.save(order);
    }
    
    /**
     * Complete order with payment
     */
    public void completeOrder(OrderId orderId, TransactionId transactionId, Long paidAmount, String currency) {
        Money payment = new Money(paidAmount, currency);
        orderDomainService.processOrderCompletion(orderId, transactionId, payment);
    }
    
    /**
     * Cancel order
     */
    public void cancelOrder(OrderId orderId) {
        OrderAggregate order = orderRepository.findById(orderId)
            .orElseThrow(() -> new IllegalArgumentException("Order not found: " + orderId));
        
        order.cancel();
        orderRepository.save(order);
    }
    
    /**
     * Refund order
     */
    public void refundOrder(OrderId orderId, Long refundAmount, String currency) {
        Money refund = new Money(refundAmount, currency);
        orderDomainService.processOrderRefund(orderId, refund);
    }
    
    /**
     * Get orders by application
     */
    @Transactional(readOnly = true)
    public List<OrderAggregate> getOrdersByApplication(String applicationId) {
        ApplicationId appId = ApplicationId.of(applicationId);
        return orderRepository.findByApplicationId(appId);
    }
    
    /**
     * Get orders by status
     */
    @Transactional(readOnly = true)
    public List<OrderAggregate> getOrdersByStatus(OrderStatus status) {
        return orderRepository.findByStatus(status);
    }
    
    /**
     * Get application revenue
     */
    @Transactional(readOnly = true)
    public Money getApplicationRevenue(String applicationId) {
        ApplicationId appId = ApplicationId.of(applicationId);
        return orderDomainService.calculateApplicationRevenue(appId);
    }
    
    /**
     * Check if order can be cancelled
     */
    @Transactional(readOnly = true)
    public boolean canCancelOrder(OrderId orderId) {
        return orderDomainService.canCancelOrder(orderId);
    }
    
    /**
     * Get all orders with pagination
     */
    @Transactional(readOnly = true)
    public List<OrderAggregate> getAllOrders(int page, int size) {
        return orderRepository.findAll(page, size);
    }
}