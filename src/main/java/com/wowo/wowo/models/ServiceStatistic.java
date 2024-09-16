package com.wowo.wowo.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Table(name = "service_statistic")
public class ServiceStatistic {

    @EmbeddedId
    private ServiceStatisticId id;

    @MapsId("service")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "service", nullable = false)
    private Service service;

    @NotNull
    @Column(name = "income", nullable = false)
    private BigDecimal income;

    @NotNull
    @Column(name = "outcome", nullable = false)
    private BigDecimal outcome;

}