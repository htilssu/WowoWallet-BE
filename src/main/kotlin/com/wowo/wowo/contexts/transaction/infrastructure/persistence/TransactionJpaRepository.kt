package com.wowo.wowo.contexts.transaction.infrastructure.persistence

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.util.*

/**
 * Spring Data JPA Repository for Transaction
 */
@Repository
interface TransactionJpaRepository : JpaRepository<TransactionJpaEntity, UUID> {
    @Query("SELECT t FROM TransactionJpaEntity t WHERE t.fromWalletId = :walletId OR t.toWalletId = :walletId")
    fun findByWalletId(walletId: String): List<TransactionJpaEntity>

    fun findByStatus(status: String): List<TransactionJpaEntity>
}

