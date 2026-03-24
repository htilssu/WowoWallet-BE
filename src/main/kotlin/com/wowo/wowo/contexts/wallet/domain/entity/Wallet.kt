package com.wowo.wowo.contexts.wallet.domain.entity

import com.wowo.wowo.contexts.wallet.domain.event.*
import com.wowo.wowo.contexts.wallet.domain.valueobject.*
import com.wowo.wowo.shared.domain.*
import com.wowo.wowo.shared.exception.*
import com.wowo.wowo.shared.valueobject.*
import java.time.*

/**
 * Wallet Aggregate Root
 */
class Wallet(
    override val id: WalletId,
    val ownerId: String,
    val ownerType: OwnerType,
    private var balance: Balance,
    val currency: Currency,
    var isActive: Boolean = true,
    override val createdAt: Instant = Instant.now(),
    override var updatedAt: Instant = Instant.now()
) : AggregateRoot<WalletId>() {

    fun credit(amount: Money) {
        validateActive()
        validateCurrency(amount.currency)

        balance = balance.add(amount)
        updatedAt = Instant.now()

        addDomainEvent(WalletCreditedEvent(id.toString(), ownerId, ownerType, amount.amount, currency))
    }

    fun debit(amount: Money) {
        validateActive()
        validateCurrency(amount.currency)

        if (balance.money.amount < amount.amount) {
            throw InsufficientBalanceException(
                "Insufficient balance. Available: ${balance.money.amount}, Required: ${amount.amount}"
            )
        }

        balance = balance.subtract(amount)
        updatedAt = Instant.now()

        addDomainEvent(WalletDebitedEvent(id.toString(), ownerId, ownerType, amount.amount, currency))
    }


    fun freeze() {
        isActive = false
        updatedAt = Instant.now()
    }

    fun unfreeze() {
        isActive = true
        updatedAt = Instant.now()
    }

    fun getBalance(): Balance = balance

    private fun validateActive() {
        if (!isActive) {
            throw InvalidOperationException("Wallet is not active")
        }
    }

    private fun validateCurrency(operationCurrency: Currency) {
        if (currency != operationCurrency) {
            throw InvalidOperationException(
                "Currency mismatch. Wallet currency: $currency, Operation currency: $operationCurrency"
            )
        }
    }

    companion object {
        fun create(ownerId: String, ownerType: OwnerType, currency: Currency): Wallet {
            val wallet = Wallet(
                id = WalletId(),
                ownerId = ownerId,
                ownerType = ownerType,
                balance = Balance.zero(currency),
                currency = currency
            )
            wallet.addDomainEvent(WalletCreatedEvent(wallet.id.toString(), ownerId, ownerType, currency))
            return wallet
        }
    }
}

