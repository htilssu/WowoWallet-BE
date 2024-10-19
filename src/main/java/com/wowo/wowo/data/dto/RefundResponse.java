package com.wowo.wowo.data.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Getter
@Builder
public class RefundResponse {

    TransactionDto transaction;
    String message;
}
