package com.wowo.wowo.service;

import com.wowo.wowo.model.Order;
import com.wowo.wowo.model.PaymentStatus;
import com.wowo.wowo.model.WalletTransaction;
import com.wowo.wowo.repository.OrderRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class RefundService {

    private final OrderRepository orderRepository;
    private final TransferService transferService;

    public Order refund(Order order) {

        final WalletTransaction walletTransaction = order.getTransaction().getWalletTransaction();

        var senderWallet = walletTransaction.getSenderUserWallet();
        var receiverWallet = walletTransaction.getReceiverUserWallet();

        transferService.transfer(receiverWallet, senderWallet, order.getTransaction().getAmount());

        order.setStatus(PaymentStatus.REFUNDED);
        return orderRepository.save(order);
    }
}
