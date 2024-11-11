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

public final class VoucherMessage {

    private final String voucherId;
    private final PaymentStatus status;

    public VoucherMessage(String voucherId, PaymentStatus status) {
        this.voucherId = voucherId;
        this.status = status;
    }

    public String voucherId() {return voucherId;}

    public PaymentStatus status() {return status;}

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (VoucherMessage) obj;
        return Objects.equals(this.voucherId, that.voucherId) &&
                Objects.equals(this.status, that.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(voucherId, status);
    }

    @Override
    public String toString() {
        return "VoucherMessage[" +
                "voucherId=" + voucherId + ", " +
                "status=" + status + ']';
    }

}
