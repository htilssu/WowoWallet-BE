package com.wowo.wowo.data.dto.response;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.io.Serializable;

/**
 * DTO for {@link com.wowo.wowo.models.GroupFund}
 */
public record GroupFundDto(Long id, @NotNull @Size(max = 255) String name,
                           @Size(max = 256) String image, @Size(max = 255) String description,
                           @NotNull @Min(0) Long balance, @NotNull Long target)
        implements Serializable {

}