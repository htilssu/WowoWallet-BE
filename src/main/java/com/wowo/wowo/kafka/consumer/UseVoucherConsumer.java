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
 *  * Created: 5-11-2024
 *  ******************************************************
 */

package com.wowo.wowo.kafka.consumer;

import com.wowo.wowo.kafka.message.UseVoucherMessage;
import com.wowo.wowo.service.OrderService;
import lombok.AllArgsConstructor;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@KafkaListener(topics = "useVoucher", groupId = "my-consumer")
@AllArgsConstructor
@Component
public class UseVoucherConsumer {

    private final OrderService orderService;

    @KafkaHandler
    public void consumer(UseVoucherMessage message) {
        orderService.useVoucher(message);
    }
}
