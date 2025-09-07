package com.wowo.wowo.presentation.wallet;

import com.wowo.wowo.domain.transaction.entity.TransactionAggregate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

/**
 * Response DTO for transaction operations.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionResponse {
    
    private String id;
    private Long amount;
    private String currency;
    private String transactionType;
    private String senderName;
    private String receiverName;
    private String message;
    private Long sourceWalletId;
    private Long targetWalletId;
    private Instant created;
    
    public static TransactionResponse from(TransactionAggregate transaction) {
        return new TransactionResponse(
            transaction.getId(),
            transaction.getTransactionAmount().getAmount(),
            transaction.getTransactionAmount().getCurrency(),
            transaction.getTransactionType().name(),
            transaction.getSenderName(),
            transaction.getReceiverName(),
            transaction.getMessage(),
            transaction.getSourceWalletId().getValue(),
            transaction.getTargetWalletId().getValue(),
            transaction.getCreated()
        );
    }
}