package com.wowo.wowo.contexts.wallet.infrastructure.persistence

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

/**
 * Spring Data JPA Repository for Wallet
 */
@Repository
interface WalletJpaRepository : JpaRepository<WalletJpaEntity, UUID> {
    fun findByUserId(userId: String): List<WalletJpaEntity>
    fun existsByUserId(userId: String): Boolean
    fun existsByUserIdAndCurrency(userId: String, currency: com.wowo.wowo.shared.valueobject.Currency): Boolean
}
