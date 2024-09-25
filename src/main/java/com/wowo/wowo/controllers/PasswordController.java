package com.wowo.wowo.controllers;

import com.wowo.wowo.data.dto.response.ResponseMessage;
import com.wowo.wowo.services.UserService;
import com.wowo.wowo.util.JwtUtil;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping()
@AllArgsConstructor
public class PasswordController {

    private final UserService userService;


    @PostMapping("/v1/password/reset")
    public ResponseEntity<ResponseMessage> resetPassword(@RequestParam String email) {
        userService.resetPassword(email);
        return ResponseEntity.ok(new ResponseMessage("Một email đã được gửi đến hòm thư của bạn"));
    }

    @GetMapping("/v1/password/new")
    public ResponseEntity<ResponseMessage> resetPassword(@RequestParam String token, @RequestParam String password) {
        userService.setNewPassword(token, password);
        return ResponseEntity.ok(new ResponseMessage("Mật khẩu đã được thay đổi"));
    }

}
