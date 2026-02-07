package com.wowo.wowo.contexts.transaction.application.usecase

import com.wowo.wowo.contexts.transaction.application.dto.TransactionDTO
import com.wowo.wowo.contexts.transaction.application.enricher.TransactionOwnerEnricher
import com.wowo.wowo.contexts.transaction.application.mapper.TransactionMapper
import com.wowo.wowo.contexts.transaction.domain.repository.TransactionRepository
import com.wowo.wowo.contexts.transaction.domain.repository.TransactionSearchCriteria
import com.wowo.wowo.shared.domain.PagedResult
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class GetTransactionHistoryUseCase(
    private val transactionRepository: TransactionRepository,
    private val transactionMapper: TransactionMapper,
    private val transactionOwnerEnricher: TransactionOwnerEnricher
) {
    @Transactional(readOnly = true)
    fun execute(criteria: TransactionSearchCriteria): PagedResult<TransactionDTO> {
        val pagedTransactions = transactionRepository.search(criteria)

        val dtos = pagedTransactions.items.map { transaction ->
            transactionMapper.toDTO(transaction)
        }

        // Enrich with owner names (sender and receiver)
        val enrichedDtos = transactionOwnerEnricher.enrichWithLookup(dtos)

        return PagedResult(
            items = enrichedDtos,
            totalItems = pagedTransactions.totalItems,
            totalPages = pagedTransactions.totalPages,
            currentPage = pagedTransactions.currentPage,
            pageSize = pagedTransactions.pageSize
        )
    }
}

