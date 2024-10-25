package com.wowo.wowo.controllers;

import com.wowo.wowo.data.dto.ResponseMessage;
import com.wowo.wowo.otp.OTPManager;
import com.wowo.wowo.otp.OtpSendDto;
import com.wowo.wowo.otp.OTPVerifyDto;
import com.wowo.wowo.repositories.UserRepository;
import com.wowo.wowo.services.EmailService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping(value = "v1/otp", produces = "application/json; charset=utf-8")
@Tag(name = "OTP", description = "One Time Password")
public class OTPController {

    private final OTPManager otpManager;
    private final EmailService emailService;
    private final UserRepository userRepository;

    @PostMapping
    public ResponseEntity<?> sendOtp(@RequestBody @Nullable OtpSendDto otpSendDto,
            Authentication authentication) {

        if (otpSendDto == null) {
            return ResponseEntity.badRequest()
                    .body(new ResponseMessage("Data is invalid"));
        }


        //        TODO: implement send otp
        return ResponseEntity.ok().build();
    }

    @PostMapping("/verify")
    public ResponseEntity<ResponseMessage> verifyOtp(@RequestBody OTPVerifyDto otpData,
            Authentication authentication) {
        if (authentication != null) {
            String userId = (String) authentication.getPrincipal();
            boolean verifyStatus = otpManager.verify(userId, otpData);

            if (verifyStatus) {
                return ResponseEntity.ok(new ResponseMessage("Mã OTP hợp lệ"));
            }
            else {
                return ResponseEntity.status(401)
                        .body(new ResponseMessage("Mã OTP không hợp lệ"));
            }
        }

        return ResponseEntity.badRequest()
                .body(new ResponseMessage("Người dùng không hợp lệ"));
    }
}
