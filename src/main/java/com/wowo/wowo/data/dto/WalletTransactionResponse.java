package com.wowo.wowo.data.dto;

import lombok.Data;

@Data
public class WalletTransactionResponse {
    WalletResponse senderWallet;
    WalletResponse receiverWallet;
    private String id;
    private int senderWalletId;
    private int receiverWalletId;
}
