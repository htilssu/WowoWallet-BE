/*
 * ******************************************************
 *  * Copyright (c) 2024 htilssu
 *  *
 *  * This code is the property of htilssu. All rights reserved.
 *  * Redistribution or reproduction of any part of this code
 *  * in any form, with or without modification, is strictly
 *  * prohibited without prior written permission from the author.
 *  *
 *  * Author: htilssu
 *  * Created: 15-11-2024
 *  ******************************************************
 */

package com.wowo.wowo;

import com.wowo.wowo.model.Order;
import com.wowo.wowo.model.PaymentStatus;
import com.wowo.wowo.service.OrderService;
import lombok.AllArgsConstructor;

import java.util.concurrent.Callable;

@AllArgsConstructor
public class CheckOrderTask implements Callable<Void> {

    private final Order order;
    private final OrderService orderService;

    @Override
    public Void call() {
        final Order orderInDb = orderService.getOrderOrThrow(order.getId());
        if (orderInDb.getStatus() == PaymentStatus.PENDING) {
            orderService.cancelOrder(orderInDb);
        }

        return null;
    }
}
