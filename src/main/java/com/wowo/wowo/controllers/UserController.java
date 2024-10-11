package com.wowo.wowo.controllers;

import com.wowo.wowo.data.dto.response.UserDto;
import com.wowo.wowo.data.mapper.UserMapper;
import com.wowo.wowo.data.mapper.WalletMapper;
import com.wowo.wowo.models.User;
import com.wowo.wowo.models.Wallet;
import com.wowo.wowo.repositories.UserRepository;
import com.wowo.wowo.repositories.WalletRepository;
import com.wowo.wowo.services.EmailService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@AllArgsConstructor
@RequestMapping(value = "/v1/user", produces = "application/json; charset=UTF-8")
public class UserController {

    private final UserRepository userRepository;
    private final WalletRepository walletRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final UserMapper userMapperImpl;
    private final WalletMapper walletMapperImpl;
    private final EmailService emailService;

    @GetMapping()
    public ResponseEntity<UserDto> getUser(Authentication authentication) {
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/wallet")
    public ResponseEntity<?> getWallet(Authentication authentication) {
        Optional<Wallet> wallet = walletRepository.findByOwnerIdAndOwnerType(
                (String) authentication.getPrincipal(), "user");
        if (wallet.isPresent()) {
            return ResponseEntity.ok(walletMapperImpl.toResponse(wallet.get()));
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/{id}")
    public UserDto getUserById(@PathVariable String id) {
        if (id != null) {
            final Optional<User> byId = userRepository.findById(id);
            if (byId.isPresent()) {
                return userMapperImpl.toDto(byId.get());
            }
        }

        return null;
    }

    @GetMapping("/check/{id}")
    public ResponseEntity<?> checkUser(@PathVariable String id) {
        return ResponseEntity.notFound().build();
    }
}
