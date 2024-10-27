package com.wowo.wowo.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
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

    @Size(max = 100)
    @Column(name = "type")
    private String type;

    @Size(max = 255)
    @Column(name = "description")
    private String description;

    @NotNull
    @Column(name = "target", nullable = false)
    private Long target;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id")
    private User owner;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "wallet_id")
    private Wallet wallet;

    @OneToMany(mappedBy = "group")
    private Set<FundMember> fundMembers = new LinkedHashSet<>();

    @OneToMany(mappedBy = "group")
    private Set<GroupFundTransaction> groupFundTransactions = new LinkedHashSet<>();

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @Column(name = "created_date", nullable = false, updatable = false)
    private LocalDateTime createdDate = LocalDateTime.now();

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @Column(name = "target_date", nullable = false)
    private LocalDate targetDate;
}
