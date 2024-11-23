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
 *  * Created: 9-11-2024
 *  ******************************************************
 */

package com.wowo.wowo.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Value;

import java.io.Serializable;
import java.util.List;

/**
 * DTO for {@link Equity}
 */
@Value
@JsonIgnoreProperties(ignoreUnknown = true)
public class EquityDTO implements Serializable {

    String user;
    Integer month;
    Integer year;
    List<EquityItemDTO> equityItemList;


    /**
     * DTO for {@link Equity.EquityItem}
     */
    @Value
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class EquityItemDTO implements Serializable {

        String date;
        double in;
        double out;
    }
}