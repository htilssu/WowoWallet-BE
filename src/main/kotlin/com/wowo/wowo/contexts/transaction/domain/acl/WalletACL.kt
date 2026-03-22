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
     * Get owner IDs for multiple wallets
     * @return Map of walletId -> ownerId (ownerId is null if wallet not found)
     */
    fun getWalletOwners(walletIds: Set<String>): Map<String, String?>
    fun transfer(fromWalletId: String, toWalletId: String, amount: Money)
}

