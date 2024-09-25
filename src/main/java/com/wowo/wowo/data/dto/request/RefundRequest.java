package com.wowo.wowo.data.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class RefundRequest {
    String transactionId;
    String orderId;
}
