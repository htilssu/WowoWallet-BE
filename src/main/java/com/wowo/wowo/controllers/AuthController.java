package com.wowo.wowo.controllers;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.wowo.wowo.annotations.authorized.IsUser;
import com.wowo.wowo.data.dto.SSOData;
import com.wowo.wowo.services.PartnerService;
import com.wowo.wowo.services.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("v1/auth")
@Tag(name = "Auth", description = "Xác thực")
public class AuthController {

    private final UserService userService;
    private final PartnerService partnerService;

    @RequestMapping("/sso")
    @IsUser
    public ResponseEntity<?> handleCallback(Authentication authentication) {
        DecodedJWT decodedJWT = (DecodedJWT) authentication.getDetails();
        String email = decodedJWT.getClaim("email").asString();
        String userId = decodedJWT.getClaim("userId").asString();
        String partnerId = decodedJWT.getClaim("partnerId").asString();
        String username = decodedJWT.getClaim("username").asString();
        String role = decodedJWT.getClaim("role").asString();

        switch (role) {
            case "user" -> {
                SSOData ssoData = new SSOData(email, userId, username);
                userService.createUser(ssoData);
            }
            case "partner" -> {
                SSOData ssoData = new SSOData(email, partnerId, username);
                partnerService.createPartner(ssoData);
            }

            default -> throw new IllegalStateException("Unexpected value: " + role);
        }

        return ResponseEntity.ok().build();
    }
}
