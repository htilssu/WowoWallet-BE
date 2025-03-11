package com.wowo.wowo.model;

public abstract class OrderState {

    protected OrderState(Order order) {
        this.order = order;
    }

    Order order;

    public abstract void refund() throws Exception;
    public abstract void cancel() throws Exception;
}
