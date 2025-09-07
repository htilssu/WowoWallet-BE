package com.wowo.wowo.domain.payment.events;

import com.wowo.wowo.domain.payment.valueobjects.ApplicationId;
import com.wowo.wowo.domain.payment.valueobjects.PaymentId;
import com.wowo.wowo.domain.payment.valueobjects.TransactionId;
import com.wowo.wowo.domain.shared.BaseDomainEvent;
import com.wowo.wowo.domain.shared.valueobjects.Money;
import lombok.Getter;

import java.time.Instant;

/**
 * Domain event raised when a payment is completed
 */
@Getter
public class PaymentCompletedEvent extends BaseDomainEvent {
    
    private final PaymentId paymentId;
    private final ApplicationId applicationId;
    private final Money amount;
    private final TransactionId transactionId;
    
    public PaymentCompletedEvent(PaymentId paymentId, ApplicationId applicationId, 
                                Money amount, TransactionId transactionId) {
        super("PaymentCompleted");
        this.paymentId = paymentId;
        this.applicationId = applicationId;
        this.amount = amount;
        this.transactionId = transactionId;
    }
}