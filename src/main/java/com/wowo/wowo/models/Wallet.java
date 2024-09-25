package com.wowo.wowo.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

@Getter
@Setter
@Entity
@Table(name = "wallet", uniqueConstraints = {
        @UniqueConstraint(name = "uk_wallet_owner", columnNames = {"owner_id", "owner_type"})
})
public class Wallet {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "wallet_id_gen")
    @SequenceGenerator(name = "wallet_id_gen", sequenceName = "wallet_id_seq", allocationSize = 1)
    @Column(name = "id", nullable = false)
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
    private double balance;

    public void sendMoneyTo(Wallet receiverWallet, double money) {
        if (balance >= money) {
            balance -= money;
            receiverWallet.balance += money;
        }
    }
}