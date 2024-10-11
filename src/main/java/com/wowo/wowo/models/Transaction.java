package com.wowo.wowo.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.math.BigDecimal;
import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "transaction")
public class Transaction {

    @Id
    @Size(max = 40)
    @Column(name = "id", nullable = false, length = 40)
    private String id;

    @NotNull
    @Column(name = "money", nullable = false)
    private Long money;

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
    private String description;

    @NotNull
    @CreatedDate
    @Column(name = "created", nullable = false)
    private Instant created;

    @NotNull
    @Column(name = "updated", nullable = false)
    @LastModifiedDate
    private Instant updated;

}