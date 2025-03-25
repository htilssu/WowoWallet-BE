package com.wowo.wowo.model;

import org.apache.coyote.BadRequestException;

public class OrderSuccessState extends OrderState {

    public OrderSuccessState(Order order) {super(order);}

    @Override
    public void refund() {
        final Transaction walletTransaction = order.getTransaction();

        var senderWallet = walletTransaction.getSenderWallet();
        var receiverWallet = walletTransaction.getReceiveWallet();

        final Transaction refundTransaction = walletTransaction.clone();
        refundTransaction.setId(null);
        refundTransaction.setSenderWallet(receiverWallet);
        refundTransaction.setReceiveWallet(senderWallet);
        refundTransaction.setReceiverName(refundTransaction.getSenderName());
        refundTransaction.setSenderName(refundTransaction.getReceiverName());
        refundTransaction.setFlowType(FlowType.REFUND);

        order.setRefundTransaction(refundTransaction);
        order.setStatus(PaymentStatus.REFUNDED);
    }

    @Override
    public void cancel() throws BadRequestException {
        throw new BadRequestException("Không thể hủy đơn hàng đã thanh toán");
    }
}
