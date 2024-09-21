package com.wowo.wowo.controllers;

import com.wowo.wowo.data.dto.response.ResponseMessage;
import com.wowo.wowo.models.User;
import com.wowo.wowo.otp.OTPManager;
import com.wowo.wowo.otp.OTPSend;
import com.wowo.wowo.otp.OTPVerify;
import com.wowo.wowo.services.EmailService;
import com.wowo.wowo.util.OTPUtil;
import com.wowo.wowo.services.otp.SmsService;
import com.wowo.wowo.repositories.UserRepository;
import com.wowo.wowo.util.ObjectUtil;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@AllArgsConstructor
@RequestMapping(value = "v1/otp", produces = "application/json; charset=utf-8")
public class OTPController {

    private final OTPManager otpManager;
    private final EmailService emailService;
    private final UserRepository userRepository;
    private final SmsService smsService;

    @PostMapping
    public ResponseEntity<?> sendOtp(@RequestBody @Nullable OTPSend otpSend,
            Authentication authentication) {

        if (otpSend == null) {
            return ResponseEntity.badRequest()
                    .body(new ResponseMessage("Data is invalid"));
        }

        switch (otpSend.getOtpType()) {
            case "email":
                Optional<User> user = userRepository.findById((String) authentication.getPrincipal());

                if (user.isPresent()) {
                    otpSend.setSendTo(user.get().getEmail());
                    otpManager.send(emailService, otpSend, authentication);
                    return ResponseEntity.ok(ObjectUtil.mergeObjects(
                            new ResponseMessage("OTP đã được gửi đến email của bạn!"),
                            ObjectUtil.wrapObject("email", user.get().getEmail()),
                            ObjectUtil.wrapObject("expire", OTPUtil.getExpiryTime())));
                } else {
                    return ResponseEntity.badRequest()
                            .body(new ResponseMessage("Không tìm thấy người dùng"));
                }

            case "phone":
                otpManager.send(smsService, otpSend, authentication);
                return ResponseEntity.ok(new ResponseMessage("OTP đã được gửi đến số điện thoại của bạn!"));

            default:
                return ResponseEntity.badRequest()
                        .body(new ResponseMessage("OTP type is invalid"));
        }
    }

    @PostMapping("/verify")
    public ResponseEntity<ResponseMessage> verifyOtp(@RequestBody OTPVerify otpData,
            Authentication authentication) {
        if (authentication != null) {
            String userId = (String) authentication.getPrincipal();
            boolean verifyStatus = otpManager.verify(userId, otpData);

            if (verifyStatus) {
                return ResponseEntity.ok(new ResponseMessage("Mã OTP hợp lệ"));
            } else {
                return ResponseEntity.status(401)
                        .body(new ResponseMessage("Mã OTP không hợp lệ"));
            }
        }

        return ResponseEntity.badRequest()
                .body(new ResponseMessage("Người dùng không hợp lệ"));
    }
}
