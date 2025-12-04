package com.wowo.wowo.contexts.user.application.dto

import java.time.LocalDateTime

/**
 * DTO for Role response
 */
data class RoleDto(
    val id: String,
    val name: String,
    val description: String?,
    val permissions: List<PermissionDto>,
    val createdAt: LocalDateTime
)

/**
 * DTO for creating a new role
 */
data class CreateRoleDto(
    val name: String,
    val description: String?,
    val permissionIds: List<String> = emptyList()
)

/**
 * DTO for updating role permissions
 */
data class UpdateRolePermissionsDto(
    val roleId: String,
    val permissionIds: List<String>
)
