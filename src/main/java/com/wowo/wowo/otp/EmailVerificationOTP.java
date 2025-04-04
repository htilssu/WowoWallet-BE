package com.wowo.wowo.otp;

import com.wowo.wowo.constant.Constant.OTPType;
import com.wowo.wowo.service.UserService;
import lombok.Getter;
import lombok.Setter;

/**
 * OTP dùng cho xác minh email
 * Có thời gian hết hạn dài nhất (60 phút), vì người dùng có thể không kiểm tra
 * email ngay lập tức
 */
@Setter
@Getter
public class EmailVerificationOTP extends OTP {

    /**
     * -- SETTER --
     *  Đặt UserService để cập nhật trạng thái người dùng
     *
     * @param userService Service để cập nhật thông tin người dùng
     */
    private UserService userService;

    public EmailVerificationOTP(String userId, String recipient) {
        this.userId = userId;
        this.recipient = recipient;
        this.otpType = OTPType.EMAIL_VERIFICATION;
        setExpirationTime();
    }

    /**
     * Constructor với UserService để có thể cập nhật trạng thái người dùng
     * 
     * @param userId      ID của người dùng
     * @param recipient   Địa chỉ email người nhận
     * @param userService Service để cập nhật thông tin người dùng
     */
    public EmailVerificationOTP(String userId, String recipient, UserService userService) {
        this(userId, recipient);
        this.userService = userService;
    }

    /**
     * Ghi đè phương thức xác minh để cập nhật trạng thái xác thực email
     * của người dùng khi xác thực OTP thành công
     * 
     * @param inputCode    mã OTP được nhập vào
     * @param otpGenerator generator OTP để kiểm tra tính hợp lệ
     * @return true nếu mã đúng và chưa hết hạn
     */
    @Override
    public boolean verify(String inputCode, OTPGenerator otpGenerator) {
        // Gọi phương thức verify của lớp cha
        boolean isValid = super.verify(inputCode, otpGenerator);

        // Cập nhật trạng thái xác thực email nếu OTP hợp lệ
        if (isValid && userService != null) {
            try {
                userService.verifyUserEmail(userId);
            } catch (Exception e) {
                // Log lỗi nhưng vẫn trả về kết quả xác thực
                System.err.println("Không thể cập nhật trạng thái xác thực email: " + e.getMessage());
            }
        }

        return isValid;
    }
}