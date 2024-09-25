package com.wowo.wowo.controllers;

import com.wowo.wowo.data.dto.request.AuthData;
import com.wowo.wowo.data.dto.request.CreateUserDto;
import com.wowo.wowo.data.dto.response.ResponseMessage;
import com.wowo.wowo.data.mapper.UserMapperImpl;
import com.wowo.wowo.models.User;
import com.wowo.wowo.repositories.UserRepository;
import com.wowo.wowo.services.AuthService;
import com.wowo.wowo.validator.UserValidator;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping(value = "v1/auth", produces = "application/json; charset=UTF-8")
@Tag(name = "Auth", description = "API for authentication")
@PreAuthorize("permitAll()")
public class AuthController {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final UserMapperImpl userMapperImpl;
    private final AuthService authService;

    @PostMapping("/sign-in")
    public ResponseEntity<?> login(@RequestBody AuthData authData) {
        if (authData.getUsername().isBlank() || authData.getPassword().isBlank()) {
            return ResponseEntity.badRequest()
                    .body(new ResponseMessage("Vui lòng kiểm tra lại thông tin"));
        }

        return authService.login(authData);
    }

    @PostMapping("/sign-up")
    public ResponseEntity<ResponseMessage> register(@RequestBody CreateUserDto user,
            Authentication authentication) {


        User userEntity = userMapperImpl.toEntity(user);

        if (!UserValidator.isValidateUser(userEntity)) {
            return ResponseEntity.badRequest()
                    .body(new ResponseMessage("Vui lòng kiểm tra lại thông tin"));
        }

        User existingUser = userRepository.findByEmail(user.getEmail());

        if (existingUser != null) {
            return ResponseEntity.ok(new ResponseMessage("Người dùng đã tồn tại"));
        }
        var userCheck = userRepository.findByPhoneNumber(user.getPhoneNumber());
        if (userCheck != null) {
            return ResponseEntity.ok(new ResponseMessage("Số điện thoại đã tồn tại"));

        }

        if (user.getUsername() != null) {
            userCheck = userRepository.findByUserName(user.getUsername());
            if (userCheck != null) {
                return ResponseEntity.ok(new ResponseMessage("Tên đăng nhập đã tồn tại"));
            }
        }

        userEntity.setPassword(bCryptPasswordEncoder.encode(userEntity.getPassword()));
        try {
            userRepository.save(userEntity);
            return ResponseEntity.ok(new ResponseMessage("Đăng ký thành công"));
        } catch (Exception e) {
            //TODO: catch number phone
            return ResponseEntity.ok(new ResponseMessage(e.getMessage()));
        }
    }

    @GetMapping("/logout")
    public ResponseEntity<String> logout() {
        return ResponseEntity.ok("Đăng xuất thành công!");
    }
}
