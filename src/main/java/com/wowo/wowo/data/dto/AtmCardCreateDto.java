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
 *  * Created: 31-10-2024
 *  ******************************************************
 */

package com.wowo.wowo.data.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.wowo.wowo.models.AtmCard;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * DTO for {@link AtmCard}
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class AtmCardCreateDto {

    @NotNull
    @Size(max = 16)
    private String cardNumber;
    private String atmId;
    @Size(max = 3)
    private String ccv;
    @NotNull
    @Size(max = 60)
    private String holderName;
    @NotNull
    private Integer month;
    @NotNull
    private Integer year;
    private Long bankId;
}
