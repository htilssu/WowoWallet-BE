package com.wowo.wowo.shared.infrastructure.security.annotations

import com.wowo.wowo.shared.infrastructure.security.Role

/**
 * Shortcut annotation to require Super Admin role
 * 
 * Usage:
 * ```
 * @RequireSuperAdmin
 * fun systemConfiguration() { ... }
 * ```
 */
@Target(AnnotationTarget.FUNCTION, AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
@RequireRole(Role.SUPER_ADMIN)
annotation class RequireSuperAdmin
