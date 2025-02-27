package com.wowo.wowo.otp;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.wowo.wowo.data.dto.OtpSendDTO;
import com.wowo.wowo.exception.NotFoundException;
import com.wowo.wowo.model.OtpClaim;
import com.wowo.wowo.repository.OtpClaimRepository;
import com.wowo.wowo.repository.UserRepository;
import com.wowo.wowo.service.EmailService;
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
    private final UserRepository userRepository;
    private final OtpClaimRepository otpClaimRepository;

    /**
     * Gửi mã OTP cho người dùng, thông tin người nhận sẽ được lấy từ {@link OtpSendDTO#getSendTo()}
     * hàm này sẽ gửi OTP bất đồng bộ
     * {@link OTPSender} là service gửi OTP (ví dụ: gửi qua email, sms) {@link EmailService},
     *
     * @param otpSender  đối tượng gửi OTP
     * @param otpSendDTO thông tin người nhận và loại OTP
     */
    public void send(OTPSender otpSender, OtpSendDTO otpSendDTO, Authentication authentication) {
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

            OtpClaim otpClaim = OtpClaim.builder()
                    .claimant(authentication.getPrincipal().toString())
                    .otp(otp)
                    .createdAt(Instant.now())
                    .expiresAt(Instant.now().plus(10, ChronoUnit.MINUTES))
                    .build();

            otpClaimRepository.save(otpClaim);
        });

    }

    /**
     * Xác thực mã OTP có đúng của người dùng hiện tại hay không
     */
    public boolean verify(String userId, OTPData otpSend) {
        var userClaim = otpClaimRepository.findFirstByClaimantOrderByCreatedAtAsc(userId);
        if (userClaim.isEmpty()) return false;
        final OtpClaim claim = userClaim.get();
        if (claim.isExpired()) {
            otpClaimRepository.deleteByClaimant(userId);
            return false;
        }

        final boolean isMatch = otpSend.getOtp().equals(claim.getOtp());
        if (isMatch) {
            otpClaimRepository.deleteByClaimant(userId);
        }

        return isMatch;
    }

}
