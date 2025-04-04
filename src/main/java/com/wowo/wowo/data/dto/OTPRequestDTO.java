package com.wowo.wowo.data.dto;

import com.wowo.wowo.constant.Constant.OTPType;
import com.wowo.wowo.otp.OTPFactory.OTPChannel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO chứa thông tin yêu cầu gửi OTP từ controller
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OTPRequestDTO {

    /**
     * Loại OTP
     */
    private OTPType otpType;

    /**
     * Địa chỉ người nhận (email, số điện thoại)
     */
    private String recipient;

    /**
     * Phương thức gửi OTP (EMAIL hoặc SMS)
     * Mặc định là EMAIL nếu không được chỉ định
     */
    private String sendChannel;

    /**
     * Chuyển đổi sendMethod thành OTPChannel
     * 
     * @return OTPChannel tương ứng (EMAIL hoặc SMS)
     */
    public OTPChannel getSendChannel() {
        try {
            return OTPChannel.valueOf(sendChannel.toUpperCase());
        } catch (IllegalArgumentException e) {
            // Mặc định trả về EMAIL nếu giá trị không hợp lệ
            return OTPChannel.EMAIL;
        }
    }
}