package com.wowo.wowo.otp;

import static jakarta.mail.Message.RecipientType.TO;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Qualifier;

import com.wowo.wowo.constant.Constant.OTPType;
import com.wowo.wowo.data.MailContent;

import jakarta.mail.MessagingException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Triển khai OTPSender sử dụng email để gửi nội dung
 */
@AllArgsConstructor
@Service
@Qualifier("emailOTPSender")
@Slf4j
public class EmailOTPSender implements OTPSender {

    private final JavaMailSender javaMailSender;

    @Override
    public boolean send(String recipientAddress, String subject, String content) {
        var newMail = javaMailSender.createMimeMessage();

        try {
            newMail.setSubject(subject, "utf-8");
            newMail.addRecipients(TO, recipientAddress);
            newMail.setContent(content, "text/html; charset=utf-8");
        } catch (MessagingException e) {
            log.warn("Lỗi khi tạo email: " + e.getMessage());
            return false;
        }

        try {
            javaMailSender.send(newMail);
            return true;
        } catch (Exception e) {
            log.warn("Không thể gửi email: " + e.getMessage(), e);
            return false;
        }
    }

    @Override
    public boolean sendOTP(OTP otp) {
        // Tạo tiêu đề và nội dung dựa trên loại OTP
        String subject = generateSubject(otp);
        String content = generateContent(otp);

        // Gửi email
        return send(otp.getRecipient(), subject, content);
    }

    /**
     * Tạo tiêu đề email dựa trên loại OTP
     */
    private String generateSubject(OTP otp) {
        OTPType otpType = otp.getOtpType();

        return switch (otpType) {
            case PASSWORD_RESET -> "Đặt lại mật khẩu - Mã xác thực OTP";
            case EMAIL_VERIFICATION -> "Xác minh email - Mã xác thực OTP";
            case ACCOUNT_VERIFICATION -> "Xác minh tài khoản - Mã xác thực OTP";
            case FINANCIAL_OPERATION -> "Xác nhận giao dịch tài chính - Mã xác thực OTP";
            case WITHDRAW_CONFIRMATION -> "Xác nhận rút tiền - Mã xác thực OTP";
            case TRANSACTION_CONFIRMATION -> "Xác nhận giao dịch - Mã xác thực OTP";
            default -> "Mã xác thực OTP";
        };
    }

    /**
     * Tạo nội dung dựa trên loại OTP
     */
    private String generateContent(OTP otp) {
        OTPType otpType = otp.getOtpType();
        String otpCode = otp.getCode();

        // Xác định nếu là OTP tài chính hoặc giao dịch
        boolean isFinancialOTP = (otpType == OTPType.FINANCIAL_OPERATION ||
                otpType == OTPType.TRANSACTION_CONFIRMATION ||
                otpType == OTPType.WITHDRAW_CONFIRMATION);

        if (isFinancialOTP) {
            // Tạo nội dung cho OTP tài chính
            String baseTemplate = MailContent.TRANSACTION_OTP_BODY
                    .replace("{{OTP}}", otpCode)
                    .replace("{{TRANSACTION_ID}}", "N/A");

            // Thêm thông báo tùy theo loại OTP tài chính
            String message = switch (otpType) {
                case WITHDRAW_CONFIRMATION -> "để xác nhận yêu cầu rút tiền";
                case TRANSACTION_CONFIRMATION -> "để xác nhận giao dịch chuyển tiền";
                case FINANCIAL_OPERATION -> "để xác nhận thao tác tài chính";
                default -> "để xác nhận thao tác tài chính";
            };

            return baseTemplate.replace("{{MESSAGE}}", message);
        } else {
            // Sử dụng template OTP cơ bản cho tất cả các trường hợp khác
            String baseTemplate = MailContent.OTP_BODY;

            // Thêm thông báo tùy theo loại OTP
            String messageByType = switch (otpType) {
                case PASSWORD_RESET -> "để đặt lại mật khẩu cho tài khoản của bạn";
                case EMAIL_VERIFICATION -> "để xác minh địa chỉ email của bạn";
                case ACCOUNT_VERIFICATION -> "để xác minh tài khoản của bạn";
                default -> "để xác nhận thao tác của bạn";
            };

            return baseTemplate
                    .replace("{{OTP}}", otpCode)
                    .replace("{{MESSAGE}}", messageByType);
        }
    }
}
