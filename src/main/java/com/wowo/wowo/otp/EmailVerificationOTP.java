package com.wowo.wowo.otp;

import com.wowo.wowo.constant.Constant.OTPType;
import lombok.Getter;
import lombok.Setter;

/**
 * OTP dùng cho xác minh email
 * Có thời gian hết hạn dài nhất (60 phút), vì người dùng có thể không kiểm tra
 * email ngay lập tức
 */
@Getter
@Setter
public class EmailVerificationOTP extends OTP {

    public EmailVerificationOTP(String userId, String recipient) {
        this.userId = userId;
        this.recipient = recipient;
        this.otpType = OTPType.EMAIL_VERIFICATION;
        setExpirationTime();
    }
}