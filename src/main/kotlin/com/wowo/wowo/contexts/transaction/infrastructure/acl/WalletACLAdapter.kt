package com.wowo.wowo.contexts.transaction.infrastructure.acl

import com.wowo.wowo.contexts.transaction.domain.acl.*
import com.wowo.wowo.contexts.wallet.domain.repository.*
import com.wowo.wowo.contexts.wallet.domain.valueobject.*
import com.wowo.wowo.shared.domain.*
import com.wowo.wowo.shared.exception.*
import com.wowo.wowo.shared.valueobject.*
import org.springframework.stereotype.*

/**
 * Implementation of WalletACL using Wallet Repository
 * This adapter sits in infrastructure layer and bridges to Wallet context
 */
@Component
class WalletACLAdapter(
    private val walletRepository: WalletRepository
) : WalletACL {

    override fun getWallet(walletId: String): Any? {
        return walletRepository.findById(WalletId.fromString(walletId))
    }

    override fun getWalletOwners(walletIds: Set<String>): Map<String, String?> {
        return walletIds.associateWith { walletId ->
            walletRepository.findById(WalletId.fromString(walletId))?.ownerId
        }
    }

    override fun transfer(
        fromWalletId: String, toWalletId: String, amount: Money
    ) {
        val fromWallet = walletRepository.findById(WalletId.fromString(fromWalletId))
            ?: throw EntityNotFoundException("Source wallet not found: $fromWalletId")

        val toWallet = walletRepository.findById(WalletId.fromString(toWalletId))
            ?: throw EntityNotFoundException("Destination wallet not found: $toWalletId")

        fromWallet.debit(amount)
        toWallet.credit(amount)

        walletRepository.save(fromWallet)
        walletRepository.save(toWallet)
    }
    
    /**
     * Convert wallet's OwnerType to shared domain OwnerType.
     */
    private fun OwnerType.toSharedOwnerType(): OwnerType = this
}

