package com.wowo.wowo.kafka.producer;

import com.wowo.wowo.data.dto.MessageType;
import com.wowo.wowo.data.dto.NotifyDTO;
import com.wowo.wowo.model.Transaction;
import com.wowo.wowo.model.UserWallet;
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
            this.send("notify", notifyDTO);
        }
    }
}


