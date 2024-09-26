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
    @Size(max = 15)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "order_id_seq")
    @Column(name = "id", nullable = false, length = 15)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "partner_id")
    private Partner partner;

    @NotNull
    @Column(name = "money", nullable = false, precision = 10, scale = 2)
    private BigDecimal money;

    @Size(max = 50)
    @NotNull
    @ColumnDefault("'PENDING'")
    @Column(name = "status", nullable = false, length = 50)
    private String status;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "transaction_id")
    private Transaction transaction;

    @Size(max = 50)
    @Column(name = "voucher_id", length = 50)
    private String voucherId;

    @Size(max = 100)
    @Column(name = "voucher_name", length = 100)
    private String voucherName;

    @Size(max = 100)
    @Column(name = "voucher_code", length = 100)
    private String voucherCode;

    @Size(max = 50)
    @Column(name = "order_id", length = 50)
    private String orderId;

    @Size(max = 300)
    @Column(name = "return_url", length = 300)
    private String returnUrl;

    @Size(max = 300)
    @Column(name = "success_url", length = 300)
    private String successUrl;

    @Column(name = "voucher_discount", precision = 10, scale = 2)
    private BigDecimal voucherDiscount;

    @Size(max = 50)
    @Column(name = "external_transaction_id", length = 50)
    private String externalTransactionId;

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