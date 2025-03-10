package com.wowo.wowo.service;

import com.wowo.wowo.controller.OrderController;
import com.wowo.wowo.model.FlowType;
import com.wowo.wowo.model.Order;
import com.wowo.wowo.model.PaymentStatus;
import com.wowo.wowo.model.Transaction;
import com.wowo.wowo.repository.OrderRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class RefundService {

    private final OrderRepository orderRepository;
    private final TransferService transferService;

    public Order refund(Order order, OrderController.RefundDTO refundDTO) {

        final Transaction walletTransaction = order.getTransaction();

        var senderWallet = walletTransaction.getSenderWallet();
        var receiverWallet = walletTransaction.getReceiveWallet();

        if (order.getDiscountMoney() < refundDTO.getAmount()) {
            throw new IllegalArgumentException(
                    "Số tiền hoàn trả không thể lớn hơn số tiền đã thanh toán");
        }

        if (refundDTO.getAmount() == 0) {
            transferService.transferWithNoFee(receiverWallet, senderWallet,
                    order.getDiscountMoney());
        }
        else {
            transferService.transferWithNoFee(receiverWallet, senderWallet,
                    refundDTO.getAmount());
        }

        final Transaction refundTransaction = walletTransaction.clone();
        refundTransaction.setSenderWallet(receiverWallet);
        refundTransaction.setReceiveWallet(senderWallet);
        refundTransaction.setReceiverName(refundTransaction.getSenderName());
        refundTransaction.setSenderName(refundTransaction.getReceiverName());
        refundTransaction.setFlowType(FlowType.REFUND);

        order.setRefundTransaction(refundTransaction);
        order.setStatus(PaymentStatus.REFUNDED);
        return orderRepository.save(order);
    }
}
