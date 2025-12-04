package com.wowo.wowo.shared.infrastructure.security

/**
 * Enum defining specific permissions in the system
 */
enum class Permission(val value: String) {
    // User permissions
    USER_READ("user:read"),
    USER_WRITE("user:write"),
    USER_DELETE("user:delete"),
    
    // Wallet permissions
    WALLET_READ("wallet:read"),
    WALLET_WRITE("wallet:write"),
    WALLET_DELETE("wallet:delete"),
    WALLET_TRANSFER("wallet:transfer"),
    
    // Transaction permissions
    TRANSACTION_READ("transaction:read"),
    TRANSACTION_WRITE("transaction:write"),
    TRANSACTION_APPROVE("transaction:approve"),
    TRANSACTION_CANCEL("transaction:cancel"),
    
    // Admin permissions
    ADMIN_READ("admin:read"),
    ADMIN_WRITE("admin:write"),
    ADMIN_USER_MANAGE("admin:user_manage"),
    ADMIN_SYSTEM_CONFIG("admin:system_config"),
    
    // Report permissions
    REPORT_VIEW("report:view"),
    REPORT_EXPORT("report:export");
    
    companion object {
        fun fromValue(value: String): Permission? {
            return entries.find { it.value == value || it.name == value }
        }
    }
}
