package com.wowo.wowo.kafka.producers;

import com.wowo.wowo.data.dto.NotifyDto;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.stereotype.Component;

@Component
public class NotifyProducer extends KafkaTemplate<String, NotifyDto> {

    public NotifyProducer(ProducerFactory<String, NotifyDto> producerFactory) {
        super(producerFactory);
    }

    public void sendNotifyMessage(NotifyDto message) {
        this.send("notify", message);
    }
}


