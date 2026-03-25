package com.wowo.wowo.shared.enrichment

interface IEnricher<T> {

    fun enrich(data: T): T
    suspend fun enrichMany(data: List<T>): List<T>
}