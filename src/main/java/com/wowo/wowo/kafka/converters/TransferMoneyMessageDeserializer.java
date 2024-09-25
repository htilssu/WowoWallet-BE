package com.wowo.wowo.kafka.converters;

import com.wowo.wowo.kafka.messages.TransferMoneyMessage;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Deserializer;

@Slf4j
public class TransferMoneyMessageDeserializer implements Deserializer<TransferMoneyMessage> {

    @Override
    public TransferMoneyMessage deserialize(String topic, byte[] data) {
        String message = new String(data);

        try {
            Gson gson = new Gson();
            return gson.fromJson(message, TransferMoneyMessage.class);
        } catch (JsonSyntaxException e) {
            log.error("Error deserializing TransferMoneyMessage with data {}", message);
            return null;
        }
    }
}
