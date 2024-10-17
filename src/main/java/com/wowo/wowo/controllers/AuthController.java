package com.wowo.wowo.controllers;

import com.wowo.wowo.data.dto.SSOData;
import com.wowo.wowo.models.User;
import com.wowo.wowo.services.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.websocket.server.PathParam;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/auth")
@Tag(name = "Auth", description = "Xác thực")
public class AuthController {

    private final UserService userService;

    @RequestMapping("/sso")
    public void handleCallback(SSOData ssoData) {
        final User userByIdOrUsernameOrEmail = userService.getUserByIdOrUsernameOrEmail(
                ssoData.getId(), ssoData.getUsername(),
                ssoData.getEmail());

        if (userByIdOrUsernameOrEmail == null) {
            // create new user
            userService.createUser(ssoData);
        }

    }
}
