package com.wowo.wowo.data.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PagingDTO {

    @Min(0)
    @NotNull
    private Integer offset = 20;
    @Min(0)
    @NotNull
    private Integer page = 0;
}
