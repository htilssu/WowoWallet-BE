package com.ewallet.ewallet.controllers;

import com.ewallet.ewallet.atm.AuthData;
import com.ewallet.ewallet.dto.mapper.UserMapperImpl;
import com.ewallet.ewallet.data.dto.request.UserDto;
import com.ewallet.ewallet.data.dto.response.ResponseMessage;
import com.ewallet.ewallet.models.User;
import com.ewallet.ewallet.repositories.UserRepository;
import com.ewallet.ewallet.util.JwtUtil;
import com.ewallet.ewallet.util.ObjectUtil;
import com.ewallet.ewallet.validator.EmailValidator;
import com.ewallet.ewallet.validator.UserValidator;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping(value = "/api/v1/auth", produces = "application/json; charset=UTF-8")
public class AuthController {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final UserMapperImpl userMapperImpl;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthData authData) {

        if (authData.getUsername() == null || authData.getPassword() == null) {
            return ResponseEntity.status(401)
                    .body(new ResponseMessage(
                            "Đăng nhập thất bại, username hoặc password không được để trống!"));
        }

        User user;

        if (!EmailValidator.isValid(authData.getUsername())) {
            user = userRepository.findByUserName(authData.getUsername());
        }
        else {
            user = userRepository.findByEmail(authData.getUsername());
        }


        if (user == null) {
            return ResponseEntity.status(401)
                    .body(new ResponseMessage("Tài khoản không tồn tại trong hệ thống"));
        }

        if (bCryptPasswordEncoder.matches(authData.getPassword(), user.getPassword())) {
            String token = JwtUtil.generateToken(user);
            return ResponseEntity.ok()
                    .header("Set-Cookie",
                            "token=" + token + "; Path=/; SameSite=None; Secure; Max-Age=99999;")
                    .body(ObjectUtil.mergeObjects(
                            ObjectUtil.wrapObject("user", userMapperImpl.toDto(user)),
                            new ResponseMessage("Đăng nhập thành công"),
                            ObjectUtil.wrapObject("token", token)));
        }
        else {
            return ResponseEntity.status(401)
                    .body(new ResponseMessage("Mật khẩu không đúng"));
        }
    }

    @PostMapping("/register")
    public ResponseEntity<ResponseMessage> register(@RequestBody UserDto user,
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

        if (user.getUsername() != null){
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
