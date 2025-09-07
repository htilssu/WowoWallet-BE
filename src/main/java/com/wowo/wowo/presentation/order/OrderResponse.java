package com.wowo.wowo.presentation.order;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;

/**
 * Response DTO for Order operations
 */
@Data
@Builder
public class OrderResponse {
    private Long id;
    private String applicationId;
    private BigDecimal amount;
    private BigDecimal discountAmount;
    private BigDecimal finalAmount;
    private String currency;
    private String status;
    private String transactionId;
    private String returnUrl;
    private String successUrl;
    private String serviceName;
    private Instant createdAt;
    private Instant updatedAt;
    private boolean canBeProcessed;
    private boolean canBeCancelled;
}