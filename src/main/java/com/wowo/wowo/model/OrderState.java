package com.wowo.wowo.model;

import org.apache.coyote.BadRequestException;

public abstract class OrderState {

    protected OrderState(Order order) {
        this.order = order;
    }

    Order order;

    public abstract void refund() throws BadRequestException;
    public abstract void cancel() throws BadRequestException;
}