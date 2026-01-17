package com.wowo.wowo.contexts.auth.application.usecase

import com.wowo.wowo.contexts.auth.application.dto.LoginRequest
import com.wowo.wowo.contexts.auth.application.dto.RefreshTokenRequest
import com.wowo.wowo.contexts.user.domain.entity.Role
import com.wowo.wowo.contexts.user.domain.entity.User
import com.wowo.wowo.contexts.user.domain.repository.RoleRepository
import com.wowo.wowo.contexts.user.domain.repository.UserRepository
import com.wowo.wowo.contexts.user.domain.valueobject.*
import com.wowo.wowo.infrastructure.security.jwt.JwtTokenProvider
import com.wowo.wowo.shared.valueobject.Email
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.security.crypto.password.PasswordEncoder
import java.util.UUID

@ExtendWith(MockitoExtension::class)
class AuthUseCaseTest {

    @Mock
    lateinit var userRepository: UserRepository

    @Mock
    lateinit var roleRepository: RoleRepository

    @Mock
    lateinit var jwtTokenProvider: JwtTokenProvider

    @Mock
    lateinit var passwordEncoder: PasswordEncoder

    private lateinit var authUseCase: AuthUseCase

    private lateinit var user: User
    private lateinit var role: Role

    @BeforeEach
    fun setUp() {
        val userId = UserId.fromString(UUID.randomUUID().toString())
        user = User(
            id = userId,
            username = Username("testuser"),
            password = Password("hashedPassword"),
            email = Email("test@example.com"),
            phoneNumber = null
        )
        
        role = Role.create(
            name = RoleName("ROLE_USER")
        )
        
        // Use reflection to set private jwtExpirationMs if needed, 
        // but since it's a value injection in constructor, we might need a workaround or assume default behavior
        // In this unit test, we are mocking the dependency, so we don't worry about @Value injection 
        // as long as we use the constructor for InjectMocks. 
        // However, InjectMocks might fail with primitive types not mocked.
        // We will construct the usecase manually to be safe.
        authUseCase = AuthUseCase(userRepository, roleRepository, jwtTokenProvider, passwordEncoder, 3600000)
    }

    @Test
    fun `should login successfully and return tokens with roles`() {
        // Given
        val request = LoginRequest("test@example.com", "password")
        
        Mockito.`when`(userRepository.findByEmail(Email("test@example.com")))
            .thenReturn(user)
        Mockito.`when`(passwordEncoder.matches("password", "hashedPassword"))
            .thenReturn(true)
        Mockito.`when`(roleRepository.findAllByUserId(user.id))
            .thenReturn(setOf(role))
        Mockito.`when`(jwtTokenProvider.generateAccessToken(
            user.id.toString(),
            user.email.value,
            setOf("ROLE_USER")
        )).thenReturn("accessToken")
        Mockito.`when`(jwtTokenProvider.generateRefreshToken(user.id.toString()))
            .thenReturn("refreshToken")

        // When
        val response = authUseCase.login(request)

        // Then
        assertNotNull(response)
        assertEquals("accessToken", response.accessToken)
        assertEquals("refreshToken", response.refreshToken)
        assertEquals(user.id.toString(), response.userId)
        
        Mockito.verify(roleRepository).findAllByUserId(user.id)
    }

    @Test
    fun `should refresh token successfully with roles`() {
        // Given
        val request = RefreshTokenRequest("validRefreshToken")
        val userId = user.id.toString()

        Mockito.`when`(jwtTokenProvider.validateToken("validRefreshToken")).thenReturn(true)
        Mockito.`when`(jwtTokenProvider.isRefreshToken("validRefreshToken")).thenReturn(true)
        Mockito.`when`(jwtTokenProvider.getUserIdFromToken("validRefreshToken")).thenReturn(userId)
        Mockito.`when`(userRepository.findById(user.id)).thenReturn(user)
        Mockito.`when`(roleRepository.findAllByUserId(user.id)).thenReturn(setOf(role))
        Mockito.`when`(jwtTokenProvider.generateAccessToken(
            user.id.toString(),
            user.email.value,
            setOf("ROLE_USER")
        )).thenReturn("newAccessToken")

        // When
        val response = authUseCase.refreshToken(request)

        // Then
        assertNotNull(response)
        assertEquals("newAccessToken", response.accessToken)
        
        Mockito.verify(roleRepository).findAllByUserId(user.id)
    }
}
