package com.wowo.wowo.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "group_fund")
public class GroupFund {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "group_fund_id_seq")
    @SequenceGenerator(name = "group_fund_id_seq", sequenceName = "group_fund_id_seq",
                       allocationSize = 1)
    @Column(name = "id", nullable = false)
    private Long id;

    @Size(max = 255)
    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @Size(max = 256)
    @Column(name = "image")
    private String image;

    @Size(max = 255)
    @Column(name = "description")
    private String description;

    @NotNull
    @Min(0)
    @Column(name = "balance", nullable = false, precision = 10)
    private Long balance;

    @NotNull
    @Column(name = "target", nullable = false, precision = 10)
    private Long target;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id")
    private User owner;

    @OneToMany(mappedBy = "group")
    private Set<FundMember> fundMembers = new LinkedHashSet<>();

    @OneToMany(mappedBy = "group")
    private Set<GroupFundTransaction> groupFundTransactions = new LinkedHashSet<>();

}