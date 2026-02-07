package com.wowo.wowo.shared.enrichment

import com.wowo.wowo.shared.domain.OwnerType

/**
 * Abstract base enricher for entities with a single owner.
 * 
 * This provides a reusable pattern for enriching any entity that has
 * an ownerId and ownerType field with the resolved owner name.
 * 
 * @param T The DTO type to enrich
 */
abstract class SingleOwnerEnricher<T>(
    private val ownerNameResolver: OwnerNameResolver
) : EntityEnricher<T, OwnerNameContext> {
    
    /**
     * Extract the owner ID from the entity.
     */
    abstract fun extractOwnerId(entity: T): String?
    
    /**
     * Extract the owner type from the entity.
     */
    abstract fun extractOwnerType(entity: T): OwnerType?
    
    /**
     * Apply the resolved owner name to the entity.
     */
    abstract fun applyOwnerName(entity: T, ownerName: String?): T
    
    override fun enrich(entities: List<T>, context: OwnerNameContext): List<T> {
        return entities.map { entity ->
            val ownerId = extractOwnerId(entity)
            val ownerType = extractOwnerType(entity)
            val ownerName = context.getOwnerName(ownerId, ownerType)
            applyOwnerName(entity, ownerName)
        }
    }
    
    /**
     * Build context by collecting owner info from all entities.
     */
    fun buildContext(entities: List<T>): OwnerNameContext {
        val ownerInfos = entities.mapNotNull { entity ->
            val ownerId = extractOwnerId(entity)
            val ownerType = extractOwnerType(entity)
            if (ownerId != null && ownerType != null) {
                OwnerInfo.unresolved(ownerId, ownerType)
            } else null
        }
        
        return ownerNameResolver.resolveNames(ownerInfos)
    }
    
    /**
     * Convenience method: build context and enrich in one call.
     */
    fun enrichWithLookup(entities: List<T>): List<T> {
        val context = buildContext(entities)
        return enrich(entities, context)
    }
}
