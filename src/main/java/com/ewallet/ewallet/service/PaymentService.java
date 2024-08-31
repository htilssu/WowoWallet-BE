package com.ewallet.ewallet.service;

import com.ewallet.ewallet.models.PaymentRequest;
import com.ewallet.ewallet.models.Transaction;
import com.ewallet.ewallet.models.Wallet;
import com.ewallet.ewallet.exceptions.InsufficientBalanceException;
import com.ewallet.ewallet.exceptions.WalletNotFoundException;
import com.ewallet.ewallet.util.RequestUtil;
import com.ewallet.ewallet.repositories.WalletRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class PaymentService {

    private final WalletRepository walletRepository;
    private final TransactionService transactionService;

    public Transaction makePayment(PaymentRequest paymentRequest,
            Authentication authentication) throws
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

        RequestUtil.sendRequest(paymentRequest.getSuccessUrl(), "POST");
        RequestUtil.sendRequest(
                "https://voucher-server-alpha.vercel.app/api/vouchers/getVoucherByVoucherID/" + paymentRequest.getVoucherId(),
                "POST");
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
