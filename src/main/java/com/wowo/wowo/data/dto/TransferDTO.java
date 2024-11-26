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
public class TransferDTO {

    @NotNull(message = "Nguồn chuyển không được để trống")
    private Long sourceId;
    @NotNull(message = "Người nhận không được để trống")
    private String receiverId;
    private Long targetId;
    @Min(value = 1000, message = "Số tiền phải lớn hơn 1000 VND")
    private Long money;
    private String message;
}