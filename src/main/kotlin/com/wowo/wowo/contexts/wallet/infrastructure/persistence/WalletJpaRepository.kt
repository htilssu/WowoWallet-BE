package com.wowo.wowo.contexts.wallet.infrastructure.persistence

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*
import com.wowo.wowo.shared.domain.OwnerType

/**
 * Spring Data JPA Repository for Wallet
 */
@Repository
interface WalletJpaRepository : JpaRepository<WalletJpaEntity, UUID> {
    fun findByOwnerIdAndOwnerType(ownerId: String, ownerType: OwnerType): List<WalletJpaEntity>
    fun existsByOwnerIdAndOwnerType(ownerId: String, ownerType: OwnerType): Boolean
    fun existsByOwnerIdAndOwnerTypeAndCurrency(ownerId: String, ownerType: OwnerType, currency: com.wowo.wowo.shared.valueobject.Currency): Boolean
}

