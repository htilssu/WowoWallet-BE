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
 *  * Created: 27-11-2024
 *  ******************************************************
 */

package com.wowo.wowo.data.dto;

import lombok.Data;

@Data
public class ApplicationTransferDTO {

    @Override
    public String toString() {
        return "ApplicationTransferDTO{" +
                "walletId=" + walletId +
                ", amount=" + amount +
                '}';
    }

    private Long walletId;
    private Long amount;
}
