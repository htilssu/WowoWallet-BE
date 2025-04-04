package com.wowo.wowo.otp;

import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.wowo.wowo.constant.Constant.OTPType;

import lombok.Data;

/**
 * Dịch vụ gửi OTP qua tin nhắn SMS sử dụng ESMS API
 */
@Service
@Qualifier("smsOTPSender")
public class SMSOTPSender implements OTPSender {

    private final RestTemplate restTemplate;
    private final Logger logger = Logger.getLogger(SMSOTPSender.class.getName());

    /**
     * URL của ESMS API SendMultipleMessage_V4
     */
    @Value("${esms.api.url:https://rest.esms.vn/MainService.svc/json/SendMultipleMessage_V4_post_json/}")
    private String esmsApiUrl;

    /**
     * API Key của tài khoản ESMS
     */
    @Value("${esms.api.key:default-api-key}")
    private String apiKey;

    /**
     * Secret Key của tài khoản ESMS
     */
    @Value("${esms.secret.key:default-secret-key}")
    private String secretKey;

    /**
     * Brandname đã đăng ký với ESMS (tên công ty/tổ chức hiển thị trên tin nhắn)
     */
    @Value("${esms.brandname:WowoWallet}")
    private String brandname;

    /**
     * Chế độ Sandbox
     * 1: Môi trường test, không gửi tin nhắn thật và không trừ tiền
     * 0: Môi trường thực tế, có gửi tin nhắn thật và trừ tiền
     */
    @Value("${esms.sandbox:1}")
    private String sandbox;

    public SMSOTPSender() {
        this.restTemplate = new RestTemplate();
    }

