package com.wowo.wowo.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.math.BigDecimal;
import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "transaction")
public class Transaction {

    @Id
    @Size(max = 15)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "transaction_id_seq")
    @SequenceGenerator(name = "transaction_id_seq", sequenceName = "transaction_id_seq",
                       allocationSize = 1)
    @Column(name = "id", nullable = false, length = 15)
    private String id;

    @NotNull
    @Column(name = "money", nullable = false, precision = 10, scale = 2)
    private BigDecimal money;

    @Size(max = 50)
    @NotNull
    @Column(name = "status", nullable = false, length = 50)
    private PaymentStatus status = PaymentStatus.PENDING;

    @Column(name = "type", nullable = false)
    @NotNull
    private TransactionType type = TransactionType.TRANSFER;

    @Column(name = "variant", nullable = false)
    private TransactionVariant variant = TransactionVariant.WALLET;

    @Column(name = "description", length = 300)
    private String Description;

    @NotNull
    @ColumnDefault("now()")
    @Column(name = "created", nullable = false)
    private Instant created;

    @NotNull
    @ColumnDefault("now()")
    @Column(name = "updated", nullable = false)
    private Instant updated;

}