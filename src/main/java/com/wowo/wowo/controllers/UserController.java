package com.ewallet.ewallet.user;

import com.ewallet.ewallet.ForgotPassword.PasswordResetToken;
import com.ewallet.ewallet.dto.mapper.UserMapperImpl;
import com.ewallet.ewallet.dto.mapper.WalletMapperImpl;
import com.ewallet.ewallet.dto.response.ResponseMessage;
import com.ewallet.ewallet.dto.response.UserDto;
import com.ewallet.ewallet.models.User;
import com.ewallet.ewallet.models.Wallet;
import com.ewallet.ewallet.repository.PasswordResetTokenRepository;
import com.ewallet.ewallet.service.EmailService;
import com.ewallet.ewallet.util.ObjectUtil;
import com.ewallet.ewallet.wallet.WalletRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
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
    private final PasswordResetTokenRepository passwordResetTokenRepository;
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
    public UserDto getUserById(@PathVariable String id){
        if (id != null){
            final Optional<User> byId =   userRepository.findById(id);
            if (byId.isPresent()){
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
        User user = userRepository.findByEmail(email);
        if (user == null) {
            return ResponseEntity.badRequest().body(new ResponseMessage("Email không tồn tại trong hệ thống."));
        }

        // Tạo token để reset mật khẩu
        String token = UUID.randomUUID().toString();

        // Lưu token vào DynamoDB với userId là khóa chính
        PasswordResetToken resetToken = new PasswordResetToken();
        resetToken.setUserId(user.getId().toString());
        resetToken.setToken(token);
        resetToken.setExpirationTime(System.currentTimeMillis() / 1000 + 3600); // Token hết hạn sau 1 giờ
        passwordResetTokenRepository.saveToken(resetToken);

        // Gửi email xác nhận sử dụng phương thức sendResetPasswordToken
        emailService.sendResetPasswordToken(user.getEmail(), token);

        return ResponseEntity.ok(new ResponseMessage("Link đặt lại mật khẩu đã được gửi đến email của bạn."));
    }


    @PostMapping("/reset-password")
    public ResponseEntity<ResponseMessage> resetPassword(@RequestParam String token, @RequestBody ChangePasswordData passwordData) {
        // Tìm token trong DynamoDB
        PasswordResetToken resetToken = passwordResetTokenRepository.findByUserId(token);

        if (resetToken == null) {
            return ResponseEntity.badRequest().body(new ResponseMessage("Token không hợp lệ."));
        }

        // Kiểm tra nếu mật khẩu mới và mật khẩu xác nhận khớp
        if (passwordData.getNewPassword().equals(passwordData.getConfirmPassword())) {
            Optional<User> user = userRepository.findById(resetToken.getUserId());
            if (user.isPresent()) {
                user.get().setPassword(bCryptPasswordEncoder.encode(passwordData.getNewPassword()));
                userRepository.save(user.get());

                // Xóa token khỏi DynamoDB sau khi sử dụng
                passwordResetTokenRepository.deleteToken(resetToken.getUserId());

                return ResponseEntity.ok(new ResponseMessage("Đổi mật khẩu thành công."));
            }
        }

        return ResponseEntity.badRequest().body(new ResponseMessage("Mật khẩu xác nhận không khớp."));
    }
    /*@GetMapping
    public ResponseEntity<?> getAllUser(){

    }*/
}
