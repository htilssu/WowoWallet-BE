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
        return switch (mailType) {
            case FORGOT_PASSWORD -> FORGOT_PASSWORD_BODY.replace("{{LINK}}", body);
            case OTP -> OTP_TEMPLATE.replace("{{OTP}}", body);
            case TRANSACTION_OTP -> TRANSACTION_OTP_BODY
                    .replace("{{OTP}}", body)
                    .replace("{{TRANSACTION_ID}}", "N/A")
                    .replace("{{MESSAGE}}", "để xác nhận thao tác của bạn");
            default -> null;
        };
    }

    public static String FORGOT_PASSWORD_BODY;
    public static String OTP_TEMPLATE; // Đổi tên để tránh trùng lặp

    static {
        // Reset mail
        try (final InputStream resourceAsStream = MailContent.class.getResourceAsStream(
                "/RESET_MAIL.html")) {
            if (resourceAsStream != null) {
                FORGOT_PASSWORD_BODY = new String(resourceAsStream.readAllBytes());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // OTP mail
        try (final InputStream resourceAsStream = MailContent.class.getResourceAsStream(
                "/OTP_MAIL.html")) {
            if (resourceAsStream != null) {
                OTP_TEMPLATE = new String(resourceAsStream.readAllBytes());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Mẫu HTML cho email OTP cơ bản
     */
    public static final String OTP_BODY = """
            <!DOCTYPE html>
            <html>
            <head>
                <meta charset="UTF-8">
                <title>Mã xác thực OTP</title>
                <style>
                    body { font-family: Arial, sans-serif; margin: 0; padding: 0; background-color: #f6f6f6; }
                    .container { width: 100%; max-width: 600px; margin: 0 auto; background-color: #ffffff; padding: 20px; }
                    .header { background-color: #2d8cf0; color: white; padding: 10px; text-align: center; }
                    .content { padding: 20px; color: #333333; }
                    .otp-code { font-size: 24px; font-weight: bold; text-align: center; padding: 15px; background-color: #f1f1f1; border-radius: 5px; letter-spacing: 5px; }
                    .footer { font-size: 12px; color: #999999; margin-top: 30px; text-align: center; }
                </style>
            </head>
            <body>
                <div class="container">
                    <div class="header">
                        <h2>Mã xác thực OTP</h2>
                    </div>
                    <div class="content">
                        <p>Kính gửi Quý khách,</p>
                        <p>Đây là mã xác thực (OTP) của bạn {{MESSAGE}}:</p>
                        <div class="otp-code">{{OTP}}</div>
                        <p>Mã này sẽ hết hạn sau một khoảng thời gian ngắn.</p>
                        <p>Nếu bạn không yêu cầu mã này, vui lòng bỏ qua email này.</p>
                        <p>Trân trọng,<br>WowoWallet Team</p>
                    </div>
                    <div class="footer">
                        <p>Đây là email tự động, vui lòng không trả lời.</p>
                    </div>
                </div>
            </body>
            </html>
            """;

    /**
     * Mẫu HTML cho email OTP giao dịch với thông tin bổ sung
     */
    public static final String TRANSACTION_OTP_BODY = """
            <!DOCTYPE html>
            <html>
            <head>
                <meta charset="UTF-8">
                <title>Mã xác thực giao dịch</title>
                <style>
                    body { font-family: Arial, sans-serif; margin: 0; padding: 0; background-color: #f6f6f6; }
                    .container { width: 100%; max-width: 600px; margin: 0 auto; background-color: #ffffff; padding: 20px; }
                    .header { background-color: #2d8cf0; color: white; padding: 10px; text-align: center; }
                    .content { padding: 20px; color: #333333; }
                    .otp-code { font-size: 24px; font-weight: bold; text-align: center; padding: 15px; background-color: #f1f1f1; border-radius: 5px; letter-spacing: 5px; }
                    .transaction-info { background-color: #f9f9f9; padding: 15px; margin: 20px 0; border-left: 4px solid #2d8cf0; }
                    .transaction-info p { margin: 5px 0; }
                    .transaction-info .label { font-weight: bold; }
                    .warning { color: #d9534f; font-weight: bold; }
                    .footer { font-size: 12px; color: #999999; margin-top: 30px; text-align: center; }
                </style>
            </head>
            <body>
                <div class="container">
                    <div class="header">
                        <h2>Mã xác thực OTP giao dịch</h2>
                    </div>
                    <div class="content">
                        <p>Kính gửi Quý khách,</p>
                        <p>Đây là mã xác thực (OTP) {{MESSAGE}}:</p>
                        <div class="otp-code">{{OTP}}</div>

                        <div class="transaction-info">
                            <p><span class="label">Mã giao dịch:</span> {{TRANSACTION_ID}}</p>
                        </div>

                        <p>Mã OTP này sẽ hết hạn sau một khoảng thời gian ngắn.</p>
                        <p class="warning">CẢNH BÁO: Vui lòng không chia sẻ mã OTP này cho bất kỳ ai, kể cả nhân viên của chúng tôi.</p>
                        <p>Nếu bạn không thực hiện giao dịch này, vui lòng liên hệ với chúng tôi ngay lập tức.</p>
                        <p>Trân trọng,<br>WowoWallet Team</p>
                    </div>
                    <div class="footer">
                        <p>Đây là email tự động, vui lòng không trả lời.</p>
                    </div>
                </div>
            </body>
            </html>
            """;

    public enum MailType {
        FORGOT_PASSWORD,
        OTP,
        TRANSACTION_OTP // Thêm loại email mới cho OTP giao dịch
    }

    @Override
    public String toString() {
        return getMailContent();
    }
}
