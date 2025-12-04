package com.wowo.wowo.shared.infrastructure.security.annotations

import com.wowo.wowo.shared.infrastructure.security.Role

/**
 * Annotation to require user to have one of the specified roles
 * 
 * Usage:
 * ```
 * @RequireRole(Role.ADMIN, Role.SUPER_ADMIN)
 * fun adminOnlyMethod() { ... }
 * ```
 */
@Target(AnnotationTarget.FUNCTION, AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
annotation class RequireRole(
    /**
     * List of roles allowed to access
     * User only needs to have one of these roles
     */
    vararg val roles: Role
)
