package com.wowo.wowo.controller;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.wowo.wowo.data.dto.SSOData;
import com.wowo.wowo.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("auth")
@Tag(name = "Auth", description = "Xác thực")
public class AuthController {

    private final UserService userService;

    @RequestMapping("/sso")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> handleCallback(Authentication authentication) {
        DecodedJWT decodedJWT = (DecodedJWT) authentication.getDetails();
        String email = decodedJWT.getClaim("email")
                .asString();
        String userId = decodedJWT.getClaim("userId")
                .asString();
        String partnerId = decodedJWT.getClaim("partnerId")
                .asString();
        String username = decodedJWT.getClaim("username")
                .asString();
        String firstName = decodedJWT.getClaim("firstName")
                .asString();
        String lastName = decodedJWT.getClaim("lastName")
                .asString();
        String role = decodedJWT.getClaim("role")
                .asString();
        String name = decodedJWT.getClaim("name")
                .asString();

        switch (role) {
            case "user" -> {
                SSOData ssoData = new SSOData(email, userId, username, firstName, lastName, name);
                userService.createUser(ssoData);
            }
            case "partner" -> {
                SSOData ssoData = new SSOData(email, partnerId, username, firstName, lastName,
                        name);
            }

            default -> throw new IllegalStateException("Unexpected value: " + role);
        }

        return ResponseEntity.ok()
                .build();
    }
}
