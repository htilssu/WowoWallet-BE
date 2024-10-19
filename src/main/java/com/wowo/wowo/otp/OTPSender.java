package com.wowo.wowo.otp;

import java.util.concurrent.CompletableFuture;

public interface OTPSender {

    void sendOTP(String sendTo, String otp);

    /**
     * Send otp to the given receiver asynchronously and return a future
     * if you want to wait for the completion of the task
     * you can call {@link CompletableFuture#get()} or {@link CompletableFuture#join()} current
     * thread will be blocked
     * util the task is completed, if you want to perform some operation after the task is completed
     * asynchronously you can use {@link CompletableFuture#thenAccept(java.util.function.Consumer)}
     *
     * @param sendTo receiver email or phone number, used to send otp
     * @param otp    one time password, which will be sent to the receiver
     *
     * @return a future that will be completed when the task is completed
     */
    CompletableFuture<Void> sendOTPAsync(String sendTo, String otp);

}
