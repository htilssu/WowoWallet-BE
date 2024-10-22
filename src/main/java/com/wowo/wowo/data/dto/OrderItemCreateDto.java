package com.wowo.wowo.data.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;

/**
 * DTO for {@link com.wowo.wowo.mongo.documents.OrderItem}
 */
public record OrderItemCreateDto(@NotNull String name, @Min(1) Long amount, @Min(0) Long unitPrice)
        implements Serializable {

}