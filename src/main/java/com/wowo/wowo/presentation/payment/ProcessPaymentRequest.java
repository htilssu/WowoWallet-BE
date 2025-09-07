package com.wowo.wowo.presentation.payment;

import lombok.Value;

/**
 * Process payment request DTO
 */
@Value
public class ProcessPaymentRequest {
    Long userWalletId;
    Long applicationWalletId;
    String userName;
    String applicationName;
}