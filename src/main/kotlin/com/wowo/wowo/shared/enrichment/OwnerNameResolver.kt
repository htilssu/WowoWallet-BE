package com.wowo.wowo.shared.enrichment

import com.wowo.wowo.shared.domain.OwnerType
import org.springframework.stereotype.Component

/**
 * Service that coordinates owner name resolution across different owner types.
 * Uses strategy pattern to delegate to type-specific resolvers.
 * 
 * This is the main entry point for resolving owner names in batch.
 */
@Component
class OwnerNameResolver(
    strategies: List<OwnerNameResolverStrategy>
) {
    private val strategyMap: Map<OwnerType, OwnerNameResolverStrategy> = 
        strategies.associateBy { it.supportedType }
    
    /**
     * Resolve names for owners grouped by type.
     * Performs batch lookups for each owner type.
     * 
     * @param ownersByType Map of OwnerType to set of owner IDs
     * @return OwnerNameContext containing resolved names
     */
    fun resolveNames(ownersByType: Map<OwnerType, Set<String>>): OwnerNameContext {
        val namesByType = mutableMapOf<OwnerType, Map<String, String?>>()
        
        ownersByType.forEach { (ownerType, ownerIds) ->
            if (ownerIds.isEmpty()) return@forEach
            
            val strategy = strategyMap[ownerType]
            val resolvedNames = strategy?.resolveNames(ownerIds) 
                ?: ownerIds.associateWith { null } // Default to null if no strategy
            
            namesByType[ownerType] = resolvedNames
        }
        
        return OwnerNameContext.fromGroupedNames(namesByType)
    }
    
    /**
     * Resolve names from a list of OwnerInfo objects.
     * Groups by type internally for efficient batch lookups.
     * 
     * @param ownerInfos List of OwnerInfo with unresolved names
     * @return OwnerNameContext with resolved names
     */
    fun resolveNames(ownerInfos: Collection<OwnerInfo>): OwnerNameContext {
        val ownersByType = ownerInfos
            .groupBy { it.ownerType }
            .mapValues { (_, infos) -> infos.map { it.ownerId }.toSet() }
        
        return resolveNames(ownersByType)
    }
    
    /**
     * Check if a resolver exists for the given type.
     */
    fun supportsType(ownerType: OwnerType): Boolean = strategyMap.containsKey(ownerType)
    
    /**
     * Get all supported owner types.
     */
    fun supportedTypes(): Set<OwnerType> = strategyMap.keys
}
