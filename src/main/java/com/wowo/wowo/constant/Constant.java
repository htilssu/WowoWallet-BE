package com.wowo.wowo.constant;

public class Constant {

    public static final String MINIMUM_TRANSFER_AMOUNT = "MIN_TRANSFER";
    public static final String MAXIMUM_TRANSFER_AMOUNT = "MAX_TRANSFER";
    public static final String MINIMUM_WITHDRAW_AMOUNT = "MIN_WITHDRAW";
    public static final String MAXIMUM_WITHDRAW_AMOUNT = "MAX_WITHDRAW";
    public static final String MINIMUM_TOP_UP_AMOUNT = "MIN_TOP_UP";
    public static final String MAXIMUM_TOP_UP_AMOUNT = "MAX_TOP_UP";
    public static final String MINIMUM_REFUND_AMOUNT = "MIN_REFUND";
    public static final String MAXIMUM_REFUND_AMOUNT = "MAX_REFUND";
    public static final String MINIMUM_WITHDRAW_APPLICATION = "MIN_WITHDRAW_APPLICATION";
    public static final String MINIMUM_BALANCE = "MIN_BALANCE";
    public static final String MAXIMUM_BALANCE = "MAX_BALANCE";
    public static final String MINIMUM_TRANSFER_FEE = "MIN_TRANSFER_FEE";
    public static final String MAXIMUM_WAITING_TIME = "MAX_WAITING_TIME";
    public static final String ORDER_TIMEOUT = "ORDER_TIMEOUT";

    public enum PaymentService {
        PAYPAL,
        STRIPE,
        VNPAY,
        WALLET
    }

    public enum OTPService {
        SMS,
        EMAIL
    }

    public enum OTPType {
        FINANCIAL_OPERATION(10), // OTP cho các thao tác tài chính (10 phút)
        TRANSACTION_CONFIRMATION(10), // OTP xác nhận giao dịch (10 phút)
        WITHDRAW_CONFIRMATION(10), // OTP xác nhận rút tiền (10 phút)
        PASSWORD_RESET(30), // OTP đặt lại mật khẩu (30 phút)
        EMAIL_VERIFICATION(60), // OTP xác minh email (60 phút)
        ACCOUNT_VERIFICATION(15); // OTP xác minh tài khoản (15 phút)

        private final int expirationTimeInMinutes;

        OTPType(int expirationTimeInMinutes) {
            this.expirationTimeInMinutes = expirationTimeInMinutes;
        }

        public int getExpirationTimeInMinutes() {
            return expirationTimeInMinutes;
        }
    }
}
