package com.wowo.wowo.kafka.producer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wowo.wowo.data.dto.MessageType;
import com.wowo.wowo.data.dto.NotifyDTO;
import com.wowo.wowo.model.Transaction;
import com.wowo.wowo.model.UserWallet;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class NotifyProducer extends KafkaTemplate<String, String> {

    private final ObjectMapper objectMapper;

    public NotifyProducer(ProducerFactory<String, String> producerFactory,
            ObjectMapper objectMapper) {
        super(producerFactory);
        this.objectMapper = objectMapper;
    }

    public void pushNotifyMessage(Transaction transaction) {
        //TODO: send to user by id
        if (transaction.getReceiveWallet() instanceof UserWallet userWallet) {
            var id = userWallet.getUser()
                    .getId();
            NotifyDTO notifyDTO = NotifyDTO.builder()
                    .receiverId(id)
                    .message(
                            "Bạn vừa nhận được " + transaction.getAmount() + " VND")
                    .type(MessageType.RECEIVED_TRANSFER)
                    .build();
            try {
                this.send("notify", objectMapper.writeValueAsString(notifyDTO));
            } catch (JsonProcessingException e) {
                log.error("Error when serializing notify message: {}", e.getMessage());
            }
        }
    }
}


