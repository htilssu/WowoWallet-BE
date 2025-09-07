package com.wowo.wowo.domain.payment.events;

import com.wowo.wowo.domain.payment.valueobjects.ApplicationId;
import com.wowo.wowo.domain.payment.valueobjects.PaymentId;
import com.wowo.wowo.domain.shared.BaseDomainEvent;
import com.wowo.wowo.domain.shared.valueobjects.Money;
import lombok.Getter;

/**
 * Domain event raised when a payment is cancelled
 */
@Getter
public class PaymentCancelledEvent extends BaseDomainEvent {
    
    private final PaymentId paymentId;
    private final ApplicationId applicationId;
    private final Money amount;
    
    public PaymentCancelledEvent(PaymentId paymentId, ApplicationId applicationId, Money amount) {
        super("PaymentCancelled");
        this.paymentId = paymentId;
        this.applicationId = applicationId;
        this.amount = amount;
    }
}