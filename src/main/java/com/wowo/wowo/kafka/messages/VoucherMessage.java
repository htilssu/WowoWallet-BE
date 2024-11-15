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
 *  * Created: 10-11-2024
 *  ******************************************************
 */

package com.wowo.wowo.kafka.messages;

import com.wowo.wowo.models.PaymentStatus;

import java.util.Objects;

public record VoucherMessage(String voucherId, PaymentStatus status) {

    @Override
    public String toString() {
        return "VoucherMessage[" +
                "voucherId=" + voucherId + ", " +
                "status=" + status + ']';
    }

}
