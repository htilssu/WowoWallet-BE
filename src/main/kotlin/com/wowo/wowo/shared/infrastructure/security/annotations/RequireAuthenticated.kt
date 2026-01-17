package com.wowo.wowo.shared.infrastructure.security.annotations

import org.springframework.security.access.prepost.PreAuthorize

/**
 * Annotation to require user to be authenticated
 * 
 * Usage:
 * ```
 * @RequireAuthenticated
 * fun protectedMethod() { ... }
 * ```
 */
@Target(AnnotationTarget.FUNCTION, AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
@PreAuthorize("isAuthenticated()")
annotation class RequireAuthenticated
