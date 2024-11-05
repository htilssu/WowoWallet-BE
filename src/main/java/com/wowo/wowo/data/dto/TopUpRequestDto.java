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
 *  * Created: 30-10-2024
 *  ******************************************************
 */

package com.wowo.wowo.data.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TopUpRequestDto {

    @NotNull(message = "source is required")
    private String to;
    @NotNull(message = "Số tiền không được để trống")
    @PositiveOrZero(message = "Số tiền phải lớn hơn hoặc bằng 0")
    private Long amount;
    private TopUpSource method;


    public enum TopUpSource {
        PAYPAL, STRIPE, ATM_CARD
    }
}
