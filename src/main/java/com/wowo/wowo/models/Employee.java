package com.wowo.wowo.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Table(name = "employee", uniqueConstraints = {
        @UniqueConstraint(name = "employee_ssn_key", columnNames = {"ssn"})
})
public class Employee {

    @Id
    @Size(max = 10)
    @Column(name = "id", nullable = false, length = 10)
    private String id;

    @MapsId
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id", nullable = false)
    private User user;

    @NotNull
    @Column(name = "salary", nullable = false, precision = 10, scale = 2)
    private BigDecimal salary;

    @Size(max = 15)
    @NotNull
    @Column(name = "ssn", nullable = false, length = 15)
    private String ssn;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id")
    private Role role;

}