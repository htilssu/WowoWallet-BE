package com.wowo.wowo.contexts.wallet.domain.entity

import com.wowo.wowo.shared.domain.AggregateRoot
import com.wowo.wowo.shared.valueobject.Currency
import com.wowo.wowo.shared.valueobject.Money
import com.wowo.wowo.contexts.wallet.domain.valueobject.WalletId
import com.wowo.wowo.contexts.wallet.domain.valueobject.Balance
import com.wowo.wowo.contexts.wallet.domain.event.WalletCreatedEvent
import com.wowo.wowo.contexts.wallet.domain.event.WalletCreditedEvent
import com.wowo.wowo.contexts.wallet.domain.event.WalletDebitedEvent
import com.wowo.wowo.shared.exception.InsufficientBalanceException
import com.wowo.wowo.shared.exception.InvalidOperationException
import java.time.LocalDateTime

/**
 * Wallet Aggregate Root
 */
class Wallet(
    override val id: WalletId,
    val userId: String,
    private var balance: Balance,
    val currency: Currency,
    var isActive: Boolean = true,
    override val createdAt: LocalDateTime = LocalDateTime.now(),
    override var updatedAt: LocalDateTime = LocalDateTime.now()
) : AggregateRoot<WalletId>() {

    fun credit(amount: Money) {
        validateActive()
        validateCurrency(amount.currency)

        balance = balance.add(amount)
        updatedAt = LocalDateTime.now()

        addDomainEvent(WalletCreditedEvent(id.toString(), userId, amount.amount, currency))
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
        updatedAt = LocalDateTime.now()

        addDomainEvent(WalletDebitedEvent(id.toString(), userId, amount.amount, currency))
    }

    fun freeze() {
        isActive = false
        updatedAt = LocalDateTime.now()
    }

    fun unfreeze() {
        isActive = true
        updatedAt = LocalDateTime.now()
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
        fun create(userId: String, currency: Currency): Wallet {
            val wallet = Wallet(
                id = WalletId(),
                userId = userId,
                balance = Balance.zero(currency),
                currency = currency
            )
            wallet.addDomainEvent(WalletCreatedEvent(wallet.id.toString(), userId, currency))
            return wallet
        }
    }
}

