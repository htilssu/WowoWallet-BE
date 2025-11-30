package com.wowo.wowo.contexts.wallet.domain.repository

import com.wowo.wowo.contexts.wallet.domain.entity.Wallet
import com.wowo.wowo.contexts.wallet.domain.valueobject.WalletId

/**
 * Repository interface for Wallet aggregate (Domain layer)
 */
interface WalletRepository {
    fun save(wallet: Wallet): Wallet
    fun findById(id: WalletId): Wallet?
    fun findByUserId(userId: String): List<Wallet>
    fun existsByUserId(userId: String): Boolean
    fun delete(wallet: Wallet)
}

