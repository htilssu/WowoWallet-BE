package com.wowo.wowo.otp;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.wowo.wowo.exceptions.NotFoundException;
import com.wowo.wowo.repositories.UserRepository;
import com.wowo.wowo.services.EmailService;
import com.wowo.wowo.util.DateTimeUtil;
import com.wowo.wowo.util.OTPUtil;
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
    private final UserRepository userRepository;

    /**
     * Gửi mã OTP cho người dùng, thông tin người nhận sẽ được lấy từ {@link OtpSendDto#getSendTo()}
     * hàm này sẽ gửi OTP bất đồng bộ
     * {@link OTPSender} là service gửi OTP (ví dụ: gửi qua email, sms) {@link EmailService},
     *
     * @param otpSender  đối tượng gửi OTP
     * @param otpSendDto thông tin người nhận và loại OTP
     */
    public void send(OTPSender otpSender, OtpSendDto otpSendDto, Authentication authentication) {
        DecodedJWT decodedJWT = (DecodedJWT) authentication.getDetails();
        String role = decodedJWT.getClaim("role").asString();
        String senderMail;
        switch (role) {
            case "user" -> {
                senderMail = userRepository.findById(authentication.getPrincipal().toString())
                        .orElseThrow(() -> new NotFoundException("User not found")).getEmail();
            }
            case "partner" -> {
                senderMail = userRepository.findById(authentication.getPrincipal().toString())
                        .orElseThrow(() -> new NotFoundException("Partner not found")).getEmail();
            }
            case null, default -> throw new NotFoundException("Role not found");
        }

        otpGenerator.generateOTP().thenAccept(otp -> {
            otpSender.sendOTP(senderMail, otp);
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
     */
    public boolean verify(String userId, OTPData otpSend) {
        var userClaim = claimOTPRepository.findOtpByUserId(userId).join();
        if (userClaim == null) return false;
        if (userClaim.isExpired()) {
            claimOTPRepository.deleteByUserId(userId).join();
            return false;
        }

        final boolean isMatch = otpSend.getOtp().equals(userClaim.getOtp());
        if (isMatch) {
            claimOTPRepository.deleteByUserId(userId).join();
        }

        return isMatch;
    }

}
