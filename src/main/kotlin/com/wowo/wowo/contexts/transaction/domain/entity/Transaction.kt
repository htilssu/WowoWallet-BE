package com.wowo.wowo.contexts.transaction.domain.entity

import com.wowo.wowo.shared.domain.AggregateRoot
import com.wowo.wowo.shared.valueobject.Money
import com.wowo.wowo.contexts.transaction.domain.valueobject.TransactionId
import com.wowo.wowo.contexts.transaction.domain.valueobject.TransactionType
import com.wowo.wowo.contexts.transaction.domain.valueobject.TransactionStatus
import com.wowo.wowo.contexts.transaction.domain.event.TransactionCompletedEvent
import com.wowo.wowo.contexts.transaction.domain.event.TransactionFailedEvent
import com.wowo.wowo.shared.exception.InvalidOperationException
import java.time.LocalDateTime

/**
 * Transaction Aggregate Root
 */
class Transaction(
    override val id: TransactionId,
    val fromWalletId: String?,
    val toWalletId: String?,
    val amount: Money,
    val type: TransactionType,
    private var status: TransactionStatus = TransactionStatus.PENDING,
    val description: String?,
    val reference: String?,
    override val createdAt: LocalDateTime = LocalDateTime.now(),
    override var updatedAt: LocalDateTime = LocalDateTime.now()
) : AggregateRoot<TransactionId>() {

    init {
        validateTransaction()
    }

    fun complete() {
        if (status != TransactionStatus.PENDING && status != TransactionStatus.PROCESSING) {
            throw InvalidOperationException("Can only complete pending or processing transactions")
        }

        status = TransactionStatus.COMPLETED
        updatedAt = LocalDateTime.now()

        addDomainEvent(
            TransactionCompletedEvent(
                id.toString(),
                fromWalletId,
                toWalletId,
                amount.amount,
                amount.currency
            )
        )
    }

    fun fail(reason: String) {
        if (status == TransactionStatus.COMPLETED) {
            throw InvalidOperationException("Cannot fail a completed transaction")
        }

        status = TransactionStatus.FAILED
        updatedAt = LocalDateTime.now()

        addDomainEvent(TransactionFailedEvent(id.toString(), reason))
    }

    fun cancel() {
        if (status == TransactionStatus.COMPLETED) {
            throw InvalidOperationException("Cannot cancel a completed transaction")
        }

        status = TransactionStatus.CANCELLED
        updatedAt = LocalDateTime.now()
    }

    fun getStatus(): TransactionStatus = status

    private fun validateTransaction() {
        when (type) {
            TransactionType.CREDIT -> {
                require(toWalletId != null) { "Credit transaction must have a destination wallet" }
            }
            TransactionType.DEBIT -> {
                require(fromWalletId != null) { "Debit transaction must have a source wallet" }
            }
            TransactionType.TRANSFER -> {
                require(fromWalletId != null && toWalletId != null) {
                    "Transfer transaction must have both source and destination wallets"
                }
                require(fromWalletId != toWalletId) {
                    "Cannot transfer to the same wallet"
                }
            }
            else -> {}
        }
    }

    companion object {
        fun create(
            fromWalletId: String?,
            toWalletId: String?,
            amount: Money,
            type: TransactionType,
            description: String? = null,
            reference: String? = null
        ): Transaction {
            return Transaction(
                id = TransactionId(),
                fromWalletId = fromWalletId,
                toWalletId = toWalletId,
                amount = amount,
                type = type,
                description = description,
                reference = reference
            )
        }
    }
}

