package com.wowo.wowo.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Table(name = "fund_member")
public class FundMember {

    @EmbeddedId
    private FundMemberId id;

    @MapsId("groupId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "group_id", nullable = false)
    private GroupFund group;

    @MapsId("memberId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "member_id", nullable = false)
    private User member;

    @NotNull
    @ColumnDefault("0")
    @Column(name = "money", nullable = false)
    private BigDecimal money;

}