package com.wowo.wowo.shared.enrichment

interface IEnricher<T> {

    fun enrich(data: T) : T
}