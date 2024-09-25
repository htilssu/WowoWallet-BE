package com.wowo.wowo.exceptions;

public class TransactionNotFoundException extends RuntimeException {

    public TransactionNotFoundException(String message) {
        super(message);
    }
}
