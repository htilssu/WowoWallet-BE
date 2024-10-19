package com.wowo.wowo.kafka.messages;

public record TransferMoneyMessage(String transferId, String receiverId, double amount) {

    public TransferMoneyMessage {
        if (transferId == null || receiverId == null || amount <= 0) {
            throw new IllegalArgumentException("Invalid TransferMoneyMessage");
        }
    }
}
