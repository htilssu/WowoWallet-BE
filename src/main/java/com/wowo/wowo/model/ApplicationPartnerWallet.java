/*
 * ******************************************************
 *  * Copyright (c) 2024 htilssu
 *  *
 *  * This code is the property of htilssu. All rights reserved.
 *  * Redistribution or reproduction of any part of this code
 *  * in any form, with or without modification, is strictly
 *  * prohibited without prior written permission from the author.
 *  *
 *  * Author: htilssu
 *  * Created: 24-11-2024
 *  ******************************************************
 */

package com.wowo.wowo.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.proxy.HibernateProxy;

import java.util.Objects;

@Entity
@Getter
@Setter
@DiscriminatorValue("APPLICATION_PARTNER")
public class ApplicationPartnerWallet extends Wallet {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private Application application;

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
        ApplicationPartnerWallet that = (ApplicationPartnerWallet) o;
        return getId() != null && Objects.equals(getId(), that.getId());
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" +
                "id = " + getId() + ", " +
                "balance = " + getBalance() + ", " +
                "currency = " + getCurrency() + ")";
    }
}
