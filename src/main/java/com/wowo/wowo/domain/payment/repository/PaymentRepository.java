package com.wowo.wowo.domain.payment.repository;

import com.wowo.wowo.domain.payment.entity.PaymentAggregate;
import com.wowo.wowo.domain.payment.valueobjects.PaymentId;

import java.util.Optional;

/**
 * Domain repository interface for Payment operations
 */
public interface PaymentRepository {
    
    /**
     * Save payment aggregate
     */
    PaymentAggregate save(PaymentAggregate payment);
    
    /**
     * Find payment by ID
     */
    Optional<PaymentAggregate> findById(PaymentId paymentId);
    
    /**
     * Find payment by ID or throw exception
     */
    PaymentAggregate findByIdOrThrow(PaymentId paymentId);
    
    /**
     * Check if payment exists
     */
    boolean existsById(PaymentId paymentId);
    
    /**
     * Generate next payment ID
     */
    PaymentId nextId();
}