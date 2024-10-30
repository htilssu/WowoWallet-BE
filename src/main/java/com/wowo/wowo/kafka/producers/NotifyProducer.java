package com.wowo.wowo.kafka.producers;

import com.wowo.wowo.data.dto.MessageType;
import com.wowo.wowo.data.dto.NotifyDto;
import com.wowo.wowo.models.WalletTransaction;
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

    public void pushNotifyMessage(WalletTransaction walletTransaction) {
        NotifyDto notifyDto = NotifyDto.builder()
                .receiverId(walletTransaction.getReceiverWallet().getOwnerId())
                .message(
                        "Bạn vừa nhận được" + walletTransaction.getTransaction()
                                .getAmount() + " VND")
                .type(MessageType.RECEIVED_TRANSFER)
                .build();
        this.send("notify", notifyDto);
    }
}


