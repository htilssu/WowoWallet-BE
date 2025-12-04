package com.wowo.wowo.contexts.user.domain.repository

import com.wowo.wowo.contexts.user.domain.entity.Permission
import com.wowo.wowo.contexts.user.domain.valueobject.PermissionId
import com.wowo.wowo.contexts.user.domain.valueobject.PermissionName
import com.wowo.wowo.contexts.user.domain.valueobject.UserId

/**
 * Domain Repository interface for Permission
 * Defines the contract for permission persistence operations
 */
interface PermissionRepository {
    
    /**
     * Save a permission
     */
    fun save(permission: Permission): Permission
    
    /**
     * Find permission by ID
     */
    fun findById(id: PermissionId): Permission?
    
    /**
     * Find permission by name
     */
    fun findByName(name: PermissionName): Permission?
    
    /**
     * Find all permissions by resource
     */
    fun findByResource(resource: String): List<Permission>
    
    /**
     * Find all permissions
     */
    fun findAll(): List<Permission>
    
    /**
     * Check if permission exists by name
     */
    fun existsByName(name: PermissionName): Boolean
    
    /**
     * Delete a permission
     */
    fun delete(permission: Permission)
    
    /**
     * Find all permissions for a user (including role permissions)
     */
    fun findAllByUserId(userId: UserId): Set<Permission>
}
