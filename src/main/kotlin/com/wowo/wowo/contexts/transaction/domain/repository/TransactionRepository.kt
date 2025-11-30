package com.wowo.wowo.contexts.transaction.domain.repository

import com.wowo.wowo.contexts.transaction.domain.entity.Transaction
import com.wowo.wowo.contexts.transaction.domain.valueobject.TransactionId
import com.wowo.wowo.contexts.transaction.domain.valueobject.TransactionStatus

/**
 * Repository interface for Transaction aggregate (Domain layer)
 */
interface TransactionRepository {
    fun save(transaction: Transaction): Transaction
    fun findById(id: TransactionId): Transaction?
    fun findByWalletId(walletId: String): List<Transaction>
    fun findByStatus(status: TransactionStatus): List<Transaction>
    fun delete(transaction: Transaction)
}

