package com.wowo.wowo.model;

public class RefundedState  extends  OrderState{

    public RefundedState(Order order) {
        super(order);
    }

    @Override
    void refund() {

    }

    @Override
    void cancel() {

    }
}
