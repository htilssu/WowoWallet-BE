package com.wowo.wowo.otp;

import com.wowo.wowo.services.EmailService;
import com.wowo.wowo.services.otp.OTPGenerator;
import com.wowo.wowo.util.OTPUtil;
import com.wowo.wowo.util.DateTimeUtil;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@AllArgsConstructor
@Service
public class OTPManager {

    final
    OTPGenerator otpGenerator;
    final
    ClaimOTPRepository claimOTPRepository;

    /**
     * Gửi mã OTP cho người dùng, thông tin người nhận sẽ được lấy từ {@link OTPSend#getSendTo()}
     * hàm này sẽ gửi OTP bất đồng bộ
     * {@link OTPSender} là service gửi OTP (ví dụ: gửi qua email, sms) {@link EmailService},
     *
     * @param otpSender      đối tượng gửi OTP
     * @param otpSend        thông tin người nhận và loại OTP
     */
    public void send(OTPSender otpSender, OTPSend otpSend, Authentication authentication) {

        otpGenerator.generateOTP().thenAccept(otp -> {
            otpSender.sendOTP(otpSend.getSendTo(), otp);
            ClaimOTPModel claim = new ClaimOTPModel(otp, authentication.getPrincipal().toString(),
                    DateTimeUtil.convertToString(Instant.now()
                            .plus(OTPUtil.getExpiryTime(),
                                    ChronoUnit.SECONDS
                            ))
            );

            claimOTPRepository.save(claim).join(); //save
        });

    }

    /**
     * Xác thực mã OTP có đúng của người dùng hiện tại hay không
     *
     */
    public boolean verify(String userId, OTPData otpSend) {
        var userClaim = claimOTPRepository.findOtpByUserId(userId).join();
        if (userClaim == null) return false;
        if (userClaim.isExpired()) return false;
        final boolean isMatch = otpSend.getOtp().equals(userClaim.getOtp());
        if (isMatch) {
            claimOTPRepository.deleteByUserId(userId).join();
        }

        return isMatch;
    }

}
