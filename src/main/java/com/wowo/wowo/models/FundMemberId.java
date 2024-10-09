package com.wowo.wowo.models;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.Hibernate;

import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@Embeddable
public class FundMemberId implements Serializable {

    private static final long serialVersionUID = -3026394821843277658L;
    @NotNull
    @Column(name = "group_id", nullable = false)
    private Integer groupId;

    @Size(max = 10)
    @NotNull
    @Column(name = "member_id", nullable = false, length = 10)
    private Long memberId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        FundMemberId entity = (FundMemberId) o;
        return Objects.equals(this.groupId, entity.groupId) &&
                Objects.equals(this.memberId, entity.memberId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(groupId, memberId);
    }

}