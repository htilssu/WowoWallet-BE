package com.wowo.wowo.data.dto;

import com.wowo.wowo.constants.Constant;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PaymentDto {

    private String orderId;
    @NotNull
    private String sourceId;
    @NotNull
    private Constant.PaymentService paymentService;
}
