package com.wowo.wowo.presentation.order;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

/**
 * Request DTOs for Order operations
 */
public class OrderRequestDTOs {
    
    @Data
    public static class CreateOrderRequest {
        @NotBlank(message = "Application ID is required")
        private String applicationId;
        
        @NotNull(message = "Amount is required")
        @Positive(message = "Amount must be positive")
        private Long amount;
        
        @NotBlank(message = "Currency is required")
        private String currency = "VND";
        
        private String returnUrl;
        private String successUrl;
        
        @NotBlank(message = "Service name is required")
        private String serviceName;
    }
    
    @Data
    public static class ApplyDiscountRequest {
        @NotNull(message = "Discount amount is required")
        @PositiveOrZero(message = "Discount amount must be positive or zero")
        private Long discountAmount;
        
        @NotBlank(message = "Currency is required")
        private String currency = "VND";
    }
    
    @Data
    public static class CompleteOrderRequest {
        @NotBlank(message = "Transaction ID is required")
        private String transactionId;
        
        @NotNull(message = "Paid amount is required")
        @Positive(message = "Paid amount must be positive")
        private Long paidAmount;
        
        @NotBlank(message = "Currency is required")
        private String currency = "VND";
    }
    
    @Data
    public static class RefundOrderRequest {
        @NotNull(message = "Refund amount is required")
        @Positive(message = "Refund amount must be positive")
        private Long refundAmount;
        
        @NotBlank(message = "Currency is required")
        private String currency = "VND";
    }
}