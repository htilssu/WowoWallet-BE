package com.wowo.wowo.contexts.transaction.domain.acl

import com.wowo.wowo.shared.valueobject.Money

/**
 * Anti-Corruption Layer for Wallet Context
 * This interface protects Transaction context from direct dependency on Wallet domain
 */
interface WalletACL {
    /**
     * Get a wallet by ID
     * @return wallet entity or null if not found
     */
    fun getWallet(walletId: String): Any?

    /**
     * Validate that a wallet exists and can perform operations
     */
    fun validateWalletExists(walletId: String): Boolean

    /**
     * Check if wallet has sufficient balance
     */
    fun hasSufficientBalance(walletId: String, amount: Money): Boolean

    fun transfer(fromWalletId: String, toWalletId: String, amount: Money)
}

