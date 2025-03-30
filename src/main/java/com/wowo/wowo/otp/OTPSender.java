package com.wowo.wowo.otp;

public interface OTPSender {

    /**
     * Gửi mã OTP cho người dùng
     *
     * @param recipientAddress người nhận mã OTP
     * @param otp    mã OTP
     */
    void sendOTP(String recipientAddress, String otp);
}
