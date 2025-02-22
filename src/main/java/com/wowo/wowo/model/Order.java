package com.wowo.wowo.model;

import com.wowo.wowo.exception.BadRequest;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "\"order\"")
public class Order {

    @Id
    @Column(name = "id", nullable = false, length = 50)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "order_id_seq")
    @SequenceGenerator(name = "order_id_seq", sequenceName = "order_id_seq", allocationSize = 1)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private Application application;

    @NotNull
    @Column(name = "money", nullable = false)
    private Long money;

    private Long discountMoney;

    @NotNull
    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private PaymentStatus status = PaymentStatus.PENDING;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    private Transaction transaction;

    @Size(max = 300)
    @Column(name = "return_url", length = 300)
    private String returnUrl;

    @Size(max = 300)
    @Column(name = "success_url", length = 300)
    private String successUrl;

    @CreationTimestamp
    @Column(name = "created", nullable = false)
    private Instant created;

    @UpdateTimestamp
    @Column(name = "updated", nullable = false)
    private Instant updated;

    @Size(max = 100)
    @Column(name = "service_name", length = 100)
    private String serviceName;

    public void useVoucher(Voucher voucher) {
        if (status != PaymentStatus.PENDING) {
            throw new BadRequest("Đơn hàng đã được thanh toán");
        }
        this.discountMoney = voucher.getPrice();
        if (discountMoney < 0) {
            discountMoney = 0L;
        }
        voucher.setOrderId(this.id);
    }

    public void cancel() {
        if (this.status == PaymentStatus.PENDING) {
            this.status = PaymentStatus.CANCELLED;
        }
    }
}