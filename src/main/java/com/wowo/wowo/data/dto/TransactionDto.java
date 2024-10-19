package com.wowo.wowo.data.dto;

import com.wowo.wowo.models.PaymentStatus;
import com.wowo.wowo.models.TransactionType;
import com.wowo.wowo.models.TransactionVariant;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;
import java.time.Instant;

/**
 * DTO for {@link com.wowo.wowo.models.Transaction}
 */
public record TransactionDto(String id, @NotNull Long amount, @NotNull PaymentStatus status,
                             @NotNull TransactionType type, TransactionVariant variant,
                             String description, @NotNull Instant created, @NotNull Instant updated)
        implements Serializable {

}