package com.wowo.wowo.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;

import java.math.BigDecimal;
import java.time.Instant;

@Getter
@Setter
@Builder
@DynamicInsert
@Entity
@Table(name = "order", uniqueConstraints = {
        @UniqueConstraint(name = "order_id_partner_id_key",
                          columnNames = {"id", "partner_id"}),
        @UniqueConstraint(name = "order_transaction_id_key",
                          columnNames = {"transaction_id"}),
        @UniqueConstraint(name = "order_external_transaction_id_key",
                          columnNames = {"external_transaction_id"})
})
@NoArgsConstructor
@AllArgsConstructor
public class Order {

    @Id
    @Size(max = 15)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, length = 15)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "partner_id")
    private Partner partner;

    @NotNull
    @Column(name = "money", nullable = false, precision = 10, scale = 2)
    private BigDecimal money;

    @Size(max = 50)
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

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "created", nullable = false)
    private Instant created;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "updated", nullable = false)
    private Instant updated;

    @Size(max = 100)
    @Column(name = "service_name", length = 100)
    private String serviceName;
    
}