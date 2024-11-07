package com.wowo.wowo.kafka.consumers;

import com.wowo.wowo.data.dto.NotifyDto;
import com.wowo.wowo.services.NotifyService;
import lombok.AllArgsConstructor;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutionException;

@KafkaListener(topics = "notify", groupId = "wowo-wallet")
@Component
@AllArgsConstructor
public class NotifyConsumer {

    private final NotifyService notifyService;

    @KafkaHandler
    public void handleNotifyMessage(NotifyDto message) throws ExecutionException,
                                                              InterruptedException {
        notifyService.notifyReceiveMoney(message.getReceiverId(), message).thenAccept(_ -> {
            System.out.println("Received notify message: " + message);
        });
    }
}
