package com.wowo.wowo.data.dto;

import com.wowo.wowo.models.TransactionType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class GroupFundTransactionDto {

    @NotNull(message = "Member ID cannot be null")
    private String memberId;

    @NotNull(message = "Amount cannot be null")
    @Positive(message = "Amount must be positive")
    private Long money;

    @NotNull(message = "Transaction Type cannot be null")
    private TransactionType transactionType;

    // Constructor, Getters and Setters có thể được tạo tự động bởi Lombok nếu cần
}