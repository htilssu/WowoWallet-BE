package com.wowo.wowo.controller;

import com.wowo.wowo.constant.Constant.OTPType;
import com.wowo.wowo.data.dto.OTPRequestDTO;
import com.wowo.wowo.data.dto.OTPVerifyDTO;
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
     * @param otpRequestDTO  yêu cầu OTP chứa thông tin gửi (loại OTP, phương thức
     *                       gửi, v.v.)
     * @param authentication thông tin xác thực của người dùng
     * @return kết quả gửi OTP
     */
    @PostMapping("/send")
    public ResponseEntity<?> sendOTP(
            @RequestBody OTPRequestDTO otpRequestDTO,
            Authentication authentication) {
        // Lấy kênh gửi từ DTO (EMAIL hoặc SMS)
        OTPChannel channel = otpRequestDTO.getSendChannel();

        // Gửi OTP với kênh đã chọn
        boolean sent = otpManager.send(otpRequestDTO, authentication, channel);

        if (sent) {
            return ResponseEntity.ok().body(
                    new ApiResponse(true, "OTP đã được gửi thành công qua " + channel.name()));
        } else {
            return ResponseEntity.badRequest().body(
                    new ApiResponse(false, "Không thể gửi OTP qua " + channel.name()));
        }
    }

    /**
     * Xác minh OTP
     * 
     * @param verifyDTO      DTO chứa thông tin xác minh OTP
     * @param authentication thông tin xác thực của người dùng
     * @return kết quả xác minh OTP
     */
    @PostMapping("/verify")
    public ResponseEntity<?> verifyOTP(
            @RequestBody OTPVerifyDTO verifyDTO,
            Authentication authentication) {
        try {
            boolean verified = otpManager.verify(authentication, verifyDTO);

            if (verified) {
                return ResponseEntity.ok().body(
                        new ApiResponse(true, "OTP hợp lệ"));
            } else {
                return ResponseEntity.badRequest().body(
                        new ApiResponse(false, "OTP không hợp lệ hoặc đã hết hạn"));
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    new ApiResponse(false, "Lỗi xử lý OTP: " + e.getMessage()));
        }
    }

    /**
     * Lớp đại diện cho phản hồi API
     */
    private record ApiResponse(boolean success, String message) {
    }
}
