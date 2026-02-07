package com.wowo.wowo.shared.enrichment

import com.wowo.wowo.shared.domain.OwnerType

/**
 * Represents an owner reference that needs name resolution.
 * Used for multi-owner enrichment scenarios.
 */
data class OwnerReference(
    val fieldKey: String,     // e.g., "sender", "receiver"
    val ownerId: String?,
    val ownerType: OwnerType?
)

/**
 * Abstract base enricher for entities with multiple owners.
 * 
 * Use this for entities like Transaction that have sender and receiver,
 * or any entity with multiple owner references.
 * 
 * @param T The DTO type to enrich
 */
abstract class MultiOwnerEnricher<T>(
    private val ownerNameResolver: OwnerNameResolver
) : EntityEnricher<T, OwnerNameContext> {
    
    /**
     * Extract all owner references from the entity.
     * Each reference has a key (e.g., "sender", "receiver") for identification.
     */
    abstract fun extractOwnerReferences(entity: T): List<OwnerReference>
    
    /**
     * Apply resolved owner names to the entity.
     * 
     * @param entity The entity to enrich
     * @param resolvedNames Map of fieldKey to resolved owner name
     */
    abstract fun applyOwnerNames(entity: T, resolvedNames: Map<String, String?>): T
    
    override fun enrich(entities: List<T>, context: OwnerNameContext): List<T> {
        return entities.map { entity ->
            val references = extractOwnerReferences(entity)
            val resolvedNames = references.associate { ref ->
                ref.fieldKey to context.getOwnerName(ref.ownerId, ref.ownerType)
            }
            applyOwnerNames(entity, resolvedNames)
        }
    }
    
    /**
     * Build context by collecting all owner references from all entities.
     */
    fun buildContext(entities: List<T>): OwnerNameContext {
        val ownerInfos = entities.flatMap { entity ->
            extractOwnerReferences(entity).mapNotNull { ref ->
                if (ref.ownerId != null && ref.ownerType != null) {
                    OwnerInfo.unresolved(ref.ownerId, ref.ownerType)
                } else null
            }
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
