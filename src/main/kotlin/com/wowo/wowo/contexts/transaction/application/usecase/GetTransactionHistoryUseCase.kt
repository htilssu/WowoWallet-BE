package com.wowo.wowo.contexts.transaction.application.usecase

import com.wowo.wowo.contexts.transaction.application.dto.*
import com.wowo.wowo.contexts.transaction.application.enricher.*
import com.wowo.wowo.contexts.transaction.application.mapper.*
import com.wowo.wowo.contexts.transaction.domain.repository.*
import com.wowo.wowo.shared.domain.*
import com.wowo.wowo.shared.enrichment.Enricher
import org.springframework.stereotype.*


@Service
class GetTransactionHistoryUseCase(
    private val transactionRepository: TransactionRepository,
    private val transactionMapper: TransactionMapper,
    private val enricher: Enricher
) {
    fun execute(criteria: TransactionSearchCriteria): PagedResult<TransactionDTO> {
        val pagedTransactions = transactionRepository.search(criteria)

        val dtos = pagedTransactions.items.map { transaction ->
            transactionMapper.toDTO(transaction)
        }



        return PagedResult(
            items = listOf(),
            totalItems = pagedTransactions.totalItems,
            totalPages = pagedTransactions.totalPages,
            currentPage = pagedTransactions.currentPage,
            pageSize = pagedTransactions.pageSize
        )
    }
}
