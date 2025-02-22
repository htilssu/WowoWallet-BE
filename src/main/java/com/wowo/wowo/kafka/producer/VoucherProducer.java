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
 *  * Created: 10-11-2024
 *  ******************************************************
 */

package com.wowo.wowo.kafka.producer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wowo.wowo.kafka.message.VoucherMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class VoucherProducer extends KafkaTemplate<String, String> {

    private final ObjectMapper objectMapper;

    public VoucherProducer(ProducerFactory<String, String> producerFactory,
            ObjectMapper objectMapper) {
        super(producerFactory);
        this.objectMapper = objectMapper;
    }

    public void sendVoucherMessage(VoucherMessage message) {
        try {
            final String deserializeContent = objectMapper.writeValueAsString(message);
            this.send("voucher", deserializeContent);
        } catch (Exception e) {
            log.error("Error while sending voucher message: {}", e.getMessage());
        }
    }
}
