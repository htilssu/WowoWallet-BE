package com.wowo.wowo.data.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * DTO for {@link WalletTransaction}
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class WalletTransactionDto implements Serializable {

    @Size(max = 15)
    private String id;
    private TransactionResponse transaction;
    private Integer senderWalletId;
    private String senderWalletOwnerType;
    private String senderWalletCurrency;
    private String senderWalletOwnerId;
    private double senderWalletBalance;
    private Integer receiverWalletId;
    private String receiverWalletOwnerType;
    private String receiverWalletCurrency;
    private String receiverWalletOwnerId;
}