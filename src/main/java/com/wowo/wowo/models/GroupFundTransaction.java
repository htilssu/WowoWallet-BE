package com.wowo.wowo.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "group_fund_transaction")
public class GroupFundTransaction {

    @Id
    @Size(max = 15)
    @Column(name = "transaction_id", nullable = false, length = 15)
    private String transactionId;

    @MapsId
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "transaction_id", nullable = false)
    private Transaction transaction;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id")
    private GroupFund group;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private User member;

    @NotNull
    @Column(name = "money", nullable = false)
    private BigDecimal money;

    @NotNull
    @ColumnDefault("CURRENT_DATE")
    @Column(name = "created", nullable = false)
    private LocalDate created;

}