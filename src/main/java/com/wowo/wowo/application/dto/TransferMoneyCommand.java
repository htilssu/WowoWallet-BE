package com.wowo.wowo.application.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for money transfer requests.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransferMoneyCommand {
    
    private Long sourceWalletId;
    private Long targetWalletId;
    private Long amount;
    private String currency;
    private String senderName;
    private String receiverName;
    private String message;
}