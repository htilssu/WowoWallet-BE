package com.wowo.wowo.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "verify")
public class Verify {

    @Id
    @SequenceGenerator(name = "verify_id_seq", sequenceName = "verify_id_seq", allocationSize = 50)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "verify_id_seq")
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "customer_id", nullable = true)  
    private User customer;

    @Size(max = 255)
    @NotNull
    @Column(name = "type", nullable = false)
    private String type;

    @NotNull
    @Column(name = "number_card", nullable = false)
    private Long numberCard;

    @NotNull
    @Column(name = "open_day", nullable = false)
    private java.time.LocalDateTime openDay;

    @NotNull
    @Column(name = "close_day", nullable = false)
    private java.time.LocalDateTime closeDay;

    @NotNull
    @Size(max = 255)
    @Column(name = "font_image", nullable = false)
    private String fontImage;

    @NotNull
    @Size(max = 255)
    @Column(name = "behind_image", nullable = false)
    private String behindImage;

    @NotNull
    @Size(max = 255)
    @Column(name = "user_image", nullable = false)
    private String userImage;

}
