package com.wowo.wowo.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.proxy.HibernateProxy;

import java.util.Objects;

@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@DiscriminatorValue("USER")
public class UserWallet extends Wallet {

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @ToString.Exclude
    private User user;

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy
               ? ((HibernateProxy) this).getHibernateLazyInitializer()
                       .getPersistentClass()
                       .hashCode() : getClass().hashCode();
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy
                                   ? ((HibernateProxy) o).getHibernateLazyInitializer()
                                           .getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy
                                      ? ((HibernateProxy) this).getHibernateLazyInitializer()
                                              .getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        UserWallet that = (UserWallet) o;
        return getId() != null && Objects.equals(getId(), that.getId());
    }
}
