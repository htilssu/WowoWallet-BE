package com.wowo.wowo.shared.enrichment

/**
 * Simple enrichment context that maps IDs to names.
 * Used for basic name lookups.
 * 
 * @param idNames Map of entity ID to display name
 */
data class IdNameContext(
    val idNames: Map<String, String?>
) : EnrichmentContext {
    
    companion object {
        fun empty() = IdNameContext(emptyMap())
    }
    
    /**
     * Get the name for the given ID.
     */
    fun getName(id: String?): String? = id?.let { idNames[it] }
}
