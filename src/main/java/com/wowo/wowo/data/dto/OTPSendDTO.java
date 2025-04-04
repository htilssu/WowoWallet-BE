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
import com.wowo.wowo.otp.OTPFactory.OTPChannel;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
public class OTPSendDTO extends OTPData {

    @NotNull
    private String recipient;

    /**
     * Phương thức gửi OTP (EMAIL hoặc SMS)
     * Mặc định là EMAIL nếu không được chỉ định
     */
    private String sendMethod = "EMAIL";

    /**
     * Constructor mặc định
     */
    public OTPSendDTO() {
        // Constructor mặc định
    }

    /**
     * Constructor đầy đủ
     * 
     * @param recipient  địa chỉ người nhận
     * @param otpType    loại OTP
     * @param sendMethod phương thức gửi
     */
    public OTPSendDTO(String recipient, OTPType otpType, String sendMethod) {
        this.recipient = recipient;
        this.setOtpType(otpType);
        this.sendMethod = sendMethod;
    }

    /**
     * Chuyển đổi sendMethod thành OTPChannel
     * 
     * @return OTPChannel tương ứng (EMAIL hoặc SMS)
     */
    public OTPChannel getSendChannel() {
        try {
            return OTPChannel.valueOf(sendMethod.toUpperCase());
        } catch (IllegalArgumentException e) {
            return OTPChannel.EMAIL;
        }
    }
}
