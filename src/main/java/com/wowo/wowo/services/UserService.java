package com.wowo.wowo.services;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.wowo.wowo.exceptions.NotFoundException;
import com.wowo.wowo.models.User;
import com.wowo.wowo.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final EmailService emailService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public void resetPassword(String email) {
        final User user = userRepository.findByEmail(email);
        if (user == null) throw new NotFoundException("User not exist");

        final String token = JwtService.generateToken(user);

        emailService.sendResetPassword(token, email);
    }

    public void setNewPassword(String token, String newPassword) {
        DecodedJWT decodedJWT = JwtService.verifyToken(token);
        if (decodedJWT == null) throw new NotFoundException("Invalid token");

        String userId = decodedJWT.getSubject();
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User not found"));

        user.setPassword(bCryptPasswordEncoder.encode(newPassword));
        userRepository.save(user);
    }

}
