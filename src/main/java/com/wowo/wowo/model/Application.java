/*
 * ******************************************************
 *  * Copyright (c) 2024 htilssu
 *  *
 *  * This code is the property of htilssu. All rights reserved.
 *  * Redistribution or reproduction of any part of this code
 *  * in any form, with or without modification, is strictly
 *  * prohibited without prior written permission from the author.
 *  *
 *  * Author: htilssu
 *  * Created: 23-11-2024
 *  ******************************************************
 */

package com.wowo.wowo.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@Entity(name = "application")
public class Application {

    @Id
    @SequenceGenerator(name = "application_id_seq", sequenceName = "application_id_seq",
                       allocationSize = 1)
    @GeneratedValue(generator = "application_id_seq", strategy = GenerationType.SEQUENCE)
    public Long id;

    public String name;

    @OneToMany(mappedBy = "application")
    public List<ApplicationApiKey> apiKey;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner;
}
