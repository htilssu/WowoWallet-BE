package com.wowo.wowo.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "atm_card")
public class AtmCard {

    @Id
    @Column(name = "id", nullable = false)
    private Integer id;

    @NotNull
    @Column(name = "atm_id", nullable = false)
    private Integer atmId;

    @Size(max = 16)
    @NotNull
    @Column(name = "card_number", nullable = false, length = 16)
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

    @NotNull
    @ColumnDefault("CURRENT_DATE")
    @Column(name = "created", nullable = false)
    private LocalDate created;

}