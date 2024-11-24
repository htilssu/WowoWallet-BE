package com.wowo.wowo.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "banks")
public class Bank {

    @Id
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "name", length = Integer.MAX_VALUE)
    private String name;

    @Column(name = "code", length = Integer.MAX_VALUE)
    private String code;

    @Column(name = "bin", length = Integer.MAX_VALUE)
    private String bin;

    @Column(name = "\"shortName\"", length = Integer.MAX_VALUE)
    private String shortName;

    @Column(name = "logo", length = Integer.MAX_VALUE)
    private String logo;

    @Column(name = "\"transferSupported\"")
    private Long transferSupported;

    @Column(name = "\"lookupSupported\"")
    private Long lookupSupported;

    @Column(name = "support")
    private Long support;

    @Column(name = "\"isTransfer\"")
    private Long isTransfer;

    @Column(name = "swift_code", length = Integer.MAX_VALUE)
    private String swiftCode;

}