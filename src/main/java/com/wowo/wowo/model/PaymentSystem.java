package com.wowo.wowo.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

@Getter
@Setter
@Entity
@Table(name = "payment_system")
public class PaymentSystem {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "payment_system_id_gen")
    @SequenceGenerator(name = "payment_system_id_gen", sequenceName = "payment_system_id_seq",
                       allocationSize = 1)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Size(max = 50)
    @NotNull
    @Column(name = "name", nullable = false, length = 50)
    private String name;

    @Size(max = 255)
    @Column(name = "api_key")
    private String apiKey;

    @Size(max = 255)
    @Column(name = "api_secret")
    private String apiSecret;
    @ColumnDefault("true")
    @Column(name = "is_active")
    private Boolean isActive;
}