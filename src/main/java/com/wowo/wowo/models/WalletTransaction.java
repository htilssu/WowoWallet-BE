package com.wowo.wowo.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@Entity
@Builder
@Table(name = "wallet_transaction",
       indexes = {
               @Index(name = "wallet_transaction_sender_wallet_id_index",
                      columnList = "sender_wallet"),
               @Index(name = "wallet_transaction_receiver_wallet_id_index",
                      columnList = "receiver_wallet")
       })
@NoArgsConstructor
@AllArgsConstructor
public class WalletTransaction {

    @Id
    @Column(name = "id", nullable = false, length = 40)
    private String id;

    @MapsId
    @OneToOne(fetch = FetchType.LAZY, optional = false, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "id", nullable = false)
    private Transaction transaction;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "sender_wallet", nullable = false)
    private Wallet senderWallet;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "receiver_wallet", nullable = false)
    private Wallet receiverWallet;
}