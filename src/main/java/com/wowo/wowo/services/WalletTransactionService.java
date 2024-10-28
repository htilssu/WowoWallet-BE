package com.wowo.wowo.services;

import com.wowo.wowo.exceptions.InsufficientBalanceException;
import com.wowo.wowo.exceptions.TransactionNotFoundException;
import com.wowo.wowo.models.Transaction;
import com.wowo.wowo.models.Wallet;
import com.wowo.wowo.models.WalletTransaction;
import com.wowo.wowo.repositories.TransactionRepository;
import com.wowo.wowo.repositories.WalletTransactionRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class WalletTransactionService {

    private final WalletService walletService;
    private final WalletTransactionRepository walletTransactionRepository;
    private final TransactionRepository transactionRepository;
    private final TransactionService transactionService;

    /**
     * Hoàn tiền cho giao dịch qua ví
     * nếu không tìm thấy giao dịch thì sẽ trả về {@link TransactionNotFoundException},
     * hoặc nếu tiền của người nhận tiền không đủ số dư để hoàn thì sẽ báo lỗi
     * {@link InsufficientBalanceException}
     *
     * @param transaction giao dịch cần hoàn tiền
     *
     * @throws TransactionNotFoundException nếu không tìm thấy giao dịch
     * @throws InsufficientBalanceException nếu không đủ tiền để hoàn tiền
     */
    public void refund(Transaction transaction) throws TransactionNotFoundException,
                                                       InsufficientBalanceException {
        var walletTransaction = walletTransactionRepository.findById(transaction.getId()).orElse(
                null);
        if (walletTransaction == null) {
            throw new TransactionNotFoundException("Không thể tìm thấy giao dịch");
        }

        final Wallet senderWallet = walletTransaction.getSenderWallet();
        final Wallet receiverWallet = walletTransaction.getReceiverWallet();

        if (receiverWallet.getBalance() <= walletTransaction.getTransaction().getAmount()
                .doubleValue()) {
            throw new InsufficientBalanceException("Không đủ tiền để hoàn tiền");
        }
//        TODO: refund transaction

    }

    @Transactional
    public WalletTransaction createWalletTransaction(WalletTransaction walletTransaction) {
        if (walletTransaction.getTransaction() == null) {
            throw new RuntimeException("Không thể tạo giao dịch mà không có thông tin giao dịch");
        }

        try {
            return walletTransactionRepository.save(walletTransaction);
        } catch (Exception e) {
            throw new RuntimeException("Không thể tạo giao dịch");
        }
    }
}
