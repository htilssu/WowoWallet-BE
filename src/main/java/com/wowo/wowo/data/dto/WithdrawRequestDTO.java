package com.wowo.wowo.data.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class WithdrawRequestDTO {

    @NotNull
    private Long groupId;
    @Min(0)
    @NotNull
    private Long amount;
    private String message;
}
