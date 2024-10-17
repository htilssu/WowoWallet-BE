package com.wowo.wowo.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

@Getter
@Setter
@Entity
@Builder
@Table(name = "\"user\"")
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @Size(max = 10)
    @Column(name = "id", nullable = false)
    private String id;

    @Column(name = "username", unique = true)
    @Size(max = 255)
    private String username;

    @Column(name = "email", unique = true)
    @Size(max = 255)
    private String email;

    @NotNull
    @ColumnDefault("true")
    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    @NotNull
    @ColumnDefault("false")
    @Column(name = "is_verified", nullable = false)
    private Boolean isVerified = false;

    @ColumnDefault("0")
    @Column(name = "total_money", nullable = false)
    private Long totalMoney;

    @Size(max = 255)
    @Column(name = "job")
    private String job;
}