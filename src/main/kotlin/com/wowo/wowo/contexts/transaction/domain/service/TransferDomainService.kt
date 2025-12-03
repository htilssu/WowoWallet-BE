package com.wowo.wowo.contexts.transaction.domain.service

import com.wowo.wowo.contexts.transaction.domain.acl.WalletACL
import com.wowo.wowo.contexts.transaction.domain.entity.Transaction
import com.wowo.wowo.contexts.transaction.domain.valueobject.TransactionType
import com.wowo.wowo.shared.exception.EntityNotFoundException
import com.wowo.wowo.shared.exception.InsufficientBalanceException
import com.wowo.wowo.shared.valueobject.Money

/**
 * Domain Service for Transfer Operations
 * Encapsulates business rules and invariants for money transfers
 */
class TransferDomainService(
    private val walletACL: WalletACL
) {
    /**
     * Execute a transfer between two wallets
     * @return Transaction entity with transfer details
     * @throws EntityNotFoundException if either wallet not found
     * @throws InsufficientBalanceException if source wallet has insufficient balance
     */
    fun executeTransfer(
        fromWalletId: String,
        toWalletId: String,
        amount: Money,
        description: String?
    ): Transaction {
        // Validate wallets exist
        if (!walletACL.validateWalletExists(fromWalletId)) {
            throw EntityNotFoundException("Source wallet not found: $fromWalletId")
        }

        if (!walletACL.validateWalletExists(toWalletId)) {
            throw EntityNotFoundException("Destination wallet not found: $toWalletId")
        }

        // Create transaction
        val transaction = Transaction.create(
            fromWalletId = fromWalletId,
            toWalletId = toWalletId,
            amount = amount,
            type = TransactionType.TRANSFER,
            description = description
        )

        try {
            // Execute the transfer through ACL
            walletACL.debitWallet(fromWalletId, amount)
            walletACL.creditWallet(toWalletId, amount)

            // Mark transaction as complete
            transaction.complete()

        } catch (e: Exception) {
            // If any error occurs, mark transaction as failed
            if (e is InsufficientBalanceException) {
                throw e
            }
            transaction.fail(e.message ?: "Transfer failed")
            throw e
        }

        return transaction
    }

    /**
     * Validate if a transfer can be executed
     */
    fun canExecuteTransfer(fromWalletId: String, toWalletId: String, amount: Money): Boolean {
        return walletACL.validateWalletExists(fromWalletId) &&
               walletACL.validateWalletExists(toWalletId) &&
               walletACL.hasSufficientBalance(fromWalletId, amount)
    }
}

