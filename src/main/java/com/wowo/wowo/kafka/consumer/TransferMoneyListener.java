package com.wowo.wowo.kafka.consumer;

import com.wowo.wowo.data.dto.TransferDto;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@KafkaListener(topics = "tm", groupId = "wowo-wallet")
@Component
public class TransferMoneyListener {

    @KafkaHandler
    public void handleTransferMoneyMessage(TransferDto message) {
        System.out.println("Received transfer money message: " + message.toString());
    }
}
