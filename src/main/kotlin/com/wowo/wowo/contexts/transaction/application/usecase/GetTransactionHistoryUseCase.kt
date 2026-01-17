package com.wowo.wowo.contexts.transaction.application.usecase

import com.wowo.wowo.contexts.transaction.application.dto.TransactionDTO
import com.wowo.wowo.contexts.transaction.domain.repository.TransactionRepository
import com.wowo.wowo.contexts.transaction.domain.repository.TransactionSearchCriteria
import com.wowo.wowo.contexts.transaction.domain.acl.WalletACL
import com.wowo.wowo.contexts.transaction.domain.acl.UserACL
import com.wowo.wowo.shared.domain.PagedResult
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class GetTransactionHistoryUseCase(
    private val transactionRepository: TransactionRepository,
    private val walletACL: WalletACL,
    private val userACL: UserACL
) {
    @Transactional(readOnly = true)
    fun execute(criteria: TransactionSearchCriteria): PagedResult<TransactionDTO> {
        val pagedTransactions = transactionRepository.search(criteria)
        
        return PagedResult(
            items = pagedTransactions.items.map { transaction ->
                val toWalletName = transaction.toWalletId?.let { 
                    val ownerId = walletACL.getWalletOwner(it)
                    ownerId?.let { id -> userACL.getUserName(id) }
                }

                val fromWalletName = transaction.fromWalletId?.let {
                    val ownerId = walletACL.getWalletOwner(it)
                    ownerId?.let { id -> userACL.getUserName(id) }
                }

                TransactionDTO.fromDomain(transaction, fromWalletName, toWalletName)
            },
            totalItems = pagedTransactions.totalItems,
            totalPages = pagedTransactions.totalPages,
            currentPage = pagedTransactions.currentPage,
            pageSize = pagedTransactions.pageSize
        )
    }
}
