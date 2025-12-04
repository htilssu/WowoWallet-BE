package com.wowo.wowo.contexts.user.application.usecase

import com.wowo.wowo.contexts.user.application.dto.PermissionDto
import com.wowo.wowo.contexts.user.application.dto.RoleDto
import com.wowo.wowo.contexts.user.application.dto.UserAuthorizationDto
import com.wowo.wowo.contexts.user.domain.entity.Permission
import com.wowo.wowo.contexts.user.domain.entity.Role
import com.wowo.wowo.contexts.user.domain.repository.PermissionRepository
import com.wowo.wowo.contexts.user.domain.repository.RoleRepository
import com.wowo.wowo.contexts.user.domain.repository.UserAuthorizationRepository
import com.wowo.wowo.contexts.user.domain.valueobject.*
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

/**
 * Use Case for managing user authorization (roles and permissions)
 */
@Service
@Transactional
class UserAuthorizationUseCase(
    private val roleRepository: RoleRepository,
    private val permissionRepository: PermissionRepository,
    private val userAuthorizationRepository: UserAuthorizationRepository
) {

    /**
     * Assign a role to a user
     */
    fun assignRoleToUser(userId: String, roleName: String) {
        val userIdVo = UserId(UUID.fromString(userId))
        val role = roleRepository.findByName(RoleName(roleName))
            ?: throw IllegalArgumentException("Role not found: $roleName")
        
        userAuthorizationRepository.assignRoleToUser(userIdVo, role.id)
    }

    /**
     * Assign multiple roles to a user
     */
    fun assignRolesToUser(userId: String, roleNames: List<String>) {
        roleNames.forEach { assignRoleToUser(userId, it) }
    }

    /**
     * Remove a role from a user
     */
    fun removeRoleFromUser(userId: String, roleName: String) {
        val userIdVo = UserId.fromString(userId)
        val role = roleRepository.findByName(RoleName(roleName))
            ?: throw IllegalArgumentException("Role not found: $roleName")
        
        userAuthorizationRepository.removeRoleFromUser(userIdVo, role.id)
    }

    /**
     * Assign a permission directly to a user
     */
    fun assignPermissionToUser(userId: String, permissionName: String) {
        val userIdVo = UserId.fromString(userId)
        val permission = permissionRepository.findByName(PermissionName(permissionName))
            ?: throw IllegalArgumentException("Permission not found: $permissionName")
        
        userAuthorizationRepository.assignPermissionToUser(userIdVo, permission.id)
    }

    /**
     * Remove a direct permission from a user
     */
    fun removePermissionFromUser(userId: String, permissionName: String) {
        val userIdVo = UserId.fromString(userId)
        val permission = permissionRepository.findByName(PermissionName(permissionName))
            ?: throw IllegalArgumentException("Permission not found: $permissionName")
        
        userAuthorizationRepository.removePermissionFromUser(userIdVo, permission.id)
    }

    /**
     * Get all roles for a user
     */
    @Transactional(readOnly = true)
    fun getUserRoles(userId: String): List<RoleDto> {
        val userIdVo = UserId.fromString(userId)
        val roles = roleRepository.findAllByUserId(userIdVo)
        return roles.map { toRoleDto(it) }
    }

    /**
     * Get all role names for a user
     */
    @Transactional(readOnly = true)
    fun getUserRoleNames(userId: String): Set<String> {
        val userIdVo = UserId.fromString(userId)
        return roleRepository.findAllByUserId(userIdVo)
            .map { it.name.value }
            .toSet()
    }

    /**
     * Get all permissions for a user (including role permissions)
     */
    @Transactional(readOnly = true)
    fun getUserPermissions(userId: String): List<PermissionDto> {
        val userIdVo = UserId.fromString(userId)
        val permissions = permissionRepository.findAllByUserId(userIdVo)
        return permissions.map { toPermissionDto(it) }
    }

    /**
     * Get all permission names for a user
     */
    @Transactional(readOnly = true)
    fun getUserPermissionNames(userId: String): Set<String> {
        val userIdVo = UserId.fromString(userId)
        return permissionRepository.findAllByUserId(userIdVo)
            .map { it.name.value }
            .toSet()
    }

    /**
     * Get complete user authorization info
     */
    @Transactional(readOnly = true)
    fun getUserAuthorizationInfo(userId: String): UserAuthorizationDto {
        val userIdVo = UserId.fromString(userId)

        val roles = roleRepository.findAllByUserId(userIdVo)
        val allPermissions = permissionRepository.findAllByUserId(userIdVo)
        
        // Get direct permissions
        val directPermissionIds = userAuthorizationRepository.getDirectPermissionIdsByUserId(userIdVo)
        val directPermissions = allPermissions.filter { directPermissionIds.contains(it.id) }
        
        return UserAuthorizationDto(
            userId = userId,
            roles = roles.map { toRoleDto(it) },
            directPermissions = directPermissions.map { toPermissionDto(it) },
            allPermissions = allPermissions.map { toPermissionDto(it) }
        )
    }

    /**
     * Check if user has a specific role
     */
    @Transactional(readOnly = true)
    fun userHasRole(userId: String, roleName: String): Boolean {
        val userIdVo = UserId.fromString(userId)
        val role = roleRepository.findByName(RoleName(roleName)) ?: return false
        return userAuthorizationRepository.userHasRole(userIdVo, role.id)
    }

    /**
     * Check if user has any of the specified roles
     */
    @Transactional(readOnly = true)
    fun userHasAnyRole(userId: String, vararg roleNames: String): Boolean {
        return roleNames.any { userHasRole(userId, it) }
    }

    /**
     * Check if user has a specific permission
     */
    @Transactional(readOnly = true)
    fun userHasPermission(userId: String, permissionName: String): Boolean {
        return getUserPermissionNames(userId).contains(permissionName)
    }

    /**
     * Check if user has all specified permissions
     */
    @Transactional(readOnly = true)
    fun userHasAllPermissions(userId: String, vararg permissionNames: String): Boolean {
        val userPermissions = getUserPermissionNames(userId)
        return permissionNames.all { userPermissions.contains(it) }
    }

    /**
     * Check if user has any of the specified permissions
     */
    @Transactional(readOnly = true)
    fun userHasAnyPermission(userId: String, vararg permissionNames: String): Boolean {
        val userPermissions = getUserPermissionNames(userId)
        return permissionNames.any { userPermissions.contains(it) }
    }

    /**
     * Set user roles (replaces all existing roles)
     */
    fun setUserRoles(userId: String, roleNames: List<String>) {
        val userIdVo = UserId.fromString(userId)
        userAuthorizationRepository.removeAllRolesFromUser(userIdVo)
        roleNames.forEach { assignRoleToUser(userId, it) }
    }

    /**
     * Remove all roles from a user
     */
    fun removeAllRolesFromUser(userId: String) {
        val userIdVo = UserId.fromString(userId)
        userAuthorizationRepository.removeAllRolesFromUser(userIdVo)
    }

    /**
     * Remove all direct permissions from a user
     */
    fun removeAllPermissionsFromUser(userId: String) {
        val userIdVo = UserId.fromString(userId)
        userAuthorizationRepository.removeAllPermissionsFromUser(userIdVo)
    }

    // Mapper functions
    
    private fun toPermissionDto(permission: Permission): PermissionDto {
        return PermissionDto(
            id = permission.id.toString(),
            name = permission.name.value,
            description = permission.description,
            resource = permission.resource,
            action = permission.action,
            createdAt = permission.createdAt
        )
    }

    private fun toRoleDto(role: Role): RoleDto {
        val permissions = role.permissions
            .mapNotNull { permissionRepository.findById(it) }
            .map { toPermissionDto(it) }
        
        return RoleDto(
            id = role.id.toString(),
            name = role.name.value,
            description = role.description,
            permissions = permissions,
            createdAt = role.createdAt
        )
    }
}
