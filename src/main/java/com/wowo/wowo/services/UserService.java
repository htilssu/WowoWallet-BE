package com.wowo.wowo.services;

import com.wowo.wowo.data.dto.SSOData;
import com.wowo.wowo.data.dto.UserDto;
import com.wowo.wowo.data.mapper.UserMapper;
import com.wowo.wowo.exceptions.NotFoundException;
import com.wowo.wowo.models.User;
import com.wowo.wowo.repositories.UserRepository;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final WalletService walletService;
    private final UserMapper userMapper;

    public UserDto getUserById(String id) {
        return userMapper.toDto(userRepository.findById(id).orElseThrow(
                () -> new NotFoundException("Người dùng không tồn tại")));
    }

    @NotNull
    public User getUserByIdOrUsernameOrEmail(String id, String username, String email) {
        return userRepository.findFirstByIdOrEmailOrUsername(id, username, email).orElseThrow(
                () -> new NotFoundException("Người dùng không tồn tại"));
    }

    public void createUser(SSOData ssoData) {

        var user = userRepository.findById(ssoData.getId());
        if (user.isPresent()) return;

        var newUser = new User();
        newUser.setId(ssoData.getId());
        newUser.setEmail(ssoData.getEmail());
        newUser.setUsername(ssoData.getUsername());
        newUser.setFirstName(ssoData.getFirstName());
        newUser.setLastName(ssoData.getLastName());

        try {
            userRepository.save(newUser);
            walletService.createWallet(newUser);

        } catch (Exception e) {
            throw new RuntimeException("Không thể tạo người dùng");
        }
    }
}
