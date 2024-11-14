package com.wowo.wowo.services;

import com.wowo.wowo.exceptions.InsufficientBalanceException;
import com.wowo.wowo.exceptions.TransactionNotFoundException;
import com.wowo.wowo.models.Transaction;
import com.wowo.wowo.models.Wallet;
import com.wowo.wowo.models.WalletTransaction;
import com.wowo.wowo.repositories.WalletTransactionRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class WalletTransactionService {

    private final WalletTransactionRepository walletTransactionRepository;
    private final YearAnalysisService yearAnalysisService;


    @Transactional
    public WalletTransaction createWalletTransaction(WalletTransaction walletTransaction) {
        if (walletTransaction.getTransaction() == null) {
            throw new RuntimeException("Không thể tạo giao dịch mà không có thông tin giao dịch");
        }

        yearAnalysisService.updateAnalysis(walletTransaction.getSenderWallet()
                        .getOwnerId(),
                0,
                walletTransaction.getTransaction()
                        .getAmount());
        yearAnalysisService.updateAnalysis(walletTransaction.getReceiverWallet()
                        .getOwnerId(),
                walletTransaction.getTransaction()
                        .getAmount(),
                0);

        try {
            return walletTransactionRepository.save(walletTransaction);
        } catch (Exception e) {
            throw new RuntimeException("Không thể tạo giao dịch");
        }
    }
}
