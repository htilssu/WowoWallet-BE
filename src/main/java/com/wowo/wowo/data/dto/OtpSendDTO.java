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

package com.wowo.wowo.data.dto;

import java.util.Objects;

import com.wowo.wowo.constant.Constant;

import com.wowo.wowo.otp.OTPData;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class OtpSendDTO extends OTPData {

    @NotNull
    private Constant.OTPService type;
    private String sendTo;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        OtpSendDTO that = (OtpSendDTO) o;
        return getType() == that.getType() && Objects.equals(getSendTo(), that.getSendTo());
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + Objects.hashCode(getType());
        result = 31 * result + Objects.hashCode(getSendTo());
        return result;
    }
}
