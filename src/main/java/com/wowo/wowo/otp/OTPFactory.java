package com.wowo.wowo.otp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.wowo.wowo.constant.Constant.OTPType;
import com.wowo.wowo.service.UserService;

/**
 * Factory tạo ra các đối tượng OTP khác nhau dựa trên loại OTP.
 * Áp dụng mẫu thiết kế Factory Method.
 */
@Component
public class OTPFactory {

    @Autowired
    @Qualifier("emailOTPSender")
    private OTPSender emailOTPSender;

    @Autowired
    @Qualifier("smsOTPSender")
    private OTPSender smsOTPSender;

    @Autowired
    private UserService userService;

    /**
     * Enum định nghĩa các kênh gửi OTP
     */
    public enum OTPChannel {
        EMAIL, // Gửi qua email
        SMS // Gửi qua SMS
    }

    /**
     * Tạo đối tượng OTP thích hợp dựa trên loại OTP và kênh gửi
     * 
     * @param otpType   loại OTP
     * @param userId    ID người dùng
     * @param recipient địa chỉ người nhận
     * @param channel   kênh gửi OTP (EMAIL hoặc SMS)
     * @return đối tượng OTP tương ứng
     */
    public OTP createOTP(OTPType otpType, String userId, String recipient, OTPChannel channel) {
        OTP otp;

        switch (otpType) {
            case PASSWORD_RESET:
                otp = new PasswordResetOTP(userId, recipient);
                break;
            case EMAIL_VERIFICATION:
                otp = new EmailVerificationOTP(userId, recipient, userService);
                break;
            case ACCOUNT_VERIFICATION:
                otp = new EmailVerificationOTP(userId, recipient, userService);
                otp.setOtpType(OTPType.ACCOUNT_VERIFICATION);
                break;
            case FINANCIAL_OPERATION:
                otp = new FinancialOTP(userId, recipient);
                break;
            case TRANSACTION_CONFIRMATION:
                otp = new FinancialOTP(userId, recipient, OTPType.TRANSACTION_CONFIRMATION);
                break;
            case WITHDRAW_CONFIRMATION:
                otp = new FinancialOTP(userId, recipient, OTPType.WITHDRAW_CONFIRMATION);
                break;
            default:
                throw new IllegalArgumentException("Unsupported OTP type: " + otpType);
        }

        // Inject OTPSender phù hợp vào đối tượng OTP
        if (channel == OTPChannel.EMAIL) {
            otp.setOTPSender(emailOTPSender);
        } else { // SMS
            otp.setOTPSender(smsOTPSender);
        }

        otp.setExpirationTime();
        return otp;
    }

    /**
     * Tạo đối tượng OTP thích hợp dựa trên loại OTP (mặc định gửi qua email)
     * 
     * @param otpType   loại OTP
     * @param userId    ID người dùng
     * @param recipient địa chỉ người nhận
     * @return đối tượng OTP tương ứng
     */
    public OTP createOTP(OTPType otpType, String userId, String recipient) {
        return createOTP(otpType, userId, recipient, OTPChannel.EMAIL);
    }

    /**
     * Tạo đối tượng OTP từ chuỗi otpType và kênh gửi
     * 
     * @param otpTypeStr chuỗi biểu diễn loại OTP
     * @param userId     ID người dùng
     * @param recipient  địa chỉ người nhận
     * @param channel    kênh gửi OTP (EMAIL hoặc SMS)
     * @return đối tượng OTP tương ứng
     */
    public OTP createOTP(String otpTypeStr, String userId, String recipient, OTPChannel channel) {
        try {
            OTPType otpType = OTPType.valueOf(otpTypeStr.toUpperCase());
            return createOTP(otpType, userId, recipient, channel);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid OTP type: " + otpTypeStr);
        }
    }

    /**
     * Tạo đối tượng OTP từ chuỗi otpType (mặc định gửi qua email)
     * 
     * @param otpTypeStr chuỗi biểu diễn loại OTP
     * @param userId     ID người dùng
     * @param recipient  địa chỉ người nhận
     * @return đối tượng OTP tương ứng
     */
    public OTP createOTP(String otpTypeStr, String userId, String recipient) {
        return createOTP(otpTypeStr, userId, recipient, OTPChannel.EMAIL);
    }
}