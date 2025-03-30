package com.wowo.wowo.decorator;

import com.wowo.wowo.data.dto.WithdrawDTO;
import com.wowo.wowo.model.Wallet;
import com.wowo.wowo.service.WalletService;

public class BalanceCheckWithdrawDecorator extends WithdrawServiceDecorator {
    private final WalletService walletService;

    public BalanceCheckWithdrawDecorator(WithdrawService wrappedService, WalletService walletService) {
        super(wrappedService);
        this.walletService = walletService;
    }

    @Override
    public void withdraw(WithdrawDTO withdrawDTO) {
        Wallet wallet = walletService.getWallet(Long.valueOf(withdrawDTO.getSourceId()));
        if (wallet.getBalance() < withdrawDTO.getAmount()) {
            throw new IllegalStateException("Số dư không đủ để rút tiền");
        }
        super.withdraw(withdrawDTO); // Gọi đến service gốc nếu đủ số dư
    }
}
