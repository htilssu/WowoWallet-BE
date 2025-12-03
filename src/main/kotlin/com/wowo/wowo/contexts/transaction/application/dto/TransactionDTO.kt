package com.wowo.wowo.contexts.transaction.application.dto

import com.wowo.wowo.contexts.transaction.domain.entity.Transaction
import java.math.BigDecimal
import java.time.LocalDateTime

/**
 * Data Transfer Object for Transaction
 */
data class TransactionDTO(
    val id: String,
    val fromWalletId: String?,
    val toWalletId: String?,
    val amount: BigDecimal,
    val currency: String,
    val type: String,
    val status: String,
    val description: String?,
    val reference: String?,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime
) {
    companion object {
        fun fromDomain(transaction: Transaction): TransactionDTO {
            return TransactionDTO(
                id = transaction.id.toString(),
                fromWalletId = transaction.fromWalletId,
                toWalletId = transaction.toWalletId,
                amount = transaction.amount.amount,
                currency = transaction.amount.currency.name,
                type = transaction.type.name,
                status = transaction.getStatus().name,
                description = transaction.description,
                reference = transaction.reference,
                createdAt = transaction.createdAt,
                updatedAt = transaction.updatedAt
            )
        }
    }
}

