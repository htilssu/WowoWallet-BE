package com.wowo.wowo.constants;

public class Constant {

    public static final String MINIMUM_TRANSFER_AMOUNT = "MIN_TRANSFER";
    public static final String MAXIMUM_TRANSFER_AMOUNT = "MAX_TRANSFER";
    public static final String MINIMUM_WITHDRAW_AMOUNT = "MIN_WITHDRAW";
    public static final String MAXIMUM_WITHDRAW_AMOUNT = "MAX_WITHDRAW";
    public static final String MINIMUM_TOPUP_AMOUNT = "MIN_TOPUP";
    public static final String MAXIMUM_TOPUP_AMOUNT = "MAX_TOPUP";
    public static final String MINIMUM_REFUND_AMOUNT = "MIN_REFUND";
    public static final String MAXIMUM_REFUND_AMOUNT = "MAX_REFUND";
    public static final String MINIMUM_BALANCE = "MIN_BALANCE";
    public static final String MAXIMUM_BALANCE = "MAX_BALANCE";
    public static final String MINIMUM_TRANSFER_FEE = "MIN_TRANSFER_FEE";
    public static final String MAXIMUM_WAITING_TIME = "MAX_WAITING_TIME";


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
}
