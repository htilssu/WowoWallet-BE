package com.wowo.wowo.model;

public class RefundedState  extends  OrderState{

    public RefundedState(Order order) {
        super(order);
    }

    @Override
    public void refund() {

    }

    @Override
    public void cancel() {

    }
}
