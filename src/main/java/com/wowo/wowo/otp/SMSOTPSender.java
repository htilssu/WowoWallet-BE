package com.wowo.wowo.otp;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.wowo.wowo.constant.Constant.OTPType;

/**
 * Dịch vụ gửi OTP qua tin nhắn SMS
 */
@Service
@Qualifier("smsOTPSender")
public class SMSOTPSender implements OTPSender {

    private final RestTemplate restTemplate;

    @Value("${sms.api.url:https://api.example.com/sms}")
    private String smsApiUrl;

    @Value("${sms.api.key:default-api-key}")
    private String apiKey;

    public SMSOTPSender() {
        this.restTemplate = new RestTemplate();
    }

    @Override
    public boolean send(String recipientPhoneNumber, String subject, String content) {
        try {
            // Chuẩn bị nội dung tin nhắn SMS (không cần subject như email)
            String smsContent = content;

            // Chuẩn bị dữ liệu để gửi đến SMS API
            Map<String, String> smsRequest = new HashMap<>();
            smsRequest.put("phone", formatPhoneNumber(recipientPhoneNumber));
            smsRequest.put("message", smsContent);
            smsRequest.put("apiKey", apiKey);

            // Gọi API SMS
            // Lưu ý: Phần này cần thay thế bằng API SMS thật khi triển khai
            String response = restTemplate.postForObject(smsApiUrl, smsRequest, String.class);

            // Xử lý kết quả từ SMS API (giả định là thành công nếu không có ngoại lệ)
            return true;
        } catch (Exception e) {
            Logger.getAnonymousLogger().log(Level.SEVERE, "Lỗi khi gửi SMS: " + e.getMessage(), e);
            return false;
        }
    }

    @Override
    public boolean sendOTP(OTP otp) {
        // Tạo nội dung SMS dựa trên loại OTP
        String smsContent = generateSmsContent(otp);

        // Gửi SMS (không cần subject)
        return send(otp.getRecipient(), null, smsContent);
    }

    /**
     * Tạo nội dung SMS dựa trên loại OTP
     */
    private String generateSmsContent(OTP otp) {
        OTPType otpType = otp.getOtpType();
        String otpCode = otp.getCode();

        // Xác định nội dung thông báo dựa trên loại OTP
        String message;

        // Thêm thông báo khác nhau tùy theo loại OTP
        if (otp instanceof TransactionOTP) {
            TransactionOTP transactionOTP = (TransactionOTP) otp;
            String transactionId = transactionOTP.getTransactionId();

            message = switch (otpType) {
                case WITHDRAW_CONFIRMATION -> "xác nhận rút tiền";
                case TRANSACTION_CONFIRMATION -> "xác nhận giao dịch";
                default -> "xác nhận giao dịch tài chính";
            };

            return String.format(
                    "Mã OTP %s là %s. Mã giao dịch: %s. Có hiệu lực trong %d phút. KHÔNG chia sẻ mã này với bất kỳ ai.",
                    message, otpCode, transactionId, otpType.getExpirationTimeInMinutes());
        } else if (otp instanceof PasswordResetOTP) {
            PasswordResetOTP passwordResetOTP = (PasswordResetOTP) otp;
            String token = passwordResetOTP.getToken();

            String baseMessage = String.format(
                    "Mã OTP đặt lại mật khẩu là %s. Có hiệu lực trong %d phút. KHÔNG chia sẻ mã này với bất kỳ ai.",
                    otpCode, otpType.getExpirationTimeInMinutes());

            if (token != null && !token.isEmpty()) {
                return baseMessage + "\nToken đặt lại mật khẩu: " + token;
            }

            return baseMessage;
        } else {
            message = switch (otpType) {
                case PASSWORD_RESET -> "đặt lại mật khẩu";
                case EMAIL_VERIFICATION -> "xác minh email";
                case ACCOUNT_VERIFICATION -> "xác minh tài khoản";
                default -> "xác thực";
            };

            return String.format(
                    "Mã OTP %s của bạn là %s. Có hiệu lực trong %d phút. KHÔNG chia sẻ mã này với bất kỳ ai.",
                    message, otpCode, otpType.getExpirationTimeInMinutes());
        }
    }

    /**
     * Định dạng số điện thoại để phù hợp với yêu cầu của API
     */
    private String formatPhoneNumber(String phoneNumber) {
        // Loại bỏ khoảng trắng, dấu gạch ngang và các ký tự không phải số
        String digitsOnly = phoneNumber.replaceAll("[^0-9]", "");

        // Đảm bảo số điện thoại bắt đầu bằng mã quốc gia (+84 cho Việt Nam)
        if (digitsOnly.startsWith("0")) {
            return "+84" + digitsOnly.substring(1);
        } else if (!digitsOnly.startsWith("84")) {
            return "+84" + digitsOnly;
        }

        return "+" + digitsOnly;
    }
}