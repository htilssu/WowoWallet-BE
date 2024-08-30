package com.ewallet.ewallet.data.dto.response;

import lombok.Data;

@Data
public class WalletTransactionResponse {
    WalletResponse senderWallet;
    WalletResponse receiverWallet;
    private String id;
    private int senderWalletId;
    private int receiverWalletId;
}
