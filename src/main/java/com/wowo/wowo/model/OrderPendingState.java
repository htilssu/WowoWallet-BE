package com.wowo.wowo.model;

public class OrderPendingState extends OrderState {

    public OrderPendingState(Order order) {super(order);}

    @Override
    public void refund() throws Exception {
        throw new Exception("Order is pending");
    }

    @Override
    public void cancel() throws Exception {
        order.setStatus(PaymentStatus.CANCELLED);
    }
}
