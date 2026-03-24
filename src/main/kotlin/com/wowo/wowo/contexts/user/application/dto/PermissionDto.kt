package com.wowo.wowo.contexts.user.application.dto

import java.time.Instant
import java.time.LocalDateTime

/**
 * DTO for Permission response
 */
data class PermissionDto(
    val id: String,
    val name: String,
    val description: String?,
    val resource: String,
    val action: String,
    val createdAt: Instant
)

/**
 * DTO for creating a new permission
 */
data class CreatePermissionDto(
    val name: String,
    val description: String?
)
