package com.wowo.wowo.data;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.IOException;
import java.io.InputStream;

@Getter
@AllArgsConstructor
public class MailContent {

    String subject;
    @Getter(AccessLevel.NONE)
    String body;
    MailType mailType;
    String receiver;

    public String getMailContent() {
        switch (mailType) {
            case FORGOT_PASSWORD -> {
                return "";
            }
            case OTP -> {
                return OTP_BODY.replace("{{OTP}}", body);
            }
            default -> {
                return null;
            }
        }
    }

    public static String FORGOT_PASSWORD_BODY;
    public static String OTP_BODY;

    static {
        //Reset mail
        try (final InputStream resourceAsStream = MailContent.class.getResourceAsStream(
                "/RESET_MAIL.html")) {
            if (resourceAsStream != null) {
                FORGOT_PASSWORD_BODY = new String(resourceAsStream.readAllBytes());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        //OTP mail
        try (final InputStream resourceAsStream = MailContent.class.getResourceAsStream(
                "/OTP_MAIL.html")) {
            if (resourceAsStream != null) {
                OTP_BODY = new String(resourceAsStream.readAllBytes());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String toString() {
        return getMailContent();
    }
}
