package com.wowo.wowo.data.dto;

import lombok.Data;

@Data
public class StatisticSummary {
    private Long totalMoneySuccess;
    private Long totalOrdersSuccess;
    private Long totalOrdersCancelled;
    private Long totalOrdersPending;
    private Long totalMoneyRefunded;
    private Long totalOrdersRefunded;
}
