package com.wowo.wowo.shared.enrichment

import org.slf4j.*
import org.springframework.core.*
import org.springframework.stereotype.*

@Service
class Enricher(val enrichList: List<IEnricher<*>>) {
    private val logger = LoggerFactory.getLogger(this.javaClass)
    private val mapper: Map<Class<*>, IEnricher<*>> by lazy {
        enrichList.associateBy { enricher ->
            ResolvableType.forClass(enricher.javaClass).`as`(IEnricher::class.java).getGeneric(0).resolve()
                ?: Any::class.java
        }
    }


    @Suppress("UNCHECKED_CAST")
    fun <T> enrich(data: T): T {
        val enrichers: List<IEnricher<T>> =
            mapper.entries.filter { it.key.isAssignableFrom(data!!::class.java) }.map { it.value as IEnricher<T> }

        enrichers.forEach { enricher ->
            logger.debug("Enriching data of type: ${data!!::class.java.name} using enricher: ${enricher.javaClass.name}")
            try {
                enricher.enrich(data)
            } catch (e: Exception) {
                logger.error(
                    "Error enriching data of type: ${data!!::class.java.name} with enricher: ${enricher.javaClass.name}",
                    e
                )
            }
        }

        return data
    }
}