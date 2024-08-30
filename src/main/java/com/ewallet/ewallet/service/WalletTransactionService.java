package com.ewallet.ewallet.service;

import com.ewallet.ewallet.data.mapper.WalletTransactionMapper;
import com.ewallet.ewallet.models.Transaction;
import com.ewallet.ewallet.models.Wallet;
import com.ewallet.ewallet.models.WalletTransaction;
import com.ewallet.ewallet.repositories.WalletTransactionRepository;
import com.ewallet.ewallet.exceptions.TransactionNotFoundException;
import com.ewallet.ewallet.exceptions.InsufficientBalanceException;
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
