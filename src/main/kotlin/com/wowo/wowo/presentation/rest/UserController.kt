package com.wowo.wowo.presentation.rest

import com.wowo.wowo.contexts.user.application.dto.RegisterUserCommand
import com.wowo.wowo.contexts.user.application.dto.UserDTO
import com.wowo.wowo.contexts.user.application.usecase.GetUserUseCase
import com.wowo.wowo.contexts.user.application.usecase.RegisterUserUseCase
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*

/**
 * REST Controller for User operations
 */
@RestController
@RequestMapping("/users")
class UserController(
    private val registerUserUseCase: RegisterUserUseCase,
    private val getUserUseCase: GetUserUseCase
) {

    @PostMapping("/register")
    fun registerUser(@RequestBody request: RegisterUserRequest): ResponseEntity<UserDTO> {
        val command = RegisterUserCommand(
            username = request.username,
            password = request.password,
            email = request.email,
            phoneNumber = request.phoneNumber
        )

        val userDTO = registerUserUseCase.execute(command)
        return ResponseEntity.status(HttpStatus.CREATED).body(userDTO)
    }

    @GetMapping("/{userId}")
    @PreAuthorize("isAuthenticated()")
    fun getUser(@PathVariable userId: String): ResponseEntity<UserDTO> {
        val userDTO = getUserUseCase.execute(userId)
        return ResponseEntity.ok(userDTO)
    }
}

data class RegisterUserRequest(
    val username: String,
    val password: String,
    val email: String,
    val phoneNumber: String?
)

