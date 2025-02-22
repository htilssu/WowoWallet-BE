package com.wowo.wowo.data.dto;

import com.wowo.wowo.model.FlowType;
import com.wowo.wowo.model.Transaction;
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
public final class TransactionDTO
        implements Serializable {

    @Serial
    private static final long serialVersionUID = 0L;
    private String id;
    private @NotNull Long amount;
    private String message;
    private String created;
    private String updated;
    private String type;
    private String receiverName;
    private String senderName;
    @NotNull
    private FlowType flowType;
}