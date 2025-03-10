package com.wowo.wowo.model;

public abstract class OrderState {

    public OrderState(Order order) {
        this.order = order;
    }

    Order order;

    abstract void refund();
    abstract void cancel();
}
