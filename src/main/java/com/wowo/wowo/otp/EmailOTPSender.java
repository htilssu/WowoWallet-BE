package com.wowo.wowo.otp;

import static jakarta.mail.Message.RecipientType.TO;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Qualifier;

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
}
