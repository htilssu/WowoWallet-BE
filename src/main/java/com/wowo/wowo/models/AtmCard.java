package com.wowo.wowo.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "atm_card")
public class AtmCard {

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull
    @Column(name = "bank_id", nullable = false)
    private Integer atmId;

    @Size(max = 16)
    @NotNull
    @Column(name = "card_number", nullable = false, length = 16, unique = true)
    private String cardNumber;

    @Size(max = 3)
    @Column(name = "ccv", length = 3)
    private String ccv;

    @Size(max = 255)
    @NotNull
    @Column(name = "holder_name", nullable = false)
    private String holderName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id")
    private User owner;

    @Size(max = 255)
    @NotNull
    @Column(name = "expired", nullable = false)
    private String expired;

    @JoinColumn(name = "bank_id", nullable = false, insertable = false, updatable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Bank atm;

    @Column(name = "created", nullable = false)
    @CreationTimestamp
    private Instant created;
}