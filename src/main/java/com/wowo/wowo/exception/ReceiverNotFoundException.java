package com.wowo.wowo.exception;

/**
 * An exception that is thrown when a receiver is not found.
 *
 * @author Trung Hieu Tran
 */
public class ReceiverNotFoundException extends RuntimeException {
    public ReceiverNotFoundException(String message) {
        super(message);
    }

    public ReceiverNotFoundException( Throwable cause) {
        super("User not exist", cause);
    }
}