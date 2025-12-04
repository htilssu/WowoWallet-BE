package com.wowo.wowo.shared.infrastructure.security

import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component

/**
 * Utility class to get current user information
 */
@Component
class SecurityUtils {

    /**
     * Get current user details
     */
    fun getCurrentUser(): CustomUserDetails? {
        val authentication = SecurityContextHolder.getContext().authentication
        return authentication?.principal as? CustomUserDetails
    }

    /**
     * Get current user ID
     */
    fun getCurrentUserId(): String? {
        return getCurrentUser()?.getUserId()
    }

    /**
     * Get current username
     */
    fun getCurrentUsername(): String? {
        return getCurrentUser()?.username
    }

    /**
     * Check if user is authenticated
     */
    fun isAuthenticated(): Boolean {
        val authentication = SecurityContextHolder.getContext().authentication
        return authentication != null && 
               authentication.isAuthenticated && 
               authentication.principal != "anonymousUser"
    }

    /**
     * Check if user has a specific role
     */
    fun hasRole(role: Role): Boolean {
        return getCurrentUser()?.hasRole(role) ?: false
    }

    /**
     * Check if user has any of the specified roles
     */
    fun hasAnyRole(vararg roles: Role): Boolean {
        return getCurrentUser()?.hasAnyRole(*roles) ?: false
    }

    /**
     * Check if user has a specific permission
     */
    fun hasPermission(permission: Permission): Boolean {
        return getCurrentUser()?.hasPermission(permission) ?: false
    }

    /**
     * Check if user has any of the specified permissions
     */
    fun hasAnyPermission(vararg permissions: Permission): Boolean {
        return getCurrentUser()?.hasAnyPermission(*permissions) ?: false
    }

    /**
     * Check if user has all specified permissions
     */
    fun hasAllPermissions(vararg permissions: Permission): Boolean {
        return getCurrentUser()?.hasAllPermissions(*permissions) ?: false
    }

    /**
     * Check if user is an Admin
     */
    fun isAdmin(): Boolean {
        return hasAnyRole(Role.ADMIN, Role.SUPER_ADMIN)
    }

    /**
     * Check if user is a Super Admin
     */
    fun isSuperAdmin(): Boolean {
        return hasRole(Role.SUPER_ADMIN)
    }

    /**
     * Check if user is the owner of a resource
     */
    fun isOwner(resourceOwnerId: String): Boolean {
        return getCurrentUserId() == resourceOwnerId
    }

    /**
     * Check if user is either the owner or an admin
     */
    fun isOwnerOrAdmin(resourceOwnerId: String): Boolean {
        return isOwner(resourceOwnerId) || isAdmin()
    }
}
