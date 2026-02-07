package com.wowo.wowo.shared.enrichment

import com.wowo.wowo.shared.domain.OwnerType

/**
 * Represents owner information including ID, type, and resolved name.
 * This is the core value object for owner-related enrichment.
 * 
 * @param ownerId The owner's unique identifier
 * @param ownerType The type of owner (USER, GROUP, FUND_GROUP)
 * @param ownerName The resolved display name (null if not yet resolved)
 */
data class OwnerInfo(
    val ownerId: String,
    val ownerType: OwnerType,
    val ownerName: String? = null
) {
    companion object {
        /**
         * Create an unresolved OwnerInfo (name not yet looked up).
         */
        fun unresolved(ownerId: String, ownerType: OwnerType) = OwnerInfo(
            ownerId = ownerId,
            ownerType = ownerType,
            ownerName = null
        )
    }
    
    /**
     * Create a copy with the resolved name.
     */
    fun withName(name: String?): OwnerInfo = copy(ownerName = name)
    
    /**
     * Check if this owner info has a resolved name.
     */
    fun isResolved(): Boolean = ownerName != null
}
