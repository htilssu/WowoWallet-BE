package com.ewallet.ewallet.data.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Getter
@Builder
public class RefundResponse {

    TransactionResponse transaction;
    String message;
}
