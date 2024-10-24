package com.wowo.wowo.data.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class TransactionHistoryParams {

    @Min(0)
    @NotNull
    private Integer offset;
    @Min(0)
    @NotNull
    private Integer page;
}
