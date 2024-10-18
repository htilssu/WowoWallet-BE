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
@Table(name = "\"order\"")
public class Order {

    @Id
    @Size(max = 50)
    @Column(name = "id", nullable = false, length = 50)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "partner_id")
    private Partner partner;

    @NotNull
    @Column(name = "money", nullable = false)
    private Long money;

    @Size(max = 50)
    @NotNull
    @Column(name = "status", nullable = false)
    private PaymentStatus status = PaymentStatus.PENDING;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "transaction_id")
    private Transaction transaction;

    @Size(max = 300)
    @Column(name = "return_url", length = 300)
    private String returnUrl;

    @Size(max = 300)
    @Column(name = "success_url", length = 300)
    private String successUrl;

    @NotNull
    @ColumnDefault("now()")
    @Column(name = "created", nullable = false)
    private Instant created;

    @NotNull
    @ColumnDefault("now()")
    @Column(name = "updated", nullable = false)
    private Instant updated;

    @Size(max = 100)
    @Column(name = "service_name", length = 100)
    private String serviceName;
}