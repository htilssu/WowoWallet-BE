package com.ewallet.ewallet.data.dto.request;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class PaymentRequestData {

    double money;
    String voucherId;
    String voucherName;
    String voucherCode;
    double voucherDiscount;
    String returnUrl;
    String cancelUrl;
    String success;
    String successUrl;
    String orderId;
    @Size(max = 100)
    private String serviceName;
}
