package com.wowo.wowo.kafka.producer;

import com.wowo.wowo.data.dto.MessageType;
import com.wowo.wowo.data.dto.NotifyDTO;
import com.wowo.wowo.model.WalletTransaction;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.stereotype.Component;

@Component
public class NotifyProducer extends KafkaTemplate<String, NotifyDTO> {

    public NotifyProducer(ProducerFactory<String, NotifyDTO> producerFactory) {
        super(producerFactory);
    }

    public void sendNotifyMessage(NotifyDTO message) {
        this.send("notify", message);
    }

    public void pushNotifyMessage(WalletTransaction walletTransaction) {
        NotifyDTO notifyDTO = NotifyDTO.builder()
                .receiverId(walletTransaction.getReceiverUserWallet().getOwnerId())
                .message(
                        "Bạn vừa nhận được " + walletTransaction.getTransaction()
                                .getAmount() + " VND")
                .type(MessageType.RECEIVED_TRANSFER)
                .build();
        this.send("notify", notifyDTO);
    }
}


