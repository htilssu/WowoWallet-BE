package com.wowo.wowo.contexts.auth.presentation.rest

import com.wowo.wowo.contexts.auth.application.dto.LoginRequest
import com.wowo.wowo.contexts.auth.application.dto.LoginResponse
import com.wowo.wowo.contexts.auth.application.dto.RefreshTokenRequest
import com.wowo.wowo.contexts.auth.application.dto.RefreshTokenResponse
import com.wowo.wowo.contexts.auth.application.usecase.AuthUseCase
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/auth")
class AuthController(
    private val authUseCase: AuthUseCase
) {

    @PostMapping("/login")
    fun login(@RequestBody request: LoginRequest): ResponseEntity<LoginResponse> {
        val response = authUseCase.login(request)
        return ResponseEntity.ok(response)
    }

    @PostMapping("/refresh")
    fun refreshToken(@RequestBody request: RefreshTokenRequest): ResponseEntity<RefreshTokenResponse> {
        val response = authUseCase.refreshToken(request)
        return ResponseEntity.ok(response)
    }
}