    @Override
    public boolean send(String recipientPhoneNumber, String subject, String content) {
        try {
            // Chuẩn bị dữ liệu cho ESMS API
            ESMSRequest request = new ESMSRequest();
            request.setApiKey(apiKey);
            request.setSecretKey(secretKey);
            request.setPhone(formatPhoneNumber(recipientPhoneNumber));

            // Tạo nội dung SMS với mã OTP và chủ đề
            // Định dạng: "{OTP} là mã xác minh {subject}"
            request.setContent(content + " là mã xác minh " + subject);

            // Thiết lập các thông số khác
            request.setBrandname(brandname);
            request.setSmsType("2"); // Tin CSKH
            request.setIsUnicode("1"); // Có dấu
            request.setRequestId(UUID.randomUUID().toString());

            // Thiết lập CallbackUrl nếu cần
            // request.setCallbackUrl("https://wowowallet.com/sms-callback");

            // Thiết lập headers
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            // Tạo HTTP entity
            HttpEntity<ESMSRequest> entity = new HttpEntity<>(request, headers);

            // Gọi API
            logger.info("Gửi OTP đến " + recipientPhoneNumber + " với nội dung: " + content);
            ESMSResponse response = restTemplate.postForObject(esmsApiUrl, entity, ESMSResponse.class);

            // Kiểm tra kết quả
            if (response != null && response.getCodeResult() == 100) {
                logger.info("Gửi OTP thành công đến " + recipientPhoneNumber);
                return true;
            } else {
                logger.severe("Lỗi khi gửi OTP: " +
                        (response != null
                                ? "Mã lỗi: " + response.getCodeResult() + ", Thông báo: " + response.getErrorMessage()
                                : "Không có phản hồi từ API"));
                return false;
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Lỗi khi gửi OTP: " + e.getMessage(), e);
            return false;
        }
    }

    @Override
    public boolean sendOTP(OTP otp) {
        try {
            String phoneNumber = otp.getRecipient();
            String otpCode = otp.getCode();
            String subject = generateSubject(otp.getOtpType());

            return send(phoneNumber, subject, otpCode);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Lỗi khi gửi SMS OTP: " + e.getMessage(), e);
            return false;
        }
    }

    /**
     * Tạo chủ đề SMS dựa trên loại OTP
     */
    private String generateSubject(OTPType otpType) {
        return switch (otpType) {
            case FINANCIAL_OPERATION -> "thao tác tài chính WowoWallet";
            case WITHDRAW_CONFIRMATION -> "xác nhận rút tiền WowoWallet";
            case TRANSACTION_CONFIRMATION -> "xác nhận giao dịch WowoWallet";
            case PASSWORD_RESET -> "đặt lại mật khẩu WowoWallet";
            case EMAIL_VERIFICATION -> "xác minh email WowoWallet";
            case ACCOUNT_VERIFICATION -> "xác minh tài khoản WowoWallet";
            default -> "WowoWallet";
        };
    }

    /**
     * Định dạng số điện thoại để phù hợp với yêu cầu của API
     * ESMS yêu cầu số điện thoại không có mã quốc gia và không có dấu +
     */
    private String formatPhoneNumber(String phoneNumber) {
        // Loại bỏ khoảng trắng, dấu gạch ngang và các ký tự không phải số
        String digitsOnly = phoneNumber.replaceAll("[^0-9]", "");

        // Xóa mã quốc gia nếu có (ESMS yêu cầu số điện thoại không có mã quốc gia)
        if (digitsOnly.startsWith("84") && digitsOnly.length() > 9) {
            return digitsOnly.substring(2);
        } else if (digitsOnly.startsWith("0")) {
            return digitsOnly;
        } else {
            return "0" + digitsOnly;
        }
    }

    /**
     * Lớp đại diện cho request gửi đến ESMS API SendMultipleMessage_V4
     */
    @Data
    private static class ESMSRequest {
        /**
         * ApiKey của tài khoản ESMS
         */
        @JsonProperty("ApiKey")
        private String apiKey;

        /**
         * Nội dung tin nhắn SMS
         */
        @JsonProperty("Content")
        private String content;

        /**
         * Số điện thoại nhận tin
         */
        @JsonProperty("Phone")
        private String phone;

        /**
         * SecretKey của tài khoản ESMS
         */
        @JsonProperty("SecretKey")
        private String secretKey;

        /**
         * Tên Brandname (tên công ty/tổ chức hiển thị trên tin nhắn)
         */
        @JsonProperty("Brandname")
        private String brandname;

        /**
         * Loại tin nhắn
         * 2: Tin CSKH
         */
        @JsonProperty("SmsType")
        private String smsType;

        /**
         * Gửi tin nhắn có dấu
         * 0: Không dấu
         * 1: Có dấu
         */
        @JsonProperty("IsUnicode")
        private String isUnicode;

        /**
         * Chế độ sandbox
         * 1: Tin gửi ở môi trường test, không gửi tin nhắn thật, không trừ tiền
         * 0: Tin gửi ở môi trường thực tế, có gửi tin nhắn thật và trừ tiền
         */
        @JsonProperty("Sandbox")
        private String sandbox;

        /**
         * ID đối tác truyền sang để chặn trùng và đối soát khi cần
         */
        @JsonProperty("RequestId")
        private String requestId;

        /**
         * Thời gian hẹn gửi của tin
         * Định dạng: yyyy-mm-dd hh:MM:ss
         */
        @JsonProperty("SendDate")
        private String sendDate;

        /**
         * URL nhận kết quả gửi tin
         */
        @JsonProperty("CallbackUrl")
        private String callbackUrl;
    }

    /**
     * Lớp đại diện cho response từ ESMS API
     */
    @Data
    private static class ESMSResponse {
        /**
         * Mã kết quả
         * 100: Thành công
         * 99: Lỗi
         */
        @JsonProperty("CodeResult")
        private int codeResult;

        /**
         * Số lần tạo lại
         */
        @JsonProperty("CountRegenerate")
        private int countRegenerate;

        /**
         * Thông báo lỗi (nếu có)
         */
        @JsonProperty("ErrorMessage")
        private String errorMessage;
    }
}