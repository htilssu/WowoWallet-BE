package com.wowo.wowo.otp;

import com.wowo.wowo.constant.Constant.OTPType;

/**
 * OTP dùng cho giao dịch, mở rộng từ OTP cơ bản với thông tin giao dịch
 */
public class TransactionOTP extends OTP {

    private String transactionId;

    public TransactionOTP(String userId, String recipient, String transactionId) {
        this.userId = userId;
        this.recipient = recipient;
        this.transactionId = transactionId;
        this.otpType = OTPType.TRANSACTION_CONFIRMATION;
        setExpirationTime();
    }

    public String getTransactionId() {
        return transactionId;
    }

    /**
     * Phương thức này mở rộng xác minh thông thường, thêm kiểm tra ID giao dịch
     * 
     * @param inputCode     mã OTP nhập vào
     * @param transactionId ID giao dịch cần kiểm tra
     * @param otpGenerator  generator OTP để kiểm tra tính hợp lệ
     * @return true nếu mã OTP đúng, chưa hết hạn, và ID giao dịch khớp
     */
    public boolean verifyForTransaction(String inputCode, String transactionId, OTPGenerator otpGenerator) {
        return verify(inputCode, otpGenerator) && this.transactionId.equals(transactionId);
    }
}