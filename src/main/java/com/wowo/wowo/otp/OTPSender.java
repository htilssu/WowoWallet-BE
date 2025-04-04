package com.wowo.wowo.otp;

/**
 * Interface định nghĩa phương thức gửi OTP.
 * Có thể được triển khai bằng nhiều cách khác nhau: email, SMS, app
 * notification, etc.
 */
public interface OTPSender {

    /**
     * Gửi nội dung với tiêu đề xác định đến địa chỉ người nhận.
     * Phương thức này tổng quát hóa việc gửi nội dung, không chỉ giới hạn ở OTP.
     *
     * @param recipientAddress địa chỉ người nhận (email, số điện thoại, etc.)
     * @param subject          tiêu đề của nội dung gửi đi
     * @param content          nội dung cần gửi, đã được xử lý và định dạng trước
     * @return true nếu gửi thành công, false nếu thất bại
     */
    boolean send(String recipientAddress, String subject, String content);

    /**
     * Gửi OTP trực tiếp dựa trên đối tượng OTP.
     * Phương thức này sẽ tự động tạo nội dung phù hợp dựa trên loại OTP.
     *
     * @param otp Đối tượng OTP cần gửi, chứa tất cả thông tin cần thiết
     * @return true nếu gửi thành công, false nếu thất bại
     */
    boolean sendOTP(OTP otp);
}
