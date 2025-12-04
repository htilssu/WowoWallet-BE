package com.wowo.wowo.contexts.user.domain.repository

import com.wowo.wowo.contexts.user.domain.valueobject.PermissionId
import com.wowo.wowo.contexts.user.domain.valueobject.RoleId
import com.wowo.wowo.contexts.user.domain.valueobject.UserId

/**
 * Domain Repository interface for User Authorization
 * Handles user-role and user-permission associations
 */
interface UserAuthorizationRepository {
    
    // User-Role operations
    
    /**
     * Assign a role to a user
     */
    fun assignRoleToUser(userId: UserId, roleId: RoleId)
    
    /**
     * Remove a role from a user
     */
    fun removeRoleFromUser(userId: UserId, roleId: RoleId)
    
    /**
     * Get all role IDs for a user
     */
    fun getRoleIdsByUserId(userId: UserId): Set<RoleId>
    
    /**
     * Check if user has a specific role
     */
    fun userHasRole(userId: UserId, roleId: RoleId): Boolean
    
    /**
     * Remove all roles from a user
     */
    fun removeAllRolesFromUser(userId: UserId)
    
    // User-Permission operations (direct permissions)
    
    /**
     * Assign a permission directly to a user
     */
    fun assignPermissionToUser(userId: UserId, permissionId: PermissionId)
    
    /**
     * Remove a direct permission from a user
     */
    fun removePermissionFromUser(userId: UserId, permissionId: PermissionId)
    
    /**
     * Get all direct permission IDs for a user
     */
    fun getDirectPermissionIdsByUserId(userId: UserId): Set<PermissionId>
    
    /**
     * Check if user has a specific permission directly assigned
     */
    fun userHasDirectPermission(userId: UserId, permissionId: PermissionId): Boolean
    
    /**
     * Remove all direct permissions from a user
     */
    fun removeAllPermissionsFromUser(userId: UserId)
}
