package com.wowo.wowo.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

@Getter
@Setter
@Entity
@Table(name = "fund_member", indexes = {
        @Index(name = "fund_member_group_id_member_id_index", columnList = "group_id,member_id"),
        @Index(name = "fund_member_group_id_index", columnList = "group_id"),
})
public class FundMember {

    @EmbeddedId
    private FundMemberId id; // ID được nhúng từ FundMemberId

    @MapsId("groupId") // Ánh xạ tới groupId trong FundMemberId
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "group_id", nullable = false)
    private GroupFund group; // Quỹ nhóm mà thành viên này thuộc về

    @MapsId("memberId") // Ánh xạ tới trường memberId trong FundMemberId
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "member_id", nullable = false)
    private User member;

    @NotNull
    @ColumnDefault("0")
    @Column(name = "money", nullable = false)
    private Long money;
}