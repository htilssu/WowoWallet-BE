package com.wowo.wowo.otp;

import com.wowo.wowo.constant.Constant.OTPType;
import com.wowo.wowo.data.MailContent;

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

    @Override
    protected String generateSubject() {
        return switch (otpType) {
            case WITHDRAW_CONFIRMATION -> "Xác nhận rút tiền - Mã xác thực OTP";
            case TRANSACTION_CONFIRMATION -> "Xác nhận giao dịch - Mã xác thực OTP";
            default -> "Mã xác thực OTP giao dịch";
        };
    }

    @Override
    protected String generateContent() {
        String messageByType = switch (otpType) {
            case WITHDRAW_CONFIRMATION -> "để xác nhận yêu cầu rút tiền";
            case TRANSACTION_CONFIRMATION -> "để xác nhận giao dịch";
            default -> "để xác nhận thao tác tài chính";
        };

        return MailContent.TRANSACTION_OTP_BODY
                .replace("{{OTP}}", code)
                .replace("{{TRANSACTION_ID}}", transactionId)
                .replace("{{MESSAGE}}", messageByType);
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