package com.wowo.wowo.models;

import com.wowo.wowo.annotations.id_generator.TransactionIdSequence;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "transaction")
@AllArgsConstructor
@NoArgsConstructor
public class Transaction {

    @Id
    @TransactionIdSequence
    @Column(name = "id", nullable = false, length = 40)
    private String id;

    @NotNull
    @Column(name = "amount", nullable = false)
    private Long amount;

    @NotNull
    @Column(name = "status", nullable = false)
    private PaymentStatus status = PaymentStatus.PENDING;

    @Column(name = "type", nullable = false)
    @NotNull
    private TransactionType type = TransactionType.TRANSFER;

    @Column(name = "variant", nullable = false)
    private TransactionVariant variant = TransactionVariant.WALLET;

    @Column(name = "description", length = 300)
    private String description;

    @CreatedDate
    @Column(name = "created", nullable = false)
    private Instant created = Instant.now();

    @Column(name = "updated", nullable = false)
    @LastModifiedDate
    private Instant updated = Instant.now();

    @OneToOne(mappedBy = "transaction")
    private WalletTransaction walletTransaction;

    @OneToOne(mappedBy = "transaction")
    private GroupFundTransaction groupFundTransaction;
}