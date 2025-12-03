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

    override fun validateWalletExists(walletId: String): Boolean {
        return walletRepository.findById(WalletId.fromString(walletId)) != null
    }

    override fun debitWallet(walletId: String, amount: Money) {
        val wallet = walletRepository.findById(WalletId.fromString(walletId))
            ?: throw EntityNotFoundException("Wallet not found: $walletId")

        wallet.debit(amount)
        walletRepository.save(wallet)
    }

    override fun creditWallet(walletId: String, amount: Money) {
        val wallet = walletRepository.findById(WalletId.fromString(walletId))
            ?: throw EntityNotFoundException("Wallet not found: $walletId")

        wallet.credit(amount)
        walletRepository.save(wallet)
    }

    override fun hasSufficientBalance(walletId: String, amount: Money): Boolean {
        val wallet = walletRepository.findById(WalletId.fromString(walletId))
            ?: return false

        return wallet.getBalance().money.amount >= amount.amount
    }
}

