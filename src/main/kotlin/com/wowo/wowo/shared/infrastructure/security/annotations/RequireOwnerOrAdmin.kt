package com.wowo.wowo.shared.infrastructure.security.annotations

/**
 * Annotation to require user to be the resource owner or an Admin
 * Uses SpEL expression to determine owner
 * 
 * Usage:
 * ```
 * @RequireOwnerOrAdmin(ownerIdParam = "userId")
 * fun getUserProfile(@PathVariable userId: String) { ... }
 * 
 * @RequireOwnerOrAdmin(ownerIdParam = "#wallet.ownerId")
 * fun updateWallet(@RequestBody wallet: WalletDTO) { ... }
 * ```
 */
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
annotation class RequireOwnerOrAdmin(
    /**
     * Parameter name or SpEL expression to get owner ID
     * Examples: "userId", "#request.userId", "#entity.ownerId"
     */
    val ownerIdParam: String
)
