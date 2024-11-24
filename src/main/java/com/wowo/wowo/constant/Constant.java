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
}
