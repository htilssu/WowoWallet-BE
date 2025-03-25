package com.wowo.wowo.model;

import org.apache.coyote.BadRequestException;

public class OrderPendingState extends OrderState {

    public OrderPendingState(Order order) {super(order);}

    @Override
    public void refund() throws BadRequestException {
        throw new BadRequestException("Không thể hoàn tiền đơn hàng đang chờ xử lý");
    }

    @Override
    public void cancel() {
        order.setStatus(PaymentStatus.CANCELLED);
    }
}
