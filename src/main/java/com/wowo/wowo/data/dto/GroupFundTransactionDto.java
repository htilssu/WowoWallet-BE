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
    private String transaction_id;
    private TransactionDto transaction;
    private UserDto member;
    @NotNull
    private TransactionType transactionType;
}