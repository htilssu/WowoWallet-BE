package com.wowo.wowo.infrastructure.order;

import com.wowo.wowo.domain.order.entity.OrderAggregate;
import com.wowo.wowo.domain.order.repository.OrderRepository;
import com.wowo.wowo.domain.order.valueobjects.*;
import com.wowo.wowo.domain.shared.valueobjects.Money;
import com.wowo.wowo.domain.transaction.valueobjects.TransactionId;
import com.wowo.wowo.model.Order;
import com.wowo.wowo.model.PaymentStatus;
import com.wowo.wowo.repository.ApplicationRepository;
import com.wowo.wowo.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * JPA implementation of OrderRepository
 * Maps between domain model and persistence model
 */
@Repository
@RequiredArgsConstructor
public class JpaOrderRepository implements OrderRepository {
    
    private final com.wowo.wowo.repository.OrderRepository springOrderRepository;
    private final ApplicationRepository applicationRepository;
    private final TransactionRepository transactionRepository;
    
    @Override
    public Optional<OrderAggregate> findById(OrderId orderId) {
        return springOrderRepository.findById(orderId.getValue())
            .map(this::toDomainModel);
    }
    
    @Override
    public void save(OrderAggregate orderAggregate) {
        Order order = toJpaEntity(orderAggregate);
        springOrderRepository.save(order);
    }
    
    @Override
    public List<OrderAggregate> findByApplicationId(ApplicationId applicationId) {
        return springOrderRepository.findByApplication_IdOrderByUpdatedDesc(
                applicationId.getValue(), 
                PageRequest.of(0, Integer.MAX_VALUE))
            .getContent()
            .stream()
            .map(this::toDomainModel)
            .collect(Collectors.toList());
    }
    
    @Override
    public List<OrderAggregate> findByStatus(OrderStatus status) {
        PaymentStatus paymentStatus = toPaymentStatus(status);
        return springOrderRepository.findAll()
            .stream()
            .filter(order -> order.getStatus() == paymentStatus)
            .map(this::toDomainModel)
            .collect(Collectors.toList());
    }
    
    @Override
    public List<OrderAggregate> findByApplicationIdAndStatus(ApplicationId applicationId, OrderStatus status) {
        PaymentStatus paymentStatus = toPaymentStatus(status);
        return springOrderRepository.findByApplication_IdOrderByUpdatedDesc(
                applicationId.getValue(), 
                PageRequest.of(0, Integer.MAX_VALUE))
            .getContent()
            .stream()
            .filter(order -> order.getStatus() == paymentStatus)
            .map(this::toDomainModel)
            .collect(Collectors.toList());
    }
    
    @Override
    public List<OrderAggregate> findAll(int page, int size) {
        return springOrderRepository.findAll(PageRequest.of(page, size))
            .getContent()
            .stream()
            .map(this::toDomainModel)
            .collect(Collectors.toList());
    }
    
    @Override
    public void delete(OrderId orderId) {
        springOrderRepository.deleteById(orderId.getValue());
    }
    
    /**
     * Convert JPA entity to domain model
     */
    private OrderAggregate toDomainModel(Order order) {
        OrderId orderId = OrderId.of(order.getId());
        ApplicationId applicationId = ApplicationId.of(order.getApplication().getId());
        Money amount = new Money(order.getMoney(), "VND");
        Money discountAmount = new Money(order.getDiscountMoney() != null ? order.getDiscountMoney() : 0L, "VND");
        OrderStatus status = toOrderStatus(order.getStatus());
        TransactionId transactionId = order.getTransaction() != null ? 
            TransactionId.of(order.getTransaction().getId()) : null;
        OrderUrls urls = new OrderUrls(order.getReturnUrl(), order.getSuccessUrl());
        
        return new OrderAggregate(
            orderId,
            applicationId,
            amount,
            discountAmount,
            status,
            transactionId,
            urls,
            order.getServiceName(),
            order.getCreated(),
            order.getUpdated()
        );
    }
    
    /**
     * Convert domain model to JPA entity
     */
    private Order toJpaEntity(OrderAggregate orderAggregate) {
        Order order = new Order();
        order.setId(orderAggregate.getId().getValue());
        
        // Load application
        applicationRepository.findById(orderAggregate.getApplicationId().getValue())
            .ifPresent(order::setApplication);
        
        order.setMoney(orderAggregate.getAmount().getAmount());
        order.setDiscountMoney(orderAggregate.getDiscountAmount().getAmount());
        order.setStatus(toPaymentStatus(orderAggregate.getStatus()));
        
        // Load transaction if exists
        if (orderAggregate.getTransactionId() != null) {
            transactionRepository.findById(orderAggregate.getTransactionId().getValue())
                .ifPresent(order::setTransaction);
        }
        
        order.setReturnUrl(orderAggregate.getUrls().getReturnUrl());
        order.setSuccessUrl(orderAggregate.getUrls().getSuccessUrl());
        order.setServiceName(orderAggregate.getServiceName());
        order.setCreated(orderAggregate.getCreatedAt());
        order.setUpdated(orderAggregate.getUpdatedAt());
        
        return order;
    }
    
    /**
     * Convert PaymentStatus to OrderStatus
     */
    private OrderStatus toOrderStatus(PaymentStatus paymentStatus) {
        return switch (paymentStatus) {
            case PENDING -> OrderStatus.PENDING;
            case SUCCESS -> OrderStatus.SUCCESS;
            case CANCELLED -> OrderStatus.CANCELLED;
            case FAIL -> OrderStatus.FAILED;
            case REFUNDED -> OrderStatus.REFUNDED;
        };
    }
    
    /**
     * Convert OrderStatus to PaymentStatus
     */
    private PaymentStatus toPaymentStatus(OrderStatus orderStatus) {
        return switch (orderStatus) {
            case PENDING -> PaymentStatus.PENDING;
            case SUCCESS -> PaymentStatus.SUCCESS;
            case CANCELLED -> PaymentStatus.CANCELLED;
            case FAILED -> PaymentStatus.FAIL;
            case REFUNDED -> PaymentStatus.REFUNDED;
        };
    }
}