package com.wowo.wowo.services;

import com.wowo.wowo.data.mapper.WalletTransactionMapper;
import com.wowo.wowo.models.Transaction;
import com.wowo.wowo.models.Wallet;
import com.wowo.wowo.models.WalletTransaction;
import com.wowo.wowo.repositories.WalletTransactionRepository;
import com.wowo.wowo.exceptions.TransactionNotFoundException;
import com.wowo.wowo.exceptions.InsufficientBalanceException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class WalletTransactionService {

    private final WalletTransactionMapper walletTransactionMapper;
    private final WalletService walletService;
    private final WalletTransactionRepository walletTransactionRepository;

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

        if (receiverWallet.getBalance() <= walletTransaction.getTransaction().getMoney()
                .doubleValue()) {
            throw new InsufficientBalanceException("Không đủ tiền để hoàn tiền");
        }

        receiverWallet.sendMoneyTo(senderWallet,
                walletTransaction.getTransaction().getMoney().doubleValue());

    }

    public void createWalletTransaction(Transaction transaction) {

    }

    public void createWalletTransaction(Transaction transaction, Wallet sender, Wallet receiver) {
        WalletTransaction walletTransaction = new WalletTransaction();
        walletTransaction.setTransaction(transaction);
        walletTransaction.setSenderWallet(sender);
        walletTransaction.setReceiverWallet(receiver);

        walletTransactionRepository.save(walletTransaction);
    }
}
