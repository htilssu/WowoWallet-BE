package com.wowo.wowo.shared.infrastructure.security.annotations

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
annotation class RequireAuthenticated
