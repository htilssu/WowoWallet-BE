package com.wowo.wowo.contexts.wallet.domain.repository

import com.wowo.wowo.contexts.wallet.domain.entity.Wallet
import com.wowo.wowo.contexts.wallet.domain.valueobject.WalletId
import com.wowo.wowo.shared.valueobject.Currency
import com.wowo.wowo.contexts.wallet.domain.valueobject.OwnerType

/**
 * Repository interface for Wallet aggregate (Domain layer)
 */
interface WalletRepository {
    fun save(wallet: Wallet): Wallet
    fun findById(id: WalletId): Wallet?
    fun findByOwnerIdAndOwnerType(ownerId: String, ownerType: OwnerType): List<Wallet>
    fun existsByOwnerIdAndOwnerType(ownerId: String, ownerType: OwnerType): Boolean
    fun existsByOwnerIdAndOwnerTypeAndCurrency(ownerId: String, ownerType: OwnerType, currency: Currency): Boolean
    fun delete(wallet: Wallet)
}

