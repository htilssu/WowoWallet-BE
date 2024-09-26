package com.wowo.wowo.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;

@Getter
@Setter
@DynamicInsert
@Entity
@Table(name = "bank")
public class Bank {
    @Id
    @Column(name = "id", nullable = false)
    private Long id;

    @Size(max = 255)
    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @Size(max = 255)
    @NotNull
    @Column(name = "code", nullable = false)
    private String code;

    @Size(max = 255)
    @NotNull
    @Column(name = "bin", nullable = false)
    private String bin;

    @Size(max = 255)
    @NotNull
    @Column(name = "shortName", nullable = false)
    private String shortName;

    @Size(max = 255)
    @NotNull
    @Column(name = "logo", nullable = false)
    private String logo;

    @NotNull
    @Column(name = "transferSupported", nullable = false)
    private Long transferSupported; //

    @NotNull
    @Column(name = "lookupSupported", nullable = false) //
    private Long lookupSupported; //

    @Size(max = 255)
    @NotNull
    @Column(name = "short_name", nullable = false)
    private String short_name;

    @NotNull
    @Column(name = "support", nullable = false)
    private Long support;

    @NotNull
    @Column(name = "isTransfer", nullable = false)
    private Long isTransfer;

    @Size(max = 255)
    @Column(name = "swift_code") //
    private String swift_code;
}
