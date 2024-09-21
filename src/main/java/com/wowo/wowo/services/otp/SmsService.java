package com.wowo.wowo.services.otp;

import com.wowo.wowo.otp.OTPSender;
import com.twilio.Twilio;
import com.twilio.rest.verify.v2.service.Verification;
import jakarta.annotation.PostConstruct;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
@ConfigurationProperties(prefix = "twilio.config")
@Setter
public class SmsService implements OTPSender {

    private String ACCOUNT_SID;
    private String AUTH_TOKEN;
    private String SERVICE_ID;

    @PostConstruct
    public void init() {
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
        Twilio.setRegion("us1");
        Twilio.setEdge("singapore");
    }

    @Override
    public void sendOTP(String sendTo, String otp) {
        try {
            Verification verification = Verification.creator(SERVICE_ID, sendTo, "sms")
                    .create();
            Logger.getAnonymousLogger().log(Level.INFO, verification.getSid());

        } catch (Exception e) {
            Logger.getAnonymousLogger().log(Level.WARNING, e.getMessage());
        }
    }

    @Override
    @Async
    public CompletableFuture<Void> sendOTPAsync(String sendTo, String otp) {
        try {
            Verification verification = Verification.creator(SERVICE_ID, sendTo, "sms")
                    .create();
            Logger.getAnonymousLogger().log(Level.INFO, verification.getSid());
        } catch (Exception e) {
            Logger.getAnonymousLogger().log(Level.WARNING, e.getMessage());
        }
        return CompletableFuture.completedFuture(null);

    }
}
