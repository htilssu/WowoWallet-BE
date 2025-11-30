package com.wowo.wowo.contexts.user.application.dto

/**
 * Command for registering a new user
 */
data class RegisterUserCommand(
    val username: String,
    val password: String,
    val email: String,
    val phoneNumber: String?
)

