package com.wowo.wowo.infrastructure.payment;

import com.wowo.wowo.domain.payment.entity.PaymentAggregate;
import com.wowo.wowo.domain.payment.repository.PaymentRepository;
import com.wowo.wowo.domain.payment.valueobjects.*;
import com.wowo.wowo.domain.shared.valueobjects.Money;
import com.wowo.wowo.exception.NotFoundException;
import com.wowo.wowo.model.Order;
import com.wowo.wowo.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * JPA implementation of PaymentRepository
 * Maps between domain Payment aggregate and JPA Order entity
 */
@Repository
@RequiredArgsConstructor
public class JpaPaymentRepository implements PaymentRepository {
    
    private final OrderRepository orderRepository;
    
    @Override
    public PaymentAggregate save(PaymentAggregate payment) {
        Order order = mapToEntity(payment);
        Order savedOrder = orderRepository.save(order);
        return mapToDomain(savedOrder);
    }
    
    @Override
    public Optional<PaymentAggregate> findById(PaymentId paymentId) {
        return orderRepository.findById(paymentId.value())
                .map(this::mapToDomain);
    }
    
    @Override
    public PaymentAggregate findByIdOrThrow(PaymentId paymentId) {
        return findById(paymentId)
                .orElseThrow(() -> new NotFoundException("Payment not found: " + paymentId.value()));
    }
    
    @Override
    public boolean existsById(PaymentId paymentId) {
        return orderRepository.existsById(paymentId.value());
    }
    
    @Override
    public PaymentId nextId() {
        // For JPA auto-generated IDs, we'll return a placeholder
        // The actual ID will be generated when saving
        return PaymentId.of(0L);
    }
    
    /**
     * Map JPA Order entity to Payment domain aggregate
     */
    private PaymentAggregate mapToDomain(Order order) {
        return new PaymentAggregate(
            PaymentId.of(order.getId()),
            ApplicationId.of(order.getApplication().getId()),
            new Money(order.getMoney(), "VND"), // Assuming VND currency for now
            order.getDiscountMoney() != null ? new Money(order.getDiscountMoney(), "VND") : Money.zero("VND"),
            PaymentUrls.of(order.getReturnUrl(), order.getSuccessUrl()),
            order.getServiceName(),
            mapToPaymentStatus(order.getStatus()),
            order.getTransaction() != null ? TransactionId.of(order.getTransaction().getId()) : null,
            order.getCreated(),
            order.getUpdated()
        );
    }
    
    /**
     * Map Payment domain aggregate to JPA Order entity
     */
    private Order mapToEntity(PaymentAggregate payment) {
        Order order = new Order();
        
        // Only set ID if it's not the placeholder (0)
        if (payment.getId().value() > 0) {
            order.setId(payment.getId().value());
        }
        
        // Note: Application entity would need to be loaded separately
        // This is a simplified mapping - in real implementation you'd need to handle relationships properly
        order.setMoney(payment.getAmount().getAmount());
        order.setDiscountMoney(payment.getDiscountAmount().getAmount());
        order.setServiceName(payment.getServiceName());
        order.setReturnUrl(payment.getPaymentUrls().returnUrl());
        order.setSuccessUrl(payment.getPaymentUrls().successUrl());
        order.setStatus(mapToOrderStatus(payment.getStatus()));
        
        return order;
    }
    
    /**
     * Map Order payment status to domain payment status
     */
    private PaymentStatus mapToPaymentStatus(com.wowo.wowo.model.PaymentStatus orderStatus) {
        return switch (orderStatus) {
            case PENDING -> PaymentStatus.PENDING;
            case SUCCESS -> PaymentStatus.SUCCESS;
            case CANCELLED -> PaymentStatus.CANCELLED;
            case FAIL -> PaymentStatus.FAIL;
            case REFUNDED -> PaymentStatus.REFUNDED;
        };
    }
    
    /**
     * Map domain payment status to Order payment status
     */
    private com.wowo.wowo.model.PaymentStatus mapToOrderStatus(PaymentStatus paymentStatus) {
        return switch (paymentStatus) {
            case PENDING -> com.wowo.wowo.model.PaymentStatus.PENDING;
            case SUCCESS -> com.wowo.wowo.model.PaymentStatus.SUCCESS;
            case CANCELLED -> com.wowo.wowo.model.PaymentStatus.CANCELLED;
            case FAIL -> com.wowo.wowo.model.PaymentStatus.FAIL;
            case REFUNDED -> com.wowo.wowo.model.PaymentStatus.REFUNDED;
        };
    }
}