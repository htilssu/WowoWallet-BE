package com.wowo.wowo.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;

import java.time.LocalDate;

@Getter
@Setter
@DynamicInsert
@Entity
@Table(name = "partner")
public class Partner {

    @Id
    @Size(max = 10)
    @Column(name = "id", length = 10)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    @Size(max = 255)
    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description", length = Integer.MAX_VALUE)
    private String description;

    @Size(max = 255)
    @NotNull
    @Column(name = "email", nullable = false)
    private String email;

    @NotEmpty(message = "Không được để trống")
    @NotBlank(message = "Không được để trống")
    @Column(name = "partner_type", nullable = false, length = 100)
    private String partnerType;

    @Size(max = 255)
    @Column(name = "avatar")
    private String avatar;

    @Size(max = 255)
    @NotNull
    @Column(name = "password", nullable = false)
    private String password;

    @Size(max = 255)
    @NotNull
    @Column(name = "api_base_url", nullable = false)
    private String apiBaseUrl;

    @Size(max = 255)
    @NotNull
    @Column(name = "api_key", nullable = false)
    private String apiKey;

    @NotNull
    @Column(name = "balance", nullable = false)
    private double balance;

    @ColumnDefault("CURRENT_DATE")
    @Column(name = "created")
    private LocalDate created;

}