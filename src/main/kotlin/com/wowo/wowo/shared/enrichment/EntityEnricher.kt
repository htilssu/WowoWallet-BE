package com.wowo.wowo.shared.enrichment

/**
 * Interface for enriching entities with additional data.
 * 
 * @param T The entity type to enrich
 * @param C The enrichment context type
 */
interface EntityEnricher<T, C : EnrichmentContext> {
    
    /**
     * Enrich a list of entities using the provided context.
     * 
     * @param entities The entities to enrich
     * @param context The enrichment context containing lookup data
     * @return List of enriched entities
     */
    fun enrich(entities: List<T>, context: C): List<T>
    
    /**
     * Enrich a single entity using the provided context.
     * 
     * @param entity The entity to enrich
     * @param context The enrichment context containing lookup data
     * @return The enriched entity
     */
    fun enrichSingle(entity: T, context: C): T = enrich(listOf(entity), context).first()
}
