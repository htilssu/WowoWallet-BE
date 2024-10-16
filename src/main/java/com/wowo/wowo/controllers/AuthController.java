package com.wowo.wowo.controllers;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/auth")
@Tag(name = "Auth", description = "Xác thực")
public class AuthController {

    @RequestMapping("/sso")
    public void handleCallback(){

    }
}
