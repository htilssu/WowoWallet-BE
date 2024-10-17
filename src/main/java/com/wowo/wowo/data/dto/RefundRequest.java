package com.wowo.wowo.data.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class RefundRequest {
    String transactionId;
    String orderId;
}
