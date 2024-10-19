package com.wowo.wowo.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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
    @Column(name = "id", nullable = false, length = 40)
    private String id;

    @NotNull
    @Column(name = "amount", nullable = false)
    private Long amount;

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