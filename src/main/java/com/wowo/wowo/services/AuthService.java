package com.wowo.wowo.services;

import com.wowo.wowo.data.dto.request.AuthData;
import com.wowo.wowo.data.dto.response.ResponseMessage;
import com.wowo.wowo.data.mapper.UserMapper;
import com.wowo.wowo.exceptions.UserNotFoundException;
import com.wowo.wowo.models.User;
import com.wowo.wowo.repositories.UserRepository;
import com.wowo.wowo.util.ObjectUtil;
import com.wowo.wowo.validator.EmailValidator;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final UserMapper userMapperImpl;

    public ResponseEntity<?> login(AuthData user) {
        User loginUser;
        if (EmailValidator.isValid(user.getUsername())) {
            loginUser = userRepository.findByEmail(user.getUsername());
        }
        else {
            loginUser = userRepository.findByUserName(user.getUsername());
        }

        if (loginUser == null) {
            throw new UserNotFoundException();
        }

        final boolean matches = bCryptPasswordEncoder.matches(user.getPassword(),
                loginUser.getPassword());

        if (!matches) {
            return ResponseEntity.status(401)
                    .body(new ResponseMessage("Mật khẩu không đúng"));
        }

        String token = JwtService.generateToken(loginUser);
        var userDto = userMapperImpl.toDto(loginUser);
        return ResponseEntity.ok()
                .header("Set-Cookie",
                        "token=" + token + "; Path=/; SameSite=None; Secure; Max-Age=99999;")
                .body(ObjectUtil.mergeObjects(
                        ObjectUtil.wrapObject("user", userDto),
                        ObjectUtil.wrapObject("token", token)));
    }
}
