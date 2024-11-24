package com.wowo.wowo.exception;

public class TransactionFailedException extends RuntimeException {

    public TransactionFailedException(String message) {
        super(message);
    }
}
