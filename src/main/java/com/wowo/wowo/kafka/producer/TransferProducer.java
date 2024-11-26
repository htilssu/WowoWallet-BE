package com.wowo.wowo.kafka.producer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wowo.wowo.data.dto.TransferDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class TransferProducer extends KafkaTemplate<String, String> {

    private final ObjectMapper objectMapper;

    public TransferProducer(ProducerFactory<String, String> producerFactory,
            ObjectMapper objectMapper) {
        super(producerFactory);
        this.objectMapper = objectMapper;
    }

    public void sendTransferMessage(TransferDTO message) {
        final String content;
        try {
            content = objectMapper.writeValueAsString(message);
            this.send("tm", content);
        } catch (JsonProcessingException e) {
            log.error("Error while serializing transfer message: {}", e.getMessage());
        }
    }
}
