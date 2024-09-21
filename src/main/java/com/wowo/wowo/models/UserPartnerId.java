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
public class UserPartnerId implements Serializable {

    private static final long serialVersionUID = 4624909788687247315L;
    @Size(max = 10)
    @NotNull
    @Column(name = "user_id", nullable = false, length = 10)
    private String userId;

    @Size(max = 10)
    @NotNull
    @Column(name = "partner_id", nullable = false, length = 10)
    private String partnerId;

    @Override
    public int hashCode() {
        return Objects.hash(partnerId, userId);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        UserPartnerId entity = (UserPartnerId) o;
        return Objects.equals(this.partnerId, entity.partnerId) &&
                Objects.equals(this.userId, entity.userId);
    }

}