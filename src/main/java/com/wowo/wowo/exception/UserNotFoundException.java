package com.wowo.wowo.exception;

public class UserNotFoundException extends RuntimeException {

    final String error = "USER_NOT_FOUND";

    public UserNotFoundException(String message) {
        super(message);
    }

    public UserNotFoundException() {
        super("User not found");
    }
}
