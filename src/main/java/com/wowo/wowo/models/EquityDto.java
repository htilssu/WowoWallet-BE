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

package com.wowo.wowo.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Value;

import java.io.Serializable;
import java.util.List;

/**
 * DTO for {@link Equity}
 */
@Value
@JsonIgnoreProperties(ignoreUnknown = true)
public class EquityDto implements Serializable {

    String user;
    Integer month;
    Integer year;
    List<EquityItemDto> equityItemList;


    /**
     * DTO for {@link Equity.EquityItem}
     */
    @Value
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class EquityItemDto implements Serializable {

        String date;
        double in;
        double out;
    }
}