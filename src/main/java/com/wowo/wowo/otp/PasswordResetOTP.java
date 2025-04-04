package com.wowo.wowo.otp;

import com.wowo.wowo.constant.Constant.OTPType;
import lombok.Getter;
import lombok.Setter;

/**
 * OTP dùng cho quên mật khẩu
 * Có thời gian hết hạn dài hơn (30 phút)
 */
@Getter
@Setter
public class PasswordResetOTP extends OTP {

    private String token; // Token riêng cho đặt lại mật khẩu

    public PasswordResetOTP(String userId, String recipient) {
        this.userId = userId;
        this.recipient = recipient;
        this.otpType = OTPType.PASSWORD_RESET;
        setExpirationTime();
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    /**
     * Override phương thức xác minh để case-insensitive (không phân biệt hoa
     * thường)
     * giúp người dùng dễ dàng nhập mã hơn
     * 
     * @param inputCode    mã OTP được nhập vào
     * @param otpGenerator generator OTP để kiểm tra tính hợp lệ
     * @return true nếu mã đúng và chưa hết hạn
     */
    @Override
    public boolean verify(String inputCode, OTPGenerator otpGenerator) {
        if (isExpired()) {
            return false;
        }
        return otpGenerator.verify(inputCode.toLowerCase());
    }
}