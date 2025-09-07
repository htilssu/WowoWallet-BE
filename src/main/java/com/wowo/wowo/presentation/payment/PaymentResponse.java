package com.wowo.wowo.presentation.payment;

import com.wowo.wowo.domain.payment.entity.PaymentAggregate;
import lombok.Value;

import java.time.Instant;

/**
 * Payment response DTO for API
 */
@Value
public class PaymentResponse {
    Long id;
    Long applicationId;
    Long amount;
    Long discountAmount;
    String currency;
    String status;
    String serviceName;
    String returnUrl;
    String successUrl;
    String transactionId;
    Instant createdAt;
    Instant updatedAt;
    
    public static PaymentResponse from(PaymentAggregate payment) {
        return new PaymentResponse(
            payment.getId().value(),
            payment.getApplicationId().value(),
            payment.getAmount().getAmount(),
            payment.getDiscountAmount().getAmount(),
            payment.getAmount().getCurrency(),
            payment.getStatus().name(),
            payment.getServiceName(),
            payment.getPaymentUrls().returnUrl(),
            payment.getPaymentUrls().successUrl(),
            payment.getTransactionId() != null ? payment.getTransactionId().value() : null,
            payment.getCreatedAt(),
            payment.getUpdatedAt()
        );
    }
}