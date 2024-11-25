package com.wowo.wowo.kafka.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wowo.wowo.data.dto.TransferDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@KafkaListener(topics = "tm", groupId = "wowo-wallet")
@Component
public class TransferMoneyListener {

    private final ObjectMapper objectMapper;

    public TransferMoneyListener(ObjectMapper objectMapper) {this.objectMapper = objectMapper;}

    @KafkaHandler
    public void handleTransferMoneyMessage(String message) {
        try {
            final TransferDTO transferDTO = objectMapper.readValue(message, TransferDTO.class);
        } catch (JsonProcessingException e) {
            log.error("Error parsing transfer money message: {}",message);
        }
    }
}
