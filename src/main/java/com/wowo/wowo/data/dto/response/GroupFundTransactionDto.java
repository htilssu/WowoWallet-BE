package com.wowo.wowo.data.dto.response;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class GroupFundTransactionDto {
    private String memberId;
    private BigDecimal money;
}
