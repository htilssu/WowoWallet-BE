package com.wowo.wowo.contexts.user.domain.valueobject

import com.wowo.wowo.shared.domain.ValueObject

/**
 * Value Object representing a Permission name
 */
data class PermissionName(val value: String) : ValueObject {
    
    init {
        require(value.isNotBlank()) { "Permission name cannot be blank" }
        require(value.length <= 100) { "Permission name cannot exceed 100 characters" }
        require(value.contains(":")) { "Permission name must follow format 'resource:action'" }
    }
    
    val resource: String
        get() = value.substringBefore(":")
    
    val action: String
        get() = value.substringAfter(":")
    
    override fun toString(): String = value
    
    companion object {
        // User permissions
        val USER_READ = PermissionName("user:read")
        val USER_WRITE = PermissionName("user:write")
        val USER_DELETE = PermissionName("user:delete")
        
        // Wallet permissions
        val WALLET_READ = PermissionName("wallet:read")
        val WALLET_WRITE = PermissionName("wallet:write")
        val WALLET_DELETE = PermissionName("wallet:delete")
        val WALLET_TRANSFER = PermissionName("wallet:transfer")
        
        // Transaction permissions
        val TRANSACTION_READ = PermissionName("transaction:read")
        val TRANSACTION_WRITE = PermissionName("transaction:write")
        val TRANSACTION_APPROVE = PermissionName("transaction:approve")
        val TRANSACTION_CANCEL = PermissionName("transaction:cancel")
        
        // Admin permissions
        val ADMIN_READ = PermissionName("admin:read")
        val ADMIN_WRITE = PermissionName("admin:write")
        val ADMIN_USER_MANAGE = PermissionName("admin:user_manage")
        val ADMIN_SYSTEM_CONFIG = PermissionName("admin:system_config")
        
        // Report permissions
        val REPORT_VIEW = PermissionName("report:view")
        val REPORT_EXPORT = PermissionName("report:export")
    }
}
