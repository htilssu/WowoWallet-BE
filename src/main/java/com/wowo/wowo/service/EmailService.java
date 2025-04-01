package com.wowo.wowo.service;

import com.wowo.wowo.data.MailContent;
import com.wowo.wowo.otp.OTPSender;
import jakarta.mail.MessagingException;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;
import java.util.logging.Level;
import java.util.logging.Logger;

import static jakarta.mail.Message.RecipientType.TO;

@Service
public class EmailService {

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


}

