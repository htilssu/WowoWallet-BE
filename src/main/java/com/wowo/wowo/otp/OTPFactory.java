package com.wowo.wowo.otp;

import com.wowo.wowo.constant.Constant.OTPType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Factory class để tạo các đối tượng OTP
 */
@Component
public class OTPFactory {

    @Autowired
    private OTPSender emailOTPSender;

    /**
     * Tạo đối tượng OTP thích hợp dựa trên loại OTP
     * 
     * @param otpType       loại OTP
     * @param userId        ID người dùng
     * @param recipient     địa chỉ người nhận
     * @param transactionId ID giao dịch (cần thiết cho OTP liên quan đến giao dịch)
     * @return đối tượng OTP tương ứng
     */
    public OTP createOTP(OTPType otpType, String userId, String recipient, String transactionId) {
        OTP otp;

        switch (otpType) {
            case PASSWORD_RESET:
                otp = new PasswordResetOTP(userId, recipient);
                break;
            case EMAIL_VERIFICATION:
                otp = new EmailVerificationOTP(userId, recipient);
                break;
            case ACCOUNT_VERIFICATION:
                otp = new EmailVerificationOTP(userId, recipient);
                otp.setOtpType(OTPType.ACCOUNT_VERIFICATION);
                break;
            case TRANSACTION_CONFIRMATION:
            case WITHDRAW_CONFIRMATION:
                if (transactionId == null) {
                    throw new IllegalArgumentException("Transaction ID is required for transaction-related OTPs");
                }

                // Tạo TransactionOTP
                otp = new TransactionOTP(userId, recipient, transactionId);
                otp.setOtpType(otpType);
                break;
            default:
                throw new IllegalArgumentException("Unsupported OTP type: " + otpType);
        }

        // Inject OTPSender vào đối tượng OTP
        otp.setOTPSender(emailOTPSender);
        otp.setExpirationTime();
        return otp;
    }

    /**
     * Tạo đối tượng OTP thích hợp dựa trên loại OTP (phương thức overload cho OTP
     * không liên quan đến giao dịch)
     * 
     * @param otpType   loại OTP
     * @param userId    ID người dùng
     * @param recipient địa chỉ người nhận
     * @return đối tượng OTP tương ứng
     */
    public OTP createOTP(OTPType otpType, String userId, String recipient) {
        boolean isTransactionRelated = otpType == OTPType.TRANSACTION_CONFIRMATION ||
                otpType == OTPType.WITHDRAW_CONFIRMATION;

        if (isTransactionRelated) {
            throw new IllegalArgumentException("Transaction ID is required for transaction-related OTPs");
        }

        return createOTP(otpType, userId, recipient, null);
    }

    /**
     * Tạo đối tượng OTP từ chuỗi otpType
     * 
     * @param otpTypeStr    chuỗi biểu diễn loại OTP
     * @param userId        ID người dùng
     * @param recipient     địa chỉ người nhận
     * @param transactionId ID giao dịch (có thể null)
     * @return đối tượng OTP tương ứng
     */
    public OTP createOTPFromString(String otpTypeStr, String userId, String recipient, String transactionId) {
        try {
            OTPType otpType = OTPType.valueOf(otpTypeStr);
            return createOTP(otpType, userId, recipient, transactionId);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid OTP type: " + otpTypeStr);
        }
    }
}