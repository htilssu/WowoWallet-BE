package com.wowo.wowo.domain.order.repository;

import com.wowo.wowo.domain.order.entity.OrderAggregate;
import com.wowo.wowo.domain.order.valueobjects.OrderId;
import com.wowo.wowo.domain.order.valueobjects.ApplicationId;
import com.wowo.wowo.domain.order.valueobjects.OrderStatus;

import java.util.List;
import java.util.Optional;

/**
 * Order Repository interface
 * Defines persistence operations for Order domain
 */
public interface OrderRepository {
    
    /**
     * Find order by ID
     */
    Optional<OrderAggregate> findById(OrderId orderId);
    
    /**
     * Save order
     */
    void save(OrderAggregate order);
    
    /**
     * Find orders by application ID
     */
    List<OrderAggregate> findByApplicationId(ApplicationId applicationId);
    
    /**
     * Find orders by status
     */
    List<OrderAggregate> findByStatus(OrderStatus status);
    
    /**
     * Find orders by application and status
     */
    List<OrderAggregate> findByApplicationIdAndStatus(ApplicationId applicationId, OrderStatus status);
    
    /**
     * Get all orders with pagination
     */
    List<OrderAggregate> findAll(int page, int size);
    
    /**
     * Delete order by ID
     */
    void delete(OrderId orderId);
}