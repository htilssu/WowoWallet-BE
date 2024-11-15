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

package com.wowo.wowo.kafka.producers;

import com.wowo.wowo.kafka.messages.VoucherMessage;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.stereotype.Component;

@Component
public class VoucherProducer extends KafkaTemplate<String, String> {

    public VoucherProducer(ProducerFactory<String, String> producerFactory) {
        super(producerFactory);
    }

    public void sendVoucherMessage(String message) {
        this.send("voucher", message);
    }
}
