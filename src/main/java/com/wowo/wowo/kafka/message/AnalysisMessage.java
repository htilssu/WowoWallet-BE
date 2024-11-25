package com.wowo.wowo.kafka.message;

import lombok.Data;

import java.util.Objects;

@Data
public final class AnalysisMessage {

    private final String transferId;
    private final String receiverId;
    private final double amount;
}
