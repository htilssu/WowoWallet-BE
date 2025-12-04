package com.wowo.wowo.contexts.user.domain.repository

import com.wowo.wowo.contexts.user.domain.entity.Role
import com.wowo.wowo.contexts.user.domain.valueobject.RoleId
import com.wowo.wowo.contexts.user.domain.valueobject.RoleName
import com.wowo.wowo.contexts.user.domain.valueobject.UserId

/**
 * Domain Repository interface for Role
 * Defines the contract for role persistence operations
 */
interface RoleRepository {
    
    /**
     * Save a role
     */
    fun save(role: Role): Role
    
    /**
     * Find role by ID
     */
    fun findById(id: RoleId): Role?
    
    /**
     * Find role by name
     */
    fun findByName(name: RoleName): Role?
    
    /**
     * Find all roles
     */
    fun findAll(): List<Role>
    
    /**
     * Check if role exists by name
     */
    fun existsByName(name: RoleName): Boolean
    
    /**
     * Delete a role
     */
    fun delete(role: Role)
    
    /**
     * Find all roles for a user
     */
    fun findAllByUserId(userId: UserId): Set<Role>
}
