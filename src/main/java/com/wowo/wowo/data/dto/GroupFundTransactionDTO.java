package com.wowo.wowo.data.dto;

import com.wowo.wowo.model.TransactionType;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class GroupFundTransactionDTO {
    @NotNull(message = "Amount cannot be null")
    private String transaction_id;
    private TransactionDTO transaction;
    private UserDTO member;
    @NotNull
    private TransactionType type;
    private String description;
}