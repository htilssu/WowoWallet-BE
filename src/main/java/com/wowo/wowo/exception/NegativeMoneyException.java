package com.wowo.wowo.exception;

public class NegativeMoneyException extends RuntimeException {

    public NegativeMoneyException(String message) {
        super(message);
    }
}
