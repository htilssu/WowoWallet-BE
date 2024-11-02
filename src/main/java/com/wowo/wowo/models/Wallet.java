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
@Table(name = "wallet")
public class Wallet extends BalanceEntity {

    @Id
    @Column(name = "id", nullable = false)
    @SequenceGenerator(name = "wallet_id_seq", sequenceName = "wallet_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "wallet_id_seq")
    private Long id;

    @NotNull
    @Column(name = "owner_type", nullable = false, length = 30)
    @Enumerated(EnumType.STRING)
    private WalletOwnerType ownerType = WalletOwnerType.USER;

    @Size(max = 5)
    @NotNull
    @ColumnDefault("'VND'")
    @Column(name = "currency", nullable = false, length = 5)
    private String currency = "VND";

    @Size(max = 32)
    @Column(name = "owner_id", length = 32)
    private String ownerId;
}
