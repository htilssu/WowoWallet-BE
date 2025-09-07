package com.wowo.wowo.domain.payment.events;

import com.wowo.wowo.domain.payment.valueobjects.ApplicationId;
import com.wowo.wowo.domain.payment.valueobjects.PaymentId;
import com.wowo.wowo.domain.payment.valueobjects.TransactionId;
import com.wowo.wowo.domain.shared.BaseDomainEvent;
import com.wowo.wowo.domain.shared.valueobjects.Money;
import lombok.Getter;

/**
 * Domain event raised when a payment is refunded
 */
@Getter
public class PaymentRefundedEvent extends BaseDomainEvent {
    
    private final PaymentId paymentId;
    private final ApplicationId applicationId;
    private final Money refundAmount;
    private final TransactionId originalTransactionId;
    
    public PaymentRefundedEvent(PaymentId paymentId, ApplicationId applicationId, 
                               Money refundAmount, TransactionId originalTransactionId) {
        super("PaymentRefunded");
        this.paymentId = paymentId;
        this.applicationId = applicationId;
        this.refundAmount = refundAmount;
        this.originalTransactionId = originalTransactionId;
    }
}