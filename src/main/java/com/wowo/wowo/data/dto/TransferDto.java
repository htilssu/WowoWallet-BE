package com.wowo.wowo.data.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransferDto {

    private String sourceId;
    private String senderId;
    @NotNull(message = "Người nhận không được để trống")
    private String receiverId;
    @Min(value = 1, message = "Số tiền phải lớn hơn 0")
    private Long money;
    private String description;
}