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

import com.wowo.wowo.constant.Constant.OTPType;
import com.wowo.wowo.otp.OTPData;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Data
public class OTPVerifyDTO extends OTPData {

    /**
     * Constructor với OTPType và mã OTP
     * 
     * @param otpType Loại OTP
     * @param otpCode Mã OTP
     */
    public OTPVerifyDTO(OTPType otpType, String otpCode) {
        this.setOtpType(otpType);
        this.setOtpCode(otpCode);
    }
}
