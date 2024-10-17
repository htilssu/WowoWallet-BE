package com.wowo.wowo.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.transaction.annotation.Transactional;

@Getter
@Setter
@Entity
@Table(name = "wallet")
public class Wallet {

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Size(max = 20)
    @NotNull
    @ColumnDefault("'user'")
    @Column(name = "owner_type", nullable = false, length = 20)
    private String ownerType;

    @Size(max = 3)
    @NotNull
    @ColumnDefault("'VND'")
    @Column(name = "currency", nullable = false, length = 3)
    private String currency;

    @Size(max = 10)
    @Column(name = "owner_id", length = 10)
    private String ownerId;

    @NotNull
    @ColumnDefault("0")
    @Column(name = "balance", nullable = false)
    private Double balance;

    @Transactional
    public void sendMoney(Wallet receiveWallet, double amount) {
        receiveWallet.balance += amount;
        this.balance -= amount;
    }
}