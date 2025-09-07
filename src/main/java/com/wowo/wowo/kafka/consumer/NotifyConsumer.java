package com.wowo.wowo.kafka.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wowo.wowo.data.dto.NotifyDTO;
import com.wowo.wowo.service.NotifyService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutionException;

@Slf4j
@KafkaListener(topics = "notify", groupId = "wowo-wallet")
@Component
@AllArgsConstructor
public class NotifyConsumer {

    private final NotifyService notifyService;
    private final ObjectMapper objectMapper;

    @KafkaHandler
    public void handleNotifyMessage(String message) throws ExecutionException,
                                                           InterruptedException {
        try {
            final NotifyDTO notifyDTO = objectMapper.readValue(message, NotifyDTO.class);
            notifyService.notifyReceiveMoney(notifyDTO.getReceiverId(), notifyDTO)
                    .thenAccept(result -> {
                        log.info("Notify message processed: {}", message);
                    });
        } catch (JsonProcessingException e) {
            log.error("Error processing notify message: {}", message);
        }

    }
}
