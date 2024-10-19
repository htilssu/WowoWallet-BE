package com.wowo.wowo.util;

import lombok.Getter;
public class OTPUtil {

    /**
     * Thời gian hết hạn của mã OTP
     * Giá trị là số giây OTP hết hạn
     */
    @Getter
    static long expiryTime = 60 * 5; // 5 minutes
}
