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

@Entity(name = "application_api_key")
public class ApplicationApiKey {

    @Id
    @SequenceGenerator(name = "application_api_key_id_seq",
                       sequenceName = "application_api_key_id_seq",
                       allocationSize = 1)
    @GeneratedValue(generator = "application_api_key_id_seq",
                    strategy = jakarta.persistence.GenerationType.SEQUENCE)
    public Long id;

    public String key;

    public String secret;

    @JoinColumn(name = "application_id")
    @ManyToOne(fetch = FetchType.LAZY, optional = false, targetEntity = Application.class)
    public Application application;
}
