package com.wowo.wowo.data.dto;

import com.wowo.wowo.model.OrderItem;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;

/**
 * DTO for {@link OrderItem}
 */
public record OrderItemCreateDto(@NotNull String name, @Min(1) Long amount, @Min(0) Long unitPrice)
        implements Serializable {

}