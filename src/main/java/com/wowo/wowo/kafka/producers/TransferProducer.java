package com.wowo.wowo.kafka.producers;

import com.wowo.wowo.data.dto.TransferDto;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.stereotype.Component;

@Component
public class TransferProducer extends KafkaTemplate<String, Object> {

    public TransferProducer(ProducerFactory<String, Object> producerFactory) {
        super(producerFactory);
    }

    public void sendTransferMessage(TransferDto message) {
        this.send("tm", message);
    }
}
