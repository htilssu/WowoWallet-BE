package com.wowo.wowo.controllers;

import com.wowo.wowo.ForgotPassword.PasswordResetToken;
import com.wowo.wowo.data.dto.request.ChangePasswordData;
import com.wowo.wowo.data.dto.response.ResponseMessage;
import com.wowo.wowo.data.dto.response.UserDto;
import com.wowo.wowo.data.mapper.UserMapperImpl;
import com.wowo.wowo.data.mapper.WalletMapperImpl;
import com.wowo.wowo.models.User;
import com.wowo.wowo.models.Wallet;
import com.wowo.wowo.repositories.ResetTokenRepository;
import com.wowo.wowo.repositories.UserRepository;
import com.wowo.wowo.repositories.WalletRepository;
import com.wowo.wowo.services.EmailService;
import com.wowo.wowo.util.ObjectUtil;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@RestController
@AllArgsConstructor
@RequestMapping(value = "/api/v1/user", produces = "application/json; charset=UTF-8")
public class UserController {

    private final UserRepository userRepository;
    private final WalletRepository walletRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final UserMapperImpl userMapperImpl;
    private final WalletMapperImpl walletMapperImpl;
   private final ResetTokenRepository resetTokenRepository;
    private final EmailService emailService;

    @GetMapping()
    public ResponseEntity<UserDto> getUser(Authentication authentication) {
        Optional<User> user = userRepository.findById((String) authentication.getPrincipal());
        if (user.isPresent()) {
            user.get().setPassword(null);
            return ResponseEntity.ok(userMapperImpl.toDto(user.get()));
        }
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

    @PostMapping("/password")
    public ResponseEntity<ResponseMessage> changePassword(@RequestBody ChangePasswordData
                                                                  passwordData,
                                                          Authentication authentication) {
        Optional<User> user = userRepository.findById((String) authentication.getPrincipal());

        if (user.isEmpty()) {
            return ResponseEntity.badRequest()
                    .body(new ResponseMessage("Người dùng không hợp lệ"));
        }

        if (passwordData.getOldPassword().equals(passwordData.getNewPassword())) {
            return ResponseEntity.badRequest()
                    .body(new ResponseMessage("Mật khẩu mới không được trùng với mật khẩu cũ"));
        }

        if (!bCryptPasswordEncoder.matches(passwordData.getOldPassword(),
                user.get().getPassword())) {
            return ResponseEntity.badRequest()
                    .body(new ResponseMessage("Mật khẩu cũ không đúng"));
        }

        user.get().setPassword(bCryptPasswordEncoder.encode(passwordData.getNewPassword()));
        try {
            userRepository.save(user.get());
            return ResponseEntity.ok(new ResponseMessage("Đổi mật khẩu thành công"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    new ResponseMessage("Đổi mật khẩu thất bại"));
        }
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
        User user = userRepository.findByEmail(id);
        if (user != null) {
            return ResponseEntity.ok(ObjectUtil.mergeObjects(ObjectUtil.wrapObject("fullName",
                            user.getLastName() + " " + user.getFirstName()),
                    ObjectUtil.wrapObject("isVerified", user.getIsVerified())));
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<ResponseMessage> forgotPassword(@RequestBody String email) {
        // Tạo token để reset mật khẩu
        String token = UUID.randomUUID().toString();

        // Lưu token vào DynamoDB với userId là khóa chính
        PasswordResetToken resetToken = new PasswordResetToken();
        resetToken.setToken(token);
        resetToken.setExpirationTime(System.currentTimeMillis() / 1000 + 3600); // Token hết hạn sau 1 giờ

        resetTokenRepository.save(resetToken).join(); // Đợi đến khi lưu thành công

        // Gửi email xác nhận sử dụng phương thức sendResetPasswordToken
        emailService.sendResetPassword("toandong2004@gmail.com", token); // Gửi đến email cố định

        return ResponseEntity.ok(new ResponseMessage("Link đặt lại mật khẩu đã được gửi đến email của bạn."));
    }

    @PostMapping("/reset-password")
    public ResponseEntity<ResponseMessage> resetPassword(@RequestParam String token,
                                                         @RequestBody ChangePasswordData passwordData) {
        // Kiểm tra token
        PasswordResetToken resetToken = resetTokenRepository.findTokenByToken(token).join();
        if (resetToken == null) {
            return ResponseEntity.badRequest().body(new ResponseMessage("Token không hợp lệ."));
        }

        // Kiểm tra xem token đã hết hạn chưa
        if (System.currentTimeMillis() / 1000 > resetToken.getExpirationTime()) {
            return ResponseEntity.badRequest().body(new ResponseMessage("Token đã hết hạn."));
        }

        // Kiểm tra nếu mật khẩu mới và mật khẩu xác nhận khớp
        if (passwordData.getNewPassword().equals(passwordData.getConfirmPassword())) {
            User user = userRepository.findById(resetToken.getUserId()).orElse(null);
            if (user != null) {
                user.setPassword(bCryptPasswordEncoder.encode(passwordData.getNewPassword()));
                userRepository.save(user);

                // Xóa token khỏi DynamoDB sau khi sử dụng
                resetTokenRepository.deleteByUserId(resetToken.getUserId()).join();

                return ResponseEntity.ok(new ResponseMessage("Đổi mật khẩu thành công."));
            }
        }

        return ResponseEntity.badRequest().body(new ResponseMessage("Mật khẩu xác nhận không khớp."));
    }
}

