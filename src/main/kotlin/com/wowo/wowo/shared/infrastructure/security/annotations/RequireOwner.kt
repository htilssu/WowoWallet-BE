package com.wowo.wowo.shared.infrastructure.security.annotations

/**
 * Annotation to require user to be the resource owner or an Admin
 * Uses SpEL expression to determine owner
 * 
 * Usage:
 * ```
 * @RequireOwner(ownerIdParam = "ownerId")
 * fun getUserProfile(@PathVariable ownerId: String) { ... }
 * 
 * @RequireOwner(ownerIdParam = "#wallet.ownerId")
 * fun updateWallet(@RequestBody wallet: WalletDTO) { ... }
 * ```
 */
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
annotation class RequireOwner(
    /**
     * Parameter name or SpEL expression to get owner ID
     * Examples: "userId", "#request.userId", "#entity.ownerId"
     */
    val ownerIdParam: String
)
