package com.wowo.wowo.controller;

import com.wowo.wowo.constant.Constant.OTPType;
import com.wowo.wowo.data.dto.OTPRequestDTO;
import com.wowo.wowo.otp.OTPManager;
import com.wowo.wowo.otp.OTPFactory.OTPChannel;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;

/**
 * Controller xử lý các yêu cầu liên quan đến OTP
 */
@RestController
@RequestMapping("/api/otp")
@RequiredArgsConstructor
public class OTPController {

    private final OTPManager otpManager;

    /**
     * Gửi OTP theo yêu cầu
     * 
     * @param otpRequestDTO  yêu cầu OTP
     * @param authentication thông tin xác thực của người dùng
     * @return kết quả gửi OTP
     */
    @PostMapping("/send")
    public ResponseEntity<?> sendOTP(@RequestBody OTPRequestDTO otpRequestDTO,
            Authentication authentication) {
        boolean sent = otpManager.send(otpRequestDTO, authentication);

        if (sent) {
            return ResponseEntity.ok().body(
                    new ApiResponse(true, "OTP đã được gửi thành công"));
        } else {
            return ResponseEntity.badRequest().body(
                    new ApiResponse(false, "Không thể gửi OTP"));
        }
    }

    /**
     * Gửi OTP qua kênh được chỉ định
     * 
     * @param otpRequestDTO  yêu cầu OTP
     * @param channel        kênh gửi OTP (EMAIL, SMS)
     * @param authentication thông tin xác thực của người dùng
     * @return kết quả gửi OTP
     */
    @PostMapping("/send/{channel}")
    public ResponseEntity<?> sendOTPWithChannel(
            @RequestBody OTPRequestDTO otpRequestDTO,
            @PathVariable String channel,
            Authentication authentication) {

        OTPChannel otpChannel;
        try {
            otpChannel = OTPChannel.valueOf(channel.toUpperCase());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(
                    new ApiResponse(false, "Kênh gửi OTP không hợp lệ. Các lựa chọn hợp lệ: EMAIL, SMS"));
        }

        boolean sent = otpManager.send(otpRequestDTO, authentication, otpChannel);

        if (sent) {
            return ResponseEntity.ok().body(
                    new ApiResponse(true, "OTP đã được gửi thành công qua " + channel));
        } else {
            return ResponseEntity.badRequest().body(
                    new ApiResponse(false, "Không thể gửi OTP qua " + channel));
        }
    }

    /**
     * Xác minh OTP
     * 
     * @param userId        ID người dùng
     * @param otpCode       mã OTP
     * @param otpTypeStr    loại OTP dạng chuỗi
     * @param transactionId ID giao dịch (nếu có)
     * @return kết quả xác minh OTP
     */
    @PostMapping("/verify")
    public ResponseEntity<?> verifyOTP(@RequestParam String userId,
            @RequestParam String otpCode,
            @RequestParam String otpTypeStr,
            @RequestParam(required = false) String transactionId) {
        try {
            OTPType otpType = OTPType.valueOf(otpTypeStr);
            boolean verified;

            // Xác định loại OTP và phương thức xác minh phù hợp
            boolean isTransactionRelated = otpType == OTPType.TRANSACTION_CONFIRMATION ||
                    otpType == OTPType.WITHDRAW_CONFIRMATION;

            if (isTransactionRelated) {
                if (transactionId == null) {
                    return ResponseEntity.badRequest().body(
                            new ApiResponse(false, "Transaction ID bắt buộc phải có để xác minh OTP giao dịch"));
                }
                verified = otpManager.verify(userId, otpCode, otpType, transactionId);
            } else {
                verified = otpManager.verify(userId, otpCode, otpType);
            }

            if (verified) {
                return ResponseEntity.ok().body(
                        new ApiResponse(true, "OTP hợp lệ"));
            } else {
                return ResponseEntity.badRequest().body(
                        new ApiResponse(false, "OTP không hợp lệ hoặc đã hết hạn"));
            }
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(
                    new ApiResponse(false, "Loại OTP không hợp lệ: " + otpTypeStr));
        }
    }

    /**
     * Lớp đại diện cho phản hồi API
     */
    private record ApiResponse(boolean success, String message) {
    }
}
