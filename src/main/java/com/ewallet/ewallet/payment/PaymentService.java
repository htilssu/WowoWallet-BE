package com.ewallet.ewallet.payment;

import com.ewallet.ewallet.dto.mapper.TransactionMapperImpl;
import com.ewallet.ewallet.models.PaymentRequest;
import com.ewallet.ewallet.models.Transaction;
import com.ewallet.ewallet.models.Wallet;
import com.ewallet.ewallet.service.TransactionService;
import com.ewallet.ewallet.transfer.exceptions.InsufficientBalanceException;
import com.ewallet.ewallet.transfer.exceptions.WalletNotFoundException;
import com.ewallet.ewallet.wallet.WalletRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PaymentService {

    private final WalletRepository walletRepository;
    private final TransactionService transactionService;
    private final TransactionMapperImpl transactionMapperImpl;

    public PaymentService(WalletRepository walletRepository,
            TransactionService transactionService, TransactionMapperImpl transactionMapperImpl) {
        this.walletRepository = walletRepository;
        this.transactionService = transactionService;
        this.transactionMapperImpl = transactionMapperImpl;
    }

    public Transaction makePayment(PaymentRequest paymentRequest, Authentication authentication) throws
                                                                                          InsufficientBalanceException,
                                                                                          WalletNotFoundException {
        final String userId = (String) authentication.getPrincipal();
        final Optional<Wallet> userWallet = walletRepository.findByOwnerIdAndOwnerType(userId,
                "user");
        if (userWallet.isEmpty()) {
            throw new WalletNotFoundException("Không tìm thấy ví của người dùng");
        }

        final Wallet sender = userWallet.get();
        final Wallet receiver = walletRepository.findByOwnerIdAndOwnerType(
                        paymentRequest.getPartner().getId(), "partner")
                .orElseThrow(() -> new WalletNotFoundException("Không tìm thấy ví của đối tác"));

        makePayment(sender, receiver, paymentRequest.getMoney().doubleValue());

        paymentRequest.setStatus("SUCCESS");

        return transactionService.createTransaction(userId, paymentRequest,
                sender, receiver);
    }

    public void makePayment(Wallet sender, Wallet receiver, double amount) throws
                                                                           InsufficientBalanceException {
        if (sender.getBalance() < amount) {
            throw new InsufficientBalanceException("Số dư không đủ");
        }

        sender.sendMoneyTo(receiver, amount);
        walletRepository.saveAll(List.of(sender, receiver));
    }
}
