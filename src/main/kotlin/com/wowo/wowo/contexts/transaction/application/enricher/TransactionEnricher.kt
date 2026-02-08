package com.wowo.wowo.contexts.transaction.application.enricher

import com.wowo.wowo.contexts.transaction.application.dto.TransactionDTO
import com.wowo.wowo.shared.enrichment.EntityEnricher
import org.springframework.stereotype.Component

@Component
class TransactionEnricher : EntityEnricher<TransactionDTO, TransactionEnrichmentContext> {
    override fun enrich(
        entities: List<TransactionDTO>,
        context: TransactionEnrichmentContext
    ): List<TransactionDTO> {
        return entities.map { dto ->
            dto.copy(
                normalizedDescription = context.normalizedDescriptions[dto.id],
                transactionCategory = context.categories[dto.id],
                merchantName = context.merchantNames[dto.id],
                merchantCategory = context.merchantCategories[dto.id],
                geoCountry = context.geoCountries[dto.id],
                geoCity = context.geoCities[dto.id],
                riskScore = context.riskScores[dto.id],
                riskLevel = context.riskLevels[dto.id],
                tags = context.tags[dto.id] ?: emptyList()
            )
        }
    }
}
