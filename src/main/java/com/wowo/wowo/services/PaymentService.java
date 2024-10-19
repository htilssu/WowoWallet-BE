package com.wowo.wowo.services;

import com.wowo.wowo.exceptions.InsufficientBalanceException;
import com.wowo.wowo.exceptions.WalletNotFoundException;
import com.wowo.wowo.models.Order;
import com.wowo.wowo.models.PaymentStatus;
import com.wowo.wowo.models.Transaction;
import com.wowo.wowo.models.Wallet;
import com.wowo.wowo.repositories.WalletRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class PaymentService {

    private final WalletRepository walletRepository;
    private final TransactionService transactionService;
    private final OrderService orderService;
    private final TransferService transferService;

    public Transaction makePayment(Order order,
            Authentication authentication) throws
                                           InsufficientBalanceException,
                                           WalletNotFoundException {

    }

    public void makePayment(Wallet sender, Wallet receiver, long amount) throws
                                                                         InsufficientBalanceException {
        if (sender.getBalance() < amount) {
            throw new InsufficientBalanceException("Số dư không đủ");
        }

        sender.sendMoney(receiver, amount);
        walletRepository.saveAll(List.of(sender, receiver));
    }

    public void makePayment(String id, String sourceId) {
        var order = orderService.getById(id);
        if (order.getStatus() != PaymentStatus.PENDING) {
            throw new RuntimeException("Giao dịch đã được xử lý");
        }

        var senderWallet = walletRepository.findByOwnerId(sourceId).orElse(null);
        if (senderWallet == null) {
            throw new WalletNotFoundException("Không tìm thấy ví nguồn");
        }
        var receiverWallet = walletRepository.findByOwnerId(order.getOwnerId()).orElse(null);
        if (receiverWallet == null) {
            throw new WalletNotFoundException("Không tìm thấy ví người nhận");
        }

        transferService.transferMoney();
    }
}
