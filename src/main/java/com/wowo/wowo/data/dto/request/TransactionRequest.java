package com.wowo.wowo.data.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class TransactionRequest {

    /**
     * Loại ví nhận tiền
     */
    String transactionTarget;
    /**
     * Người nhận tiền
     */
    String sendTo;
    @Min(value = 0, message = "Số tiền phải lớn hơn 0")
    long money;
    String type;
    String currency;
    /**
     * Loại giao dịch ví dụ như nạp tiền {@code topup}, chuyển tiền {@code transfer},
     * rút tiền {@code withdraw}
     */
    String transactionType;
    @NotNull
    @Size(max = 10)
    private String senderId;
    @NotNull(message = "Người nhận không được để trống")
    @Size(max = 10)
    private String receiverId;
    @Size(max = 20)
    private String senderType;
    @Size(max = 20)
    private String receiverType;
}
