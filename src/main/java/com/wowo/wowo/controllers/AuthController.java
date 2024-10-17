package com.wowo.wowo.controllers;

import com.wowo.wowo.annotations.authorized.IsUser;
import com.wowo.wowo.data.dto.SSOData;
import com.wowo.wowo.models.User;
import com.wowo.wowo.services.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/auth")
@Tag(name = "Auth", description = "Xác thực")
public class AuthController {

    private final UserService userService;

    @RequestMapping("/sso")
    @IsUser
    public void handleCallback(@RequestParam @RequestBody SSOData ssoData) {
        final User userByIdOrUsernameOrEmail = userService.getUserByIdOrUsernameOrEmail(
                ssoData.getId(), ssoData.getUsername(),
                ssoData.getEmail());

        if (userByIdOrUsernameOrEmail == null) {
            // create new user
            userService.createUser(ssoData);
        }

    }
}
