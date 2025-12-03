package com.wowo.wowo.contexts.transaction.domain.acl

import com.wowo.wowo.shared.valueobject.Money

/**
 * Anti-Corruption Layer for Wallet Context
 * This interface protects Transaction context from direct dependency on Wallet domain
 */
interface WalletACL {
    /**
     * Validate that a wallet exists and can perform operations
     */
    fun validateWalletExists(walletId: String): Boolean

    /**
     * Perform debit operation on a wallet
     * @throws EntityNotFoundException if wallet not found
     * @throws InsufficientBalanceException if balance insufficient
     */
    fun debitWallet(walletId: String, amount: Money)

    /**
     * Perform credit operation on a wallet
     * @throws EntityNotFoundException if wallet not found
     */
    fun creditWallet(walletId: String, amount: Money)

    /**
     * Check if wallet has sufficient balance
     */
    fun hasSufficientBalance(walletId: String, amount: Money): Boolean
}

