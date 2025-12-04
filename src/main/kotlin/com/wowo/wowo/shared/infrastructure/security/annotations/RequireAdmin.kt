package com.wowo.wowo.shared.infrastructure.security.annotations

import com.wowo.wowo.shared.infrastructure.security.Role

/**
 * Shortcut annotation to require Admin or Super Admin role
 * 
 * Usage:
 * ```
 * @RequireAdmin
 * fun adminDashboard() { ... }
 * ```
 */
@Target(AnnotationTarget.FUNCTION, AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
@RequireRole(Role.ADMIN, Role.SUPER_ADMIN)
annotation class RequireAdmin
