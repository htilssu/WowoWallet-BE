package com.wowo.wowo.contexts.auth.application.usecase

import com.wowo.wowo.contexts.auth.application.dto.LoginRequest
import com.wowo.wowo.contexts.auth.application.dto.LoginResponse
import com.wowo.wowo.contexts.auth.application.dto.RefreshTokenRequest
import com.wowo.wowo.contexts.auth.application.dto.RefreshTokenResponse
import com.wowo.wowo.contexts.user.domain.repository.UserRepository
import com.wowo.wowo.infrastructure.security.jwt.JwtTokenProvider
import com.wowo.wowo.shared.valueobject.Email
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class AuthUseCase(
    private val userRepository: UserRepository,
    private val jwtTokenProvider: JwtTokenProvider,
    private val passwordEncoder: PasswordEncoder,
    @param:Value("\${jwt.expiration-ms:86400000}") private val jwtExpirationMs: Long
) {

    fun login(request: LoginRequest): LoginResponse {
        val email = Email(request.email)
        val user = userRepository.findByEmail(email)
            ?: throw AuthenticationException("Invalid email or password")

        if (!user.isActive) {
            throw AuthenticationException("Account is deactivated")
        }

        val passwordMatches = passwordEncoder.matches(request.password, user.getPassword().hashedValue)
        if (!passwordMatches) {
            throw AuthenticationException("Invalid email or password")
        }

        // TODO: Get user roles from authorization repository
        val roles = emptySet<String>()

        val accessToken = jwtTokenProvider.generateAccessToken(
            userId = user.id.toString(),
            email = user.email.value,
            roles = roles
        )

        val refreshToken = jwtTokenProvider.generateRefreshToken(user.id.toString())

        return LoginResponse(
            accessToken = accessToken,
            refreshToken = refreshToken,
            expiresIn = jwtExpirationMs / 1000, // Convert to seconds
            userId = user.id.toString()
        )
    }

    fun refreshToken(request: RefreshTokenRequest): RefreshTokenResponse {
        val token = request.refreshToken

        if (!jwtTokenProvider.validateToken(token)) {
            throw AuthenticationException("Invalid refresh token")
        }

        if (!jwtTokenProvider.isRefreshToken(token)) {
            throw AuthenticationException("Token is not a refresh token")
        }

        val userId = jwtTokenProvider.getUserIdFromToken(token)

        // Verify user still exists and is active
        val user = userRepository.findById(com.wowo.wowo.contexts.user.domain.valueobject.UserId.fromString(userId))
            ?: throw AuthenticationException("User not found")

        if (!user.isActive) {
            throw AuthenticationException("Account is deactivated")
        }

        // TODO: Get user roles from authorization repository
        val roles = emptySet<String>()

        val accessToken = jwtTokenProvider.generateAccessToken(
            userId = user.id.toString(),
            email = user.email.value,
            roles = roles
        )

        return RefreshTokenResponse(
            accessToken = accessToken,
            expiresIn = jwtExpirationMs / 1000
        )
    }
}

class AuthenticationException(message: String) : RuntimeException(message)
