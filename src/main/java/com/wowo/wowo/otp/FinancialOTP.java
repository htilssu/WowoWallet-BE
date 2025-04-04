package com.wowo.wowo.otp;

import com.wowo.wowo.constant.Constant.OTPType;

/**
 * OTP dùng cho các thao tác tài chính, mở rộng từ OTP cơ bản
 */
public class FinancialOTP extends OTP {

    /**
     * Constructor mặc định với FINANCIAL_OPERATION
     */
    public FinancialOTP(String userId, String recipient) {
        this.userId = userId;
        this.recipient = recipient;
        this.otpType = OTPType.FINANCIAL_OPERATION;
        setExpirationTime();
    }

    /**
     * Constructor cho phép chỉ định loại OTP tài chính cụ thể
     */
    public FinancialOTP(String userId, String recipient, OTPType otpType) {
        this.userId = userId;
        this.recipient = recipient;

        // Chỉ chấp nhận loại OTP tài chính
        if (otpType == OTPType.FINANCIAL_OPERATION ||
                otpType == OTPType.TRANSACTION_CONFIRMATION ||
                otpType == OTPType.WITHDRAW_CONFIRMATION) {
            this.otpType = otpType;
        } else {
            throw new IllegalArgumentException("Chỉ các loại OTP tài chính được chấp nhận cho FinancialOTP");
        }

        setExpirationTime();
    }
}