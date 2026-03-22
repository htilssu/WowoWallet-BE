package com.wowo.wowo.shared.enrichment

import org.springframework.stereotype.Service

@Service
class Enricher(val enrichList: List<IEnricher<*>>) {

    @Suppress("UNCHECKED_CAST")
    fun <T> enrich(data: T): T {
        var enrichedData = data
        enrichList.forEach { enrich ->
            enrichedData = (enrich as IEnricher<T>).enrich(enrichedData)
        }
        return enrichedData
    }

}