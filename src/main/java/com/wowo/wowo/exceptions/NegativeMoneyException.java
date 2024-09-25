package com.wowo.wowo.exceptions;

public class NegativeMoneyException extends RuntimeException {

    public NegativeMoneyException(String message) {
        super(message);
    }
}
