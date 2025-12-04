package com.wowo.wowo.contexts.user.application.dto

/**
 * DTO for assigning roles to user
 */
data class AssignRolesToUserDto(
    val userId: String,
    val roleIds: List<String>
)

/**
 * DTO for assigning permissions to user
 */
data class AssignPermissionsToUserDto(
    val userId: String,
    val permissionIds: List<String>
)

/**
 * DTO for user authorization info response
 */
data class UserAuthorizationDto(
    val userId: String,
    val roles: List<RoleDto>,
    val directPermissions: List<PermissionDto>,
    val allPermissions: List<PermissionDto>
)
