package com.wowo.wowo.shared.infrastructure.security.annotations

import com.wowo.wowo.shared.infrastructure.security.Permission

/**
 * Annotation to require user to have specific permission(s)
 * 
 * Usage:
 * ```
 * @RequirePermission(Permission.WALLET_TRANSFER)
 * fun transferMoney() { ... }
 * 
 * @RequirePermission(Permission.TRANSACTION_READ, Permission.TRANSACTION_WRITE, requireAll = true)
 * fun manageTransactions() { ... }
 * ```
 */
@Target(AnnotationTarget.FUNCTION, AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
annotation class RequirePermission(
    /**
     * List of required permissions
     */
    vararg val permissions: Permission,
    
    /**
     * If true, user must have ALL permissions
     * If false, user only needs ONE of the permissions
     */
    val requireAll: Boolean = false
)
