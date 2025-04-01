package com.wowo.wowo.otp.storage;

import com.wowo.wowo.constant.Constant.OTPType;
import com.wowo.wowo.otp.OTP;

import java.util.Optional;

/**
 * Interface định nghĩa các phương thức để lưu trữ và truy xuất các đối tượng
 * OTP.
 * Có thể được triển khai bằng nhiều cách khác nhau: in-memory, database, redis,
 * etc.
 */
public interface OTPRepository {

    /**
     * Lưu OTP vào repository
     * 
     * @param otp đối tượng OTP cần lưu
     */
    void save(OTP otp);

    /**
     * Tìm OTP theo userId và loại OTP
     * 
     * @param userId  ID của người dùng
     * @param otpType loại OTP cần tìm
     * @return Optional chứa OTP nếu tìm thấy, Optional.empty() nếu không tìm thấy
     */
    Optional<OTP> findByUserIdAndType(String userId, OTPType otpType);

    /**
     * Tìm OTP theo userId, recipient và loại OTP
     * 
     * @param userId    ID của người dùng
     * @param recipient địa chỉ người nhận OTP (email, số điện thoại)
     * @param otpType   loại OTP cần tìm
     * @return Optional chứa OTP nếu tìm thấy, Optional.empty() nếu không tìm thấy
     */
    Optional<OTP> findByUserIdAndRecipientAndType(String userId, String recipient, OTPType otpType);

    /**
     * Xóa OTP theo userId và loại OTP
     * 
     * @param userId  ID của người dùng
     * @param otpType loại OTP cần xóa
     */
    void deleteByUserIdAndType(String userId, OTPType otpType);

    /**
     * Xóa tất cả OTP hết hạn
     */
    void deleteExpiredOTPs();
}