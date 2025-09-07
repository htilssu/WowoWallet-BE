package com.wowo.wowo.domain.payment.entity;

import com.wowo.wowo.domain.payment.events.PaymentCompletedEvent;
import com.wowo.wowo.domain.payment.events.PaymentCancelledEvent;
import com.wowo.wowo.domain.payment.events.PaymentRefundedEvent;
import com.wowo.wowo.domain.payment.valueobjects.*;
import com.wowo.wowo.domain.shared.BaseAggregateRoot;
import com.wowo.wowo.domain.shared.valueobjects.Money;
import com.wowo.wowo.exception.BadRequest;
import lombok.Getter;

import java.time.Instant;

/**
 * Payment Aggregate - Core business entity for payment operations
 * Ensures payment state consistency and enforces business rules
 */
@Getter
public class PaymentAggregate extends BaseAggregateRoot {
    
    private final PaymentId id;
    private final ApplicationId applicationId;
    private final Money amount;
    private final Money discountAmount;
    private final PaymentUrls paymentUrls;
    private final String serviceName;
    private PaymentStatus status;
    private TransactionId transactionId;
    private final Instant createdAt;
    private Instant updatedAt;
    
    // Constructor for new payments
    public PaymentAggregate(PaymentId id, ApplicationId applicationId, Money amount, 
                           PaymentUrls paymentUrls, String serviceName) {
        super();
        this.id = id;
        this.applicationId = applicationId;
        this.amount = amount;
        this.discountAmount = Money.zero(amount.getCurrency());
        this.paymentUrls = paymentUrls;
        this.serviceName = serviceName;
        this.status = PaymentStatus.PENDING;
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
    }
    
    // Constructor for loading from persistence
    public PaymentAggregate(PaymentId id, ApplicationId applicationId, Money amount, 
                           Money discountAmount, PaymentUrls paymentUrls, String serviceName,
                           PaymentStatus status, TransactionId transactionId, 
                           Instant createdAt, Instant updatedAt) {
        super();
        this.id = id;
        this.applicationId = applicationId;
        this.amount = amount;
        this.discountAmount = discountAmount != null ? discountAmount : Money.zero(amount.getCurrency());
        this.paymentUrls = paymentUrls;
        this.serviceName = serviceName;
        this.status = status;
        this.transactionId = transactionId;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
    
    /**
     * Apply voucher discount to the payment
     */
    public void applyDiscount(Money discountMoney) {
        if (!canModifyPayment()) {
            throw new BadRequest("Cannot apply discount to non-pending payment");
        }
        
        // Ensure discount doesn't exceed payment amount
        Money finalDiscount = discountMoney.isGreaterThan(amount) ? amount : discountMoney;
        
        // This would update the discount amount (implementation depends on your discount model)
        this.updatedAt = Instant.now();
    }
    
    /**
     * Complete the payment with transaction reference
     */
    public void completePayment(TransactionId transactionId) {
        if (!canModifyPayment()) {
            throw new BadRequest("Payment is not in a valid state for completion");
        }
        
        this.status = PaymentStatus.SUCCESS;
        this.transactionId = transactionId;
        this.updatedAt = Instant.now();
        
        // Raise domain event
        addDomainEvent(new PaymentCompletedEvent(this.getId(), applicationId, 
                                               getEffectiveAmount(), transactionId));
    }
    
    /**
     * Cancel the payment
     */
    public void cancel() {
        if (!canModifyPayment()) {
            throw new BadRequest("Payment cannot be cancelled in current state");
        }
        
        this.status = PaymentStatus.CANCELLED;
        this.updatedAt = Instant.now();
        
        addDomainEvent(new PaymentCancelledEvent(this.getId(), applicationId, amount));
    }
    
    /**
     * Refund the payment
     */
    public void refund(Money refundAmount) {
        if (!canRefund()) {
            throw new BadRequest("Payment cannot be refunded in current state");
        }
        
        if (refundAmount.isGreaterThan(getEffectiveAmount())) {
            throw new BadRequest("Refund amount cannot exceed payment amount");
        }
        
        this.status = PaymentStatus.REFUNDED;
        this.updatedAt = Instant.now();
        
        addDomainEvent(new PaymentRefundedEvent(this.getId(), applicationId, 
                                              refundAmount, transactionId));
    }
    
    /**
     * Get effective payment amount after discount
     */
    public Money getEffectiveAmount() {
        return amount.subtract(discountAmount);
    }
    
    /**
     * Check if payment can be modified (cancelled, completed, etc.)
     */
    public boolean canModifyPayment() {
        return status == PaymentStatus.PENDING;
    }
    
    /**
     * Check if payment can be refunded
     */
    public boolean canRefund() {
        return status == PaymentStatus.SUCCESS;
    }
    
    /**
     * Check if payment is in a final state
     */
    public boolean isFinalized() {
        return status == PaymentStatus.SUCCESS || 
               status == PaymentStatus.CANCELLED || 
               status == PaymentStatus.REFUNDED ||
               status == PaymentStatus.FAIL;
    }
}