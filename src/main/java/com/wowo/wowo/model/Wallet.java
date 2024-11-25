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

import com.wowo.wowo.exception.InsufficientBalanceException;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.proxy.HibernateProxy;

import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@RequiredArgsConstructor
@Inheritance(strategy = InheritanceType.JOINED)
@Entity
@DiscriminatorColumn(name = "type", discriminatorType = DiscriminatorType.STRING)
public class Wallet {

    @Id
    @Column(name = "id", nullable = false)
    @SequenceGenerator(name = "wallet_id_seq", sequenceName = "wallet_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "wallet_id_seq")
    private Long id;

    @Min(value = 0, message = "Số dư không được nhỏ hơn 0")
    private Long balance = 0L;

    @Size(max = 5)
    @NotNull
    @ColumnDefault("'VND'")
    @Column(name = "currency", nullable = false, length = 5)
    private String currency = "VND";
    @Version
    private Long version;

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
        Wallet wallet = (Wallet) o;
        return getId() != null && Objects.equals(getId(), wallet.getId());
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" +
                "id = " + id + ", " +
                "balance = " + balance + ", " +
                "currency = " + currency + ", " +
                "version = " + version + ")";
    }

    public boolean hasEnoughBalance(Long amount) {
        return balance >= amount;
    }

    public void addBalance(Long amount) {
        if (amount < 0 && Math.abs(amount) > balance) {
            throw new InsufficientBalanceException("Số dư không đủ");
        }
        balance += amount;
    }
}
