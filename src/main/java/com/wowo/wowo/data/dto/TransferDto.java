package com.wowo.wowo.data.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TransferDto {

    private String sourceId;
    private String senderId;
    @NotNull(message = "Người nhận không được để trống")
    private String receiverId;
    @Min(value = 0, message = "Số tiền phải lớn hơn hoặc bằng 0")
    private Long money;
    private String description;
}