package com.wowo.wowo.contexts.transaction.application.usecase

import com.wowo.wowo.contexts.transaction.application.dto.*
import com.wowo.wowo.contexts.transaction.application.enricher.*
import com.wowo.wowo.contexts.transaction.application.mapper.*
import com.wowo.wowo.contexts.transaction.domain.repository.*
import com.wowo.wowo.shared.domain.*
import org.springframework.stereotype.*


@Service
class GetTransactionHistoryUseCase(
    private val transactionRepository: TransactionRepository,
    private val transactionMapper: TransactionMapper,
    private val transactionOwnerEnricher: TransactionOwnerEnricher,
    private val transactionEnrichmentService: TransactionEnrichmentService,
    private val transactionEnricher: TransactionEnricher
) {
    fun execute(criteria: TransactionSearchCriteria): PagedResult<TransactionDTO> {
        val pagedTransactions = transactionRepository.search(criteria)

        val dtos = pagedTransactions.items.map { transaction ->
            transactionMapper.toDTO(transaction)
        }

        // Enrich with owner names (sender and receiver)
        val ownerEnrichedDtos = transactionOwnerEnricher.enrichWithLookup(dtos)
        val enrichmentContext = transactionEnrichmentService.buildContext(ownerEnrichedDtos)
        val enrichedDtos = transactionEnricher.enrich(ownerEnrichedDtos, enrichmentContext)

        return PagedResult(
            items = enrichedDtos,
            totalItems = pagedTransactions.totalItems,
            totalPages = pagedTransactions.totalPages,
            currentPage = pagedTransactions.currentPage,
            pageSize = pagedTransactions.pageSize
        )
    }
}
