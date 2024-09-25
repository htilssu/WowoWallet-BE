package com.wowo.wowo.kafka.consumers;

import org.apache.kafka.clients.consumer.Consumer;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@KafkaListener(topics = "tm", groupId = "wowo-wallet")
@Component
public class TransferMoneyListener {

    @KafkaHandler
    public void handleTransferMoneyMessage(Consumer<String, String> message) {
        System.out.println("Received transfer money message: " + message.toString());
    }
}
