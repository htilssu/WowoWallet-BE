package com.wowo.wowo.shared.infrastructure.security

/**
 * Enum defining user roles in the system
 */
enum class Role(val value: String) {
    /**
     * Regular user
     */
    USER("ROLE_USER"),
    
    /**
     * Administrator
     */
    ADMIN("ROLE_ADMIN"),
    
    /**
     * Super Administrator
     */
    SUPER_ADMIN("ROLE_SUPER_ADMIN"),
    
    /**
     * Support staff
     */
    SUPPORT("ROLE_SUPPORT"),
    
    /**
     * Partner/Merchant
     */
    MERCHANT("ROLE_MERCHANT");
    
    companion object {
        fun fromValue(value: String): Role? {
            return entries.find { it.value == value || it.name == value }
        }
    }
}
