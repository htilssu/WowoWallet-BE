package com.wowo.wowo.data.dto;

import com.wowo.wowo.models.PaymentStatus;
import com.wowo.wowo.models.Transaction;
import com.wowo.wowo.models.TransactionType;
import com.wowo.wowo.models.TransactionVariant;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * DTO for {@link Transaction}
 */
@Data
@AllArgsConstructor
public final class TransactionDto
        implements Serializable {

    @Serial
    private static final long serialVersionUID = 0L;
    private String id;
    private @NotNull Long amount;
    private @NotNull PaymentStatus status;
    private @NotNull TransactionType type;
    private TransactionVariant variant;
    private String description;
    private @NotNull String created;
    private @NotNull String updated;
    private ReceiverDto receiver;
}