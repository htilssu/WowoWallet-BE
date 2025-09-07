package com.wowo.wowo.presentation.user;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;

/**
 * Response DTOs for User operations
 */
@Data
@Builder
public class UserResponse {
    private String id;
    private String email;
    private String firstName;
    private String lastName;
    private String fullName;
    private String phoneNumber;
    private boolean isActive;
    private boolean isVerified;
    private BigDecimal totalMoney;
    private String currency;
    private Instant createdAt;
    private Instant updatedAt;
    private boolean canPerformFinancialOperations;
}