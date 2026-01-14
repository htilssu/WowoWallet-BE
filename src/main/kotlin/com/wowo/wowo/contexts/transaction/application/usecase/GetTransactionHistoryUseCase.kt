package com.wowo.wowo.contexts.transaction.application.usecase

import com.wowo.wowo.contexts.transaction.application.dto.TransactionDTO
import com.wowo.wowo.contexts.transaction.domain.repository.TransactionRepository
import com.wowo.wowo.contexts.transaction.domain.repository.TransactionSearchCriteria
import com.wowo.wowo.shared.domain.PagedResult
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class GetTransactionHistoryUseCase(
    private val transactionRepository: TransactionRepository
) {
    @Transactional(readOnly = true)
    fun execute(criteria: TransactionSearchCriteria): PagedResult<TransactionDTO> {
        val pagedTransactions = transactionRepository.search(criteria)
        
        return PagedResult(
            items = pagedTransactions.items.map { TransactionDTO.fromDomain(it) },
            totalItems = pagedTransactions.totalItems,
            totalPages = pagedTransactions.totalPages,
            currentPage = pagedTransactions.currentPage,
            pageSize = pagedTransactions.pageSize
        )
    }
}
