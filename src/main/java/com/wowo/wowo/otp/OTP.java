package com.wowo.wowo.otp;

import com.wowo.wowo.constant.Constant.OTPType;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

/**
 * Lớp abstract OTP là cơ sở cho tất cả các loại OTP trong hệ thống.
 * Nó định nghĩa các thuộc tính và phương thức cơ bản mà tất cả các loại OTP
 * phải có.
 */
@Getter
@Setter
public abstract class OTP {
    protected String code; // Mã OTP
    protected String userId; // ID của người dùng
    protected String recipient; // Địa chỉ người nhận (email, số điện thoại)
    protected Instant createdAt; // Thời điểm tạo OTP
    protected Instant expiresAt; // Thời điểm hết hạn
    protected OTPType otpType; // Loại OTP (xác thực giao dịch, quên mật khẩu, etc)
    protected OTPSender otpSender; // Đối tượng gửi OTP

    /**
     * Kiểm tra OTP có hết hạn chưa
     * 
     * @return true nếu OTP đã hết hạn, false nếu còn hạn
     */
    public boolean isExpired() {
        return Instant.now().isAfter(expiresAt);
    }

    /**
     * Xác minh mã OTP
     * 
     * @param inputCode    mã OTP nhập vào cần xác minh
     * @param otpGenerator generator OTP để kiểm tra tính hợp lệ
     * @return true nếu mã OTP đúng và chưa hết hạn, false nếu sai hoặc hết hạn
     */
    public boolean verify(String inputCode, OTPGenerator otpGenerator) {
        if (isExpired()) {
            return false;
        }
        return otpGenerator.verify(inputCode, otpType);
    }

    /**
     * Thiết lập thời gian hết hạn dựa trên loại OTP
     */
    protected void setExpirationTime() {
        this.createdAt = Instant.now();
        this.expiresAt = createdAt.plusSeconds(otpType.getExpirationTimeInMinutes() * 60L);
    }

    /**
     * Phương thức gửi OTP
     * 
     * @return true nếu gửi thành công, false nếu thất bại
     */
    public boolean send() {
        // Gửi trực tiếp qua đối tượng sender đã được inject
        return otpSender.sendOTP(this);
    }

    /**
     * Đặt đối tượng sender
     * 
     * @param otpSender đối tượng gửi OTP
     */
    public void setOTPSender(OTPSender otpSender) {
        this.otpSender = otpSender;
    }
}