package com.wowo.wowo.data.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

import java.io.Serializable;
import java.util.Collection;

/**
 * DTO for {@link com.wowo.wowo.model.Order}
 */

public record OrderCreationDTO(@NotNull @PositiveOrZero(
        message = "Số tiền phải lớn hơn hoặc bằng 0") Long money,
                               @NotNull Collection<OrderItemCreationDTO> items,
                               @Size(max = 300) String returnUrl, @Size(max = 300) String successUrl,
                               @Size(max = 100) String serviceName)
        implements Serializable {

}