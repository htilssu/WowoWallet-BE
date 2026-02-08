package com.wowo.wowo.contexts.transaction.application.enricher

import com.wowo.wowo.shared.enrichment.EnrichmentContext

data class TransactionEnrichmentContext(
    val normalizedDescriptions: Map<String, String?>,
    val categories: Map<String, String?>,
    val merchantNames: Map<String, String?>,
    val merchantCategories: Map<String, String?>,
    val geoCountries: Map<String, String?>,
    val geoCities: Map<String, String?>,
    val riskScores: Map<String, Int?>,
    val riskLevels: Map<String, String?>,
    val tags: Map<String, List<String>>
) : EnrichmentContext {
    companion object {
        fun empty(): TransactionEnrichmentContext = TransactionEnrichmentContext(
            normalizedDescriptions = emptyMap(),
            categories = emptyMap(),
            merchantNames = emptyMap(),
            merchantCategories = emptyMap(),
            geoCountries = emptyMap(),
            geoCities = emptyMap(),
            riskScores = emptyMap(),
            riskLevels = emptyMap(),
            tags = emptyMap()
        )
    }
}
