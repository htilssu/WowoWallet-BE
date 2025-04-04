package com.wowo.wowo.data.dto;

import com.wowo.wowo.constant.Constant.OTPType;
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
     * Loại OTP (TRANSACTION_CONFIRMATION, PASSWORD_RESET, v.v)
     * Được chuyển đổi thành OTPType enum khi xử lý
     */
    private String otpType;

    /**
     * ID của người dùng cần nhận OTP
     */
    private String userId;

    /**
     * ID giao dịch (nếu là OTP liên quan đến giao dịch)
     */
    private String transactionId;

    /**
     * Chuyển đổi chuỗi otpType thành đối tượng enum OTPType
     * 
     * @return đối tượng OTPType tương ứng
     */
    public OTPType getOtpType() {
        return OTPType.valueOf(otpType);
    }

    /**
     * Tạo đối tượng TransactionInfo từ transactionId
     * 
     * @return đối tượng TransactionInfo hoặc null nếu không có transactionId
     */
    public TransactionInfo getTransactionInfo() {
        if (transactionId == null || transactionId.isEmpty()) {
            return null;
        }
        TransactionInfo info = new TransactionInfo();
        info.setTransactionId(transactionId);
        return info;
    }
}