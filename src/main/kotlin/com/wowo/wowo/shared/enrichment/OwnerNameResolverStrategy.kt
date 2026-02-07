package com.wowo.wowo.shared.enrichment

import com.wowo.wowo.shared.domain.OwnerType

/**
 * Strategy interface for resolving owner names by type.
 * Each implementation handles a specific OwnerType.
 */
interface OwnerNameResolverStrategy {
    
    /**
     * The owner type this strategy handles.
     */
    val supportedType: OwnerType
    
    /**
     * Resolve names for the given owner IDs.
     * 
     * @param ownerIds Set of owner IDs to resolve
     * @return Map of ownerId to resolved name (null if not found)
     */
    fun resolveNames(ownerIds: Set<String>): Map<String, String?>
}
