package com.wowo.wowo.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "partner")
public class Partner {

    @Id
    @Size(max = 32)
    @Column(name = "id", nullable = false, length = 32)
    private String id;

    @Column(name = "description", length = Integer.MAX_VALUE)
    private String description;

    private String name;

    @Email
    @Column(name = "email", nullable = false)
    @NotNull
    private String email;

    @Size(max = 255)
    @Column(name = "api_key", nullable = false)
    private String apiKey;
}