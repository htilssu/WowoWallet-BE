package com.wowo.wowo.presentation.payment;

import lombok.Value;

/**
 * Create payment request DTO
 */
@Value
public class CreatePaymentRequest {
    Long applicationId;
    Long amount;
    String currency;
    String serviceName;
    String returnUrl;
    String successUrl;
}