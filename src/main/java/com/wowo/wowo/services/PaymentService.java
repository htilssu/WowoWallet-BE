package com.wowo.wowo.services;

import com.wowo.wowo.exceptions.InsufficientBalanceException;
import com.wowo.wowo.exceptions.WalletNotFoundException;
import com.wowo.wowo.models.Order;
import com.wowo.wowo.models.Transaction;
import com.wowo.wowo.models.Wallet;
import com.wowo.wowo.util.RequestUtil;
import com.wowo.wowo.repositories.WalletRepository;
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

    public Transaction makePayment(Order order,
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
                        order.getPartner().getId(), "partner")
                .orElseThrow(() -> new WalletNotFoundException("Không tìm thấy ví của đối tác"));

        makePayment(sender, receiver, order.getMoney().doubleValue());

        order.setStatus("SUCCESS");

        RequestUtil.sendRequest(order.getSuccessUrl(), "POST");
        RequestUtil.sendRequest(
                "https://voucher-server-alpha.vercel.app/api/vouchers/getVoucherByVoucherID/" + order.getVoucherId(),
                "POST");
        return transactionService.createTransaction(userId, order,
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
