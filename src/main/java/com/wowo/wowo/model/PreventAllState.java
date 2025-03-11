package com.wowo.wowo.model;

public class PreventAllState extends OrderState {

    public PreventAllState(Order order) {
        super(order);
    }

    @Override
    public void refund() throws Exception {
        throw new Exception("Cannot refund");
    }

    @Override
    public void cancel() throws Exception {
        throw new Exception("Cannot cancel");
    }
}
