package com.wowo.wowo.shared.enrichment

import com.wowo.wowo.shared.domain.OwnerType

/**
 * Enrichment context for owner name resolution.
 * Supports multiple owner types and provides lookup by owner ID and type.
 * 
 * This context is designed to handle batch lookups efficiently by grouping
 * owners by type and performing type-specific batch queries.
 * 
 * @param ownerInfoMap Map of (ownerId, ownerType) pair to resolved OwnerInfo
 */
data class OwnerNameContext(
    private val ownerInfoMap: Map<OwnerKey, OwnerInfo>
) : EnrichmentContext {
    
    /**
     * Key for owner lookup - combines ownerId and ownerType.
     */
    data class OwnerKey(
        val ownerId: String,
        val ownerType: OwnerType
    )
    
    companion object {
        fun empty() = OwnerNameContext(emptyMap())
        
        /**
         * Create context from a map of ownerId to OwnerInfo.
         * Useful when you already have OwnerInfo objects.
         */
        fun fromOwnerInfos(ownerInfos: Collection<OwnerInfo>): OwnerNameContext {
            val map = ownerInfos.associateBy { OwnerKey(it.ownerId, it.ownerType) }
            return OwnerNameContext(map)
        }
        
        /**
         * Create context from grouped name maps.
         * This is the primary way to build context from ACL lookups.
         * 
         * @param namesByType Map of OwnerType to (ownerId -> ownerName) maps
         */
        fun fromGroupedNames(namesByType: Map<OwnerType, Map<String, String?>>): OwnerNameContext {
            val map = mutableMapOf<OwnerKey, OwnerInfo>()
            
            namesByType.forEach { (ownerType, idToName) ->
                idToName.forEach { (ownerId, name) ->
                    val key = OwnerKey(ownerId, ownerType)
                    map[key] = OwnerInfo(ownerId, ownerType, name)
                }
            }
            
            return OwnerNameContext(map)
        }
    }
    
    /**
     * Get the owner name for the given ID and type.
     */
    fun getOwnerName(ownerId: String?, ownerType: OwnerType?): String? {
        if (ownerId == null || ownerType == null) return null
        val key = OwnerKey(ownerId, ownerType)
        return ownerInfoMap[key]?.ownerName
    }
    
    /**
     * Get the full OwnerInfo for the given ID and type.
     */
    fun getOwnerInfo(ownerId: String?, ownerType: OwnerType?): OwnerInfo? {
        if (ownerId == null || ownerType == null) return null
        val key = OwnerKey(ownerId, ownerType)
        return ownerInfoMap[key]
    }
    
    /**
     * Check if context contains info for the given owner.
     */
    fun contains(ownerId: String, ownerType: OwnerType): Boolean {
        return ownerInfoMap.containsKey(OwnerKey(ownerId, ownerType))
    }
    
    /**
     * Get all owner IDs for a specific type.
     */
    fun getOwnerIdsByType(ownerType: OwnerType): Set<String> {
        return ownerInfoMap.entries
            .filter { it.key.ownerType == ownerType }
            .map { it.key.ownerId }
            .toSet()
    }
    
    /**
     * Merge with another context.
     */
    fun merge(other: OwnerNameContext): OwnerNameContext {
        return OwnerNameContext(ownerInfoMap + other.ownerInfoMap)
    }
}
