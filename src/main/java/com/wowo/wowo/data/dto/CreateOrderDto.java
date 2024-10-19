package com.wowo.wowo.data.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * DTO for {@link Order}
 */
public record CreateOrderDto(@Size(max = 15) String id, @NotNull @PositiveOrZero(
        message = "Số tiền phải lớn hơn hoặc bằng 0") Long money,
                             @Size(max = 300) String returnUrl, @Size(max = 300) String successUrl,
                             @Size(max = 100) String serviceName)
        implements Serializable {

}