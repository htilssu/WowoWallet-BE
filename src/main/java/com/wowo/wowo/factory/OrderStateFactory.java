package com.wowo.wowo.factory;

import com.wowo.wowo.model.*;

public class OrderStateFactory {

    private OrderStateFactory() {
    }

    public static OrderState getOrderState(Order order) {
        PaymentStatus status = order.getStatus();

        return switch (status) {
            case SUCCESS -> new OrderSuccessState(order);
            case PENDING -> new OrderPendingState(order);
            case CANCELLED, REFUNDED, FAIL -> new PreventAllState(order);
        };
    }
}