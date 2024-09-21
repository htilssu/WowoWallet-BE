package com.wowo.wowo.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "financial_statistic")
public class FinancialStatistic {

    @Id
    @Column(name = "id", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creator")
    private Employee creator;

    @NotNull
    @Column(name = "profit", nullable = false)
    private BigDecimal profit;

    @NotNull
    @Column(name = "income", nullable = false)
    private BigDecimal income;

    @NotNull
    @Column(name = "outcome", nullable = false)
    private BigDecimal outcome;

    @NotNull
    @Column(name = "total_money", nullable = false)
    private BigDecimal totalMoney;

    @NotNull
    @ColumnDefault("CURRENT_DATE")
    @Column(name = "created", nullable = false)
    private LocalDate created;

}