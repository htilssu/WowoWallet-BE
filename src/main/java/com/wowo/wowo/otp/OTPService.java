package com.wowo.wowo.otp;

import com.wowo.wowo.data.dto.OTPSendDTO;
import com.wowo.wowo.data.dto.OTPVerifyDTO;

public abstract class OTPService {

    /**
     * Gửi mã OTP cho người dùng
     *
     * @param context địa chỉ người nhận mã OTP
     * @param otp     mã OTP
     */
    public abstract void sendOTP(OTPSendDTO context, String otp);

    /**
     * Xác thực mã OTP
     *
     * @param context địa chỉ người nhận mã OTP
     * @param otp     mã OTP
     *
     * @return true nếu mã OTP hợp lệ, false nếu không hợp lệ
     */
    public abstract boolean verifyOTP(OTPVerifyDTO context, String otp);
}
