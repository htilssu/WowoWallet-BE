package com.wowo.wowo.kafka.consumers;

import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;

@KafkaListener(topics = "notify", groupId = "wowo-wallet")
public class NotifyConsumer {

    @KafkaHandler
    public void handleNotifyMessage(String message) {
        System.out.println("Received notify message: " + message);
    }
}
