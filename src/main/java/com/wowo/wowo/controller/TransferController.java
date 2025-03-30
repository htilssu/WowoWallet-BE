package com.wowo.wowo.controller;

import com.wowo.wowo.annotation.authorized.IsUser;
import com.wowo.wowo.data.dto.OTPVerifyDTO;
import com.wowo.wowo.data.dto.OTPSendDTO;
import com.wowo.wowo.data.dto.ResponseMessage;
import com.wowo.wowo.data.dto.UserDTO;
import com.wowo.wowo.data.mapper.UserMapper;
import com.wowo.wowo.exception.BadRequest;
import com.wowo.wowo.otp.OTPManager;
import com.wowo.wowo.service.EmailService;
import com.wowo.wowo.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping(value = "v1/transfer", produces = "application/json; charset=UTF-8")
@Tag(name = "Transfer", description = "Chuyển tiền")
public class TransferController {

    private final OTPManager otpManager;
    private final UserService userService;
    private final EmailService emailService;
    private final UserMapper userMapper;

    @GetMapping("/check/{id}")
    public UserDTO checkUser(@PathVariable String id) {
        return userMapper.toDto(
                userService.getUserByIdOrUsernameOrEmail(id, id, id));
    }

    @PostMapping("/send-otp")
    @IsUser
    public ResponseEntity<?> sendOtp(@RequestBody @Validated OTPSendDTO data,
            Authentication authentication) {
        otpManager.send(emailService, data, authentication);
        return ResponseEntity.ok(new ResponseMessage("Gửi mã otp thành công"));
    }

    @PostMapping("/verify-otp")
    @IsUser
    public ResponseEntity<?> verifyOtp(@RequestBody @Validated OTPVerifyDTO data,
            Authentication authentication) {
        final boolean verified = otpManager.verify(((String) authentication.getPrincipal()), data);
        if (!verified) {
            throw new BadRequest("OTP không hợp lệ");
        }
        return ResponseEntity.ok(new ResponseMessage("Xác thực thành công"));
    }
}
