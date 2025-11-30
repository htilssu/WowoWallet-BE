package com.wowo.wowo.contexts.transaction.infrastructure.persistence

import com.wowo.wowo.contexts.transaction.domain.entity.Transaction
import com.wowo.wowo.contexts.transaction.domain.repository.TransactionRepository
import com.wowo.wowo.contexts.transaction.domain.valueobject.TransactionId
import com.wowo.wowo.contexts.transaction.domain.valueobject.TransactionStatus
import com.wowo.wowo.contexts.transaction.domain.valueobject.TransactionType
import com.wowo.wowo.shared.valueobject.Currency
import com.wowo.wowo.shared.valueobject.Money
import org.springframework.stereotype.Component

/**
 * Adapter implementing TransactionRepository using JPA
 */
@Component
class TransactionRepositoryAdapter(
    private val jpaRepository: TransactionJpaRepository
) : TransactionRepository {

    override fun save(transaction: Transaction): Transaction {
        val jpaEntity = toJpaEntity(transaction)
        val savedEntity = jpaRepository.save(jpaEntity)
        return toDomainEntity(savedEntity)
    }

    override fun findById(id: TransactionId): Transaction? {
        return jpaRepository.findById(id.value).map { toDomainEntity(it) }.orElse(null)
    }

    override fun findByWalletId(walletId: String): List<Transaction> {
        return jpaRepository.findByWalletId(walletId).map { toDomainEntity(it) }
    }

    override fun findByStatus(status: TransactionStatus): List<Transaction> {
        return jpaRepository.findByStatus(status.name).map { toDomainEntity(it) }
    }

    override fun delete(transaction: Transaction) {
        jpaRepository.deleteById(transaction.id.value)
    }

    private fun toJpaEntity(transaction: Transaction): TransactionJpaEntity {
        return TransactionJpaEntity(
            id = transaction.id.value,
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

    private fun toDomainEntity(jpaEntity: TransactionJpaEntity): Transaction {
        val currency = Currency.valueOf(jpaEntity.currency)
        return Transaction(
            id = TransactionId(jpaEntity.id),
            fromWalletId = jpaEntity.fromWalletId,
            toWalletId = jpaEntity.toWalletId,
            amount = Money(jpaEntity.amount, currency),
            type = TransactionType.valueOf(jpaEntity.type),
            status = TransactionStatus.valueOf(jpaEntity.status),
            description = jpaEntity.description,
            reference = jpaEntity.reference,
            createdAt = jpaEntity.createdAt,
            updatedAt = jpaEntity.updatedAt
        )
    }
}

