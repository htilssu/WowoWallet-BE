package com.wowo.wowo.otp;

import com.wowo.wowo.constant.Constant.OTPType;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 * Factory class để tạo các đối tượng OTP
 */
@Component
@AllArgsConstructor
public class OTPFactory {

    @Qualifier("emailOTPSender")
    private OTPSender emailOTPSender;

    @Qualifier("smsOTPSender")
    private OTPSender smsOTPSender;

    /**
     * Enum xác định phương thức gửi OTP
     */
    public enum OTPChannel {
        EMAIL, // Gửi qua email
        SMS // Gửi qua tin nhắn SMS
    }

    /**
     * Tạo đối tượng OTP thích hợp dựa trên loại OTP và kênh gửi
     * 
     * @param otpType       loại OTP
     * @param userId        ID người dùng
     * @param recipient     địa chỉ người nhận
     * @param transactionId ID giao dịch (cần thiết cho OTP liên quan đến giao dịch)
     * @param channel       kênh gửi OTP (EMAIL hoặc SMS)
     * @return đối tượng OTP tương ứng
     */
    public OTP createOTP(OTPType otpType, String userId, String recipient, String transactionId, OTPChannel channel) {
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
     * @param otpType       loại OTP
     * @param userId        ID người dùng
     * @param recipient     địa chỉ người nhận
     * @param transactionId ID giao dịch (cần thiết cho OTP liên quan đến giao dịch)
     * @return đối tượng OTP tương ứng
     */
    public OTP createOTP(OTPType otpType, String userId, String recipient, String transactionId) {
        return createOTP(otpType, userId, recipient, transactionId, OTPChannel.EMAIL);
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

        return createOTP(otpType, userId, recipient, null, OTPChannel.EMAIL);
    }

    /**
     * Tạo đối tượng OTP thích hợp dựa trên loại OTP và kênh gửi (phương thức
     * overload cho OTP
     * không liên quan đến giao dịch)
     * 
     * @param otpType   loại OTP
     * @param userId    ID người dùng
     * @param recipient địa chỉ người nhận
     * @param channel   kênh gửi OTP (EMAIL hoặc SMS)
     * @return đối tượng OTP tương ứng
     */
    public OTP createOTP(OTPType otpType, String userId, String recipient, OTPChannel channel) {
        boolean isTransactionRelated = otpType == OTPType.TRANSACTION_CONFIRMATION ||
                otpType == OTPType.WITHDRAW_CONFIRMATION;

        if (isTransactionRelated) {
            throw new IllegalArgumentException("Transaction ID is required for transaction-related OTPs");
        }

        return createOTP(otpType, userId, recipient, null, channel);
    }

    /**
     * Tạo đối tượng OTP từ chuỗi otpType
     * 
     * @param otpTypeStr    chuỗi biểu diễn loại OTP
     * @param userId        ID người dùng
     * @param recipient     địa chỉ người nhận
     * @param transactionId ID giao dịch (có thể null)
     * @param channel       kênh gửi OTP (EMAIL hoặc SMS)
     * @return đối tượng OTP tương ứng
     */
    public OTP createOTPFromString(String otpTypeStr, String userId, String recipient, String transactionId,
            OTPChannel channel) {
        try {
            OTPType otpType = OTPType.valueOf(otpTypeStr);
            return createOTP(otpType, userId, recipient, transactionId, channel);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid OTP type: " + otpTypeStr);
        }
    }

    /**
     * Tạo đối tượng OTP từ chuỗi otpType (mặc định gửi qua email)
     * 
     * @param otpTypeStr    chuỗi biểu diễn loại OTP
     * @param userId        ID người dùng
     * @param recipient     địa chỉ người nhận
     * @param transactionId ID giao dịch (có thể null)
     * @return đối tượng OTP tương ứng
     */
    public OTP createOTPFromString(String otpTypeStr, String userId, String recipient, String transactionId) {
        return createOTPFromString(otpTypeStr, userId, recipient, transactionId, OTPChannel.EMAIL);
    }
}