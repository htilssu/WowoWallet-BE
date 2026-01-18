package com.wowo.wowo.contexts.transaction.infrastructure.acl

import com.wowo.wowo.contexts.transaction.domain.acl.WalletACL
import com.wowo.wowo.contexts.wallet.domain.repository.WalletRepository
import com.wowo.wowo.contexts.wallet.domain.valueobject.WalletId
import com.wowo.wowo.shared.exception.EntityNotFoundException
import com.wowo.wowo.shared.valueobject.Money
import org.springframework.stereotype.Component

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

    override fun validateWalletExists(walletId: String): Boolean {
        return getWallet(walletId) != null
    }

    override fun hasSufficientBalance(walletId: String, amount: Money): Boolean {
        val wallet = walletRepository.findById(WalletId.fromString(walletId))
            ?: return false

        return wallet.getBalance().money.amount >= amount.amount
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
}

