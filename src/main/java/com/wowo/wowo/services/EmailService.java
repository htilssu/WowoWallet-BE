package com.wowo.wowo.services;

import com.wowo.wowo.data.MailContent;
import com.wowo.wowo.otp.OTPSender;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Level;
import java.util.logging.Logger;

import static jakarta.mail.Message.RecipientType.TO;

@Service
public class EmailService implements OTPSender {

    public final JavaMailSender javaMailSender;

    public EmailService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;

    }

    public void sendEmail(String toEmail, String subject, String body) {
        var msg = new SimpleMailMessage();
        msg.setTo(toEmail);
        msg.setSubject(subject);
        msg.setText(body);
        javaMailSender.send(msg);
    }

    public void sendEmail(MailContent mailContent) throws MessagingException {
        var msg = javaMailSender.createMimeMessage();
        msg.addRecipients(TO, mailContent.getReceiver());
        msg.setSubject(mailContent.getSubject());
        msg.setContent(mailContent, "text/html; charset=utf-8");
        javaMailSender.send(msg);
    }

    @Override
    public void sendOTP(String sendTo, String otp) {
        var newMail = javaMailSender.createMimeMessage();
        // set the email recipient

        String htmlText = otpTemplate.replace("{{otp}}", otp);

        try {
            newMail.setSubject("Mã xác thực OTP", "utf-8");
            newMail.addRecipients(TO, sendTo);
            newMail.setContent(htmlText, "text/html; charset=utf-8");
        } catch (MessagingException e) {
            Logger.getAnonymousLogger().log(Level.WARNING, e.getMessage());
        }

        javaMailSender.send(newMail);
    }

    @Override
    @Async
    public CompletableFuture<Void> sendOTPAsync(String sendTo, String otp) {
        var newMail = javaMailSender.createMimeMessage();
        // set the email recipient

        String htmlText = otpTemplate.replace("{{otp}}", otp);

        try {
            newMail.setSubject("Mã xác thực OTP", "utf-8");
            newMail.addRecipients(TO, sendTo);
            newMail.setContent(htmlText, "text/html; charset=utf-8");
        } catch (MessagingException e) {
            CompletableFuture.failedFuture(e);
        }

        javaMailSender.send(newMail);

        return CompletableFuture.completedFuture(null);
    }

    public void sendResetPassword(String token, String email) {
        final MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        var resetPasswordHtml = resetPasswordTemplate.replace("{{RESET_LINK}}",
                "https://wowo.htilssu.id.vn/password/reset?token=" + token);
        try {
            mimeMessage.setSubject("Đặt lại mật khẩu", "utf-8");
            mimeMessage.addRecipients(TO, email);
            mimeMessage.setContent(resetPasswordHtml, "text/html; charset=utf-8");
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }

        javaMailSender.send(mimeMessage);
    }

    public void sendResetPasswordToken(@Size(max = 255) @NotNull String email, String token) {

    }
}

