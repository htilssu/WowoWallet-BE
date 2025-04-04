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
            case WITHDRAW_CONFIRMATION -> "Xác nhận rút tiền - Mã xác thực OTP";
            case TRANSACTION_CONFIRMATION -> "Xác nhận giao dịch - Mã xác thực OTP";
            default -> "Mã xác thực OTP";
        };
    }

    /**
     * Tạo nội dung email dựa trên loại OTP
     */
    private String generateContent(OTP otp) {
        OTPType otpType = otp.getOtpType();
        String otpCode = otp.getCode();

        // Xác định nội dung thông báo và template dựa trên loại OTP
        boolean isTransactionOTP = otp instanceof TransactionOTP;

        if (isTransactionOTP) {
            TransactionOTP transactionOTP = (TransactionOTP) otp;
            String transactionId = transactionOTP.getTransactionId();

            // Tạo nội dung cho OTP liên quan đến giao dịch
            String baseTemplate = MailContent.TRANSACTION_OTP_BODY
                    .replace("{{OTP}}", otpCode)
                    .replace("{{TRANSACTION_ID}}", transactionId);

            // Thêm thông báo tùy theo loại OTP giao dịch
            String messageByType = switch (otpType) {
                case WITHDRAW_CONFIRMATION -> "để xác nhận yêu cầu rút tiền";
                case TRANSACTION_CONFIRMATION -> "để xác nhận giao dịch";
                default -> "để xác nhận thao tác tài chính";
            };

            return baseTemplate.replace("{{MESSAGE}}", messageByType);
        } else if (otp instanceof PasswordResetOTP) {
            // Xử lý đặc biệt cho PasswordResetOTP
            PasswordResetOTP passwordResetOTP = (PasswordResetOTP) otp;
            String token = passwordResetOTP.getToken();

            String baseContent = MailContent.OTP_BODY
                    .replace("{{OTP}}", otpCode)
                    .replace("{{MESSAGE}}", "để đặt lại mật khẩu cho tài khoản của bạn.");

            // Nếu có token, thêm vào nội dung
            if (token != null && !token.isEmpty()) {
                baseContent = baseContent.replace("</body>",
                        "<p>Bạn cũng có thể sử dụng token sau để đặt lại mật khẩu: " + token + "</p></body>");
            }

            return baseContent;
        } else {
            // Tạo nội dung cho OTP thông thường
            String baseTemplate = MailContent.OTP_BODY.replace("{{OTP}}", otpCode);

            // Thêm thông báo tùy theo loại OTP
            String messageByType = switch (otpType) {
                case PASSWORD_RESET -> "để đặt lại mật khẩu cho tài khoản của bạn.";
                case EMAIL_VERIFICATION -> "để xác minh địa chỉ email của bạn.";
                case ACCOUNT_VERIFICATION -> "để xác minh tài khoản của bạn.";
                default -> "để xác nhận thao tác của bạn.";
            };

            return baseTemplate.replace("{{MESSAGE}}", messageByType);
        }
    }
}
