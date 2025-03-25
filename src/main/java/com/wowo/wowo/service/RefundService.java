package com.wowo.wowo.service;

import com.wowo.wowo.controller.OrderController;
import com.wowo.wowo.model.Order;
import com.wowo.wowo.model.Transaction;
import com.wowo.wowo.repository.OrderRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class RefundService {

    private final OrderRepository orderRepository;
    private final TransferService transferService;

    public Order refund(Order order, OrderController.RefundDTO refundDTO) throws Exception {

        order.refund();

        final Transaction walletTransaction = order.getTransaction();

        var senderWallet = walletTransaction.getSenderWallet();
        var receiverWallet = walletTransaction.getReceiveWallet();

        if (order.getDiscountMoney() < refundDTO.getAmount()) {
            throw new IllegalArgumentException(
                    "Số tiền hoàn trả không thể lớn hơn số tiền đã thanh toán");
        }

        var refundMoney =
                refundDTO.getAmount() == 0 ? order.getDiscountMoney() : refundDTO.getAmount();

        transferService.transferWithNoFee(receiverWallet, senderWallet,
                refundMoney);
        return orderRepository.save(order);
    }
}
