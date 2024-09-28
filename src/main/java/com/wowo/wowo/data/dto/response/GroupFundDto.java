package com.wowo.wowo.data.dto.response;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class GroupFundDto {
    private String name;
    private String description;
    private BigDecimal balance;
    private BigDecimal target;
    private String ownerId;
}
