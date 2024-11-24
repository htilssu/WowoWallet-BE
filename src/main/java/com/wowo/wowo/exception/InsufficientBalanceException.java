package com.wowo.wowo.exception;

public class InsufficientBalanceException extends RuntimeException {

    public InsufficientBalanceException(String message) {
        super(message);
    }

    public InsufficientBalanceException(Throwable cause) {
        super("Not enough money", cause);
    }
}