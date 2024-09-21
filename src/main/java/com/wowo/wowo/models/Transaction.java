package com.wowo.wowo.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;

import java.math.BigDecimal;
import java.time.Instant;

@Getter
@Setter
@DynamicInsert
@Entity
public class Transaction {

    @Id
    @Size(max = 15)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, length = 15)
    private String id;

    @Column(name = "money", nullable = false, precision = 10, scale = 2)
    private BigDecimal money;

    @Size(max = 3)
    @ColumnDefault("'VND'")
    @Column(name = "currency", nullable = false, length = 3)
    private String currency;

    @Size(max = 20)
    @ColumnDefault("'transfer'")
    @Column(name = "transaction_type", nullable = false, length = 20)
    private String transactionType;

    @Size(max = 20)
    @ColumnDefault("'wallet'")
    @Column(name = "transaction_target", nullable = false, length = 20)
    private String transactionTarget;

    @Size(max = 50)
    @ColumnDefault("'PENDING'")
    @Column(name = "status", nullable = false, length = 50)
    private String status;

    @ColumnDefault("CURRENT_TIMESTAMP(3)")
    @Column(name = "created", nullable = false)
    private Instant created;

    @ColumnDefault("CURRENT_TIMESTAMP(3)")
    @Column(name = "updated", nullable = false)
    private Instant updated;

    @Size(max = 10)
    @NotNull
    @Column(name = "sender_id", nullable = false, length = 10)
    private String senderId;

    @Size(max = 10)
    @NotNull
    @Column(name = "receiver_id", nullable = false, length = 10)
    private String receiverId;

    @Size(max = 20)
    @ColumnDefault("'user'")
    @Column(name = "sender_type", nullable = false, length = 20)
    private String senderType;

    @Size(max = 20)
    @ColumnDefault("'user'")
    @Column(name = "receiver_type", nullable = false, length = 20)
    private String receiverType;

}