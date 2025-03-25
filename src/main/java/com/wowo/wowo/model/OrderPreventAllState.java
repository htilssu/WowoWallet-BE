package com.wowo.wowo.model;

import org.apache.coyote.BadRequestException;

public class PreventAllState extends OrderState {

    public PreventAllState(Order order) {
        super(order);
    }

    @Override
    public void refund() throws BadRequestException {
        throw new BadRequestException("Không thể hoàn tiền");
    }

    @Override
    public void cancel() throws BadRequestException {
        throw new BadRequestException("Không thể hủy đơn hàng");
    }
}
