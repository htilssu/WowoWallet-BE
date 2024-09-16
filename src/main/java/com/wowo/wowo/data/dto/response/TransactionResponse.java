package com.wowo.wowo.data.dto.response;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TransactionResponse {

    double money;
    String currency;
    String transactionType;
    String transactionTarget;
    String status;
    String created;
    String updated;
    String receiverName;
    String returnUrl;
    String id;
    @NotNull
    @Size(max = 10)
    private String senderId;
    @NotNull
    @Size(max = 10)
    private String receiverId;
    @Size(max = 20)
    private String senderType;
    @Size(max = 20)
    private String receiverType;
}
