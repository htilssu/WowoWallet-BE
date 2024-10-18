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
public class Wallet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Size(max = 20)
    @NotNull
    @ColumnDefault("'user'")
    @Column(name = "owner_type", nullable = false, length = 20)
    private String ownerType;

    @Size(max = 3)
    @NotNull
    @ColumnDefault("'VND'")
    @Column(name = "currency", nullable = false, length = 3)
    private String currency; // Loại tiền tệ

    @Size(max = 10)
    @Column(name = "owner_id", length = 10)
    private String ownerId; // ID của chủ sở hữu

    @NotNull
    @ColumnDefault("0")
    @Column(name = "balance", nullable = false)
    private Double balance; // Số dư ví

    public void sendMoney(Wallet receiveWallet, double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Amount must be greater than 0.");
        }
        if (this.balance < amount) {
            throw new IllegalArgumentException("Insufficient balance.");
        }

        // Chuyển tiền
        receiveWallet.balance += amount;
        this.balance -= amount;
    }
}
