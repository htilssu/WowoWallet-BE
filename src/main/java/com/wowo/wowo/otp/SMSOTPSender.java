package com.wowo.wowo.otp;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

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
            String smsContent = extractSmsContent(content);

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

    /**
     * Loại bỏ HTML và trích xuất nội dung cần thiết cho SMS
     */
    private String extractSmsContent(String htmlContent) {
        // Tìm mã OTP trong nội dung HTML
        String otpCode = extractOTPFromHTML(htmlContent);

        // Tìm thông báo trong nội dung HTML
        String message = extractMessageFromHTML(htmlContent);

        // Tạo nội dung SMS ngắn gọn
        return "Mã OTP " + message + " " + otpCode
                + ". Mã có hiệu lực trong thời gian ngắn. Không chia sẻ mã này với bất kỳ ai.";
    }

    /**
     * Trích xuất mã OTP từ nội dung HTML
     */
    private String extractOTPFromHTML(String htmlContent) {
        // Tìm nội dung giữa {{OTP}} trong template
        int start = htmlContent.indexOf("{{OTP}}");
        if (start != -1) {
            // Tìm thẻ div chứa OTP
            int divStart = htmlContent.lastIndexOf("<div class=\"otp-code\">", start);
            int divEnd = htmlContent.indexOf("</div>", start);

            if (divStart != -1 && divEnd != -1) {
                // Trích xuất nội dung giữa thẻ div
                return htmlContent.substring(divStart + "<div class=\"otp-code\">".length(), divEnd).trim();
            }
        }

        return "UNKNOWN"; // Không tìm thấy mã OTP
    }

    /**
     * Trích xuất thông báo từ nội dung HTML
     */
    private String extractMessageFromHTML(String htmlContent) {
        // Tìm nội dung giữa {{MESSAGE}} trong template
        int start = htmlContent.indexOf("{{MESSAGE}}");
        if (start != -1) {
            // Tìm phần văn bản trước {{MESSAGE}}
            int beforeMessageStart = htmlContent.lastIndexOf(">", start);
            int afterMessageEnd = htmlContent.indexOf("<", start);

            if (beforeMessageStart != -1 && afterMessageEnd != -1) {
                // Lấy đoạn văn bản chứa {{MESSAGE}}
                String messageLine = htmlContent.substring(beforeMessageStart + 1, afterMessageEnd);
                // Thay thế {{MESSAGE}} bằng chuỗi rỗng để lấy các từ xung quanh
                return messageLine.replace("{{MESSAGE}}", "").trim();
            }
        }

        return "của bạn là"; // Thông báo mặc định
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