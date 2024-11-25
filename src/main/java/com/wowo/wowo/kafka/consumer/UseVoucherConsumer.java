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

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wowo.wowo.kafka.message.UseVoucherMessage;
import com.wowo.wowo.service.OrderService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@KafkaListener(topics = "useVoucher", groupId = "my-consumer")
@AllArgsConstructor
@Component
@Slf4j
public class UseVoucherConsumer {

    private final OrderService orderService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @KafkaHandler
    public void consumer(String message) {

        try {
            var useVoucher = objectMapper.readValue(message, UseVoucherMessage.class);
            orderService.useVoucher(useVoucher);
        } catch (JacksonException e) {
            log.error("Error deserializing message: {}", message);
        }
    }
}
