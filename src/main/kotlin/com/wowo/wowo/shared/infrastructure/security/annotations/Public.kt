package com.wowo.wowo.shared.infrastructure.security.annotations

/**
 * Annotation to mark endpoint as public, no authentication required
 * 
 * Usage:
 * ```
 * @Public
 * @GetMapping("/health")
 * fun healthCheck() { ... }
 * ```
 */
@Target(AnnotationTarget.FUNCTION, AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
annotation class Public
