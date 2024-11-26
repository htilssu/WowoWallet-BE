package com.wowo.wowo.data.dto;

import com.wowo.wowo.constant.Constant;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PaymentDTO {

    private Long orderId;
    @NotNull
    private String sourceId;
    @NotNull
    private Constant.PaymentService paymentService;
}
