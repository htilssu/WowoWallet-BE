package com.wowo.wowo.services;

import com.wowo.wowo.data.dto.response.UserDto;
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

    public User getUserByIdOrUsernameOrEmail(String id) {
        return userRepository.findFirstByIdOrEmailOrUsername(id, id, id).orElseThrow(
                () -> new NotFoundException("Người dùng không tồn tại"));
    }
}
