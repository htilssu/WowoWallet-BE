package com.wowo.wowo.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;

@Getter
@Setter
@DynamicInsert
@Entity
@Table(name = "atm")
public class Atm {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "atm_id_gen")
    @SequenceGenerator(name = "atm_id_gen", sequenceName = "atm_id_seq", allocationSize = 1)
    @Column(name = "id", nullable = false)
    private int id;

    @Size(max = 255)
    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @Size(max = 255)
    @Column(name = "icon")
    private String icon;
}