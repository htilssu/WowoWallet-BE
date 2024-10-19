package com.wowo.wowo.kafka.converters;

import com.wowo.wowo.kafka.messages.TransferMoneyMessage;
import com.google.gson.Gson;
import org.apache.kafka.common.serialization.Serializer;

/**
 * Serializer for TransferMoneyMessage objects.
 */
public class TransferMoneyMessageSerializer implements Serializer<TransferMoneyMessage> {

    @Override
    public byte[] serialize(String topic, TransferMoneyMessage data) {
        Gson gson = new Gson();
        String message = gson.toJson(data); // Serialize the TransferMoneyMessage object to JSON

        return message.getBytes();
    }
}
