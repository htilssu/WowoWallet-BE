package com.wowo.wowo.services;

import com.wowo.wowo.data.dto.SSOData;
import com.wowo.wowo.data.dto.UserDto;
import com.wowo.wowo.exceptions.NotFoundException;
import com.wowo.wowo.models.User;
import com.wowo.wowo.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.NotImplementedException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final EmailService emailService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public ResponseEntity<UserDto> getUserById(String id) {
        throw new NotImplementedException();
    }

    public User getUserByIdOrUsernameOrEmail(String id, String username, String email) {
        return userRepository.findFirstByIdOrEmailOrUsername(id, username, email).orElseThrow(
                () -> new NotFoundException("Người dùng không tồn tại"));
    }

    public void createUser(SSOData ssoData) {
        var user = User.builder().email(ssoData.getEmail()).id(ssoData.getId())
                .username(ssoData.getUsername()).totalMoney(0L).build();

        try {
            userRepository.save(user);
        } catch (Exception e) {
            throw new RuntimeException("Không thể tạo người dùng");
        }
    }
}
