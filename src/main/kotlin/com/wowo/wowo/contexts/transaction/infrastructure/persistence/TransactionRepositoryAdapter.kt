package com.wowo.wowo.contexts.transaction.infrastructure.persistence

import com.wowo.wowo.contexts.transaction.domain.entity.Transaction
import com.wowo.wowo.contexts.transaction.domain.repository.TransactionRepository
import com.wowo.wowo.contexts.transaction.domain.repository.TransactionSearchCriteria
import com.wowo.wowo.contexts.transaction.domain.valueobject.TransactionId
import com.wowo.wowo.contexts.transaction.domain.valueobject.TransactionStatus
import com.wowo.wowo.contexts.transaction.domain.valueobject.TransactionType
import com.wowo.wowo.shared.domain.PagedResult
import com.wowo.wowo.shared.valueobject.Currency
import com.wowo.wowo.shared.valueobject.Money
import jakarta.persistence.criteria.Predicate
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.data.jpa.domain.Specification
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
        return jpaRepository.findByStatus(status).map { toDomainEntity(it) }
    }

    override fun search(criteria: TransactionSearchCriteria): PagedResult<Transaction> {
        val pageable = PageRequest.of(criteria.page, criteria.size, Sort.by(Sort.Direction.DESC, "createdAt"))
        
        val spec = Specification<TransactionJpaEntity> { root, _, cb ->
            val predicates = mutableListOf<Predicate>()
            
            // Wallet ID (either from or to)
            // Use explicit generic types to avoid ambiguity
            val fromWallet = cb.equal(root.get<String>("fromWalletId"), criteria.walletId)
            val toWallet = cb.equal(root.get<String>("toWalletId"), criteria.walletId)
            predicates.add(cb.or(fromWallet, toWallet))
            
            // Date Range
            criteria.startDate?.let {
                predicates.add(cb.greaterThanOrEqualTo(root.get("createdAt"), it))
            }
            criteria.endDate?.let {
                predicates.add(cb.lessThanOrEqualTo(root.get("createdAt"), it))
            }
            
            // Type
            criteria.type?.let {
                predicates.add(cb.equal(root.get<TransactionType>("type"), it))
            }
            
            cb.and(*predicates.toTypedArray())
        }
        
        val pagedEntities = jpaRepository.findAll(spec, pageable)
        
        return PagedResult(
            items = pagedEntities.content.map { toDomainEntity(it) },
            totalItems = pagedEntities.totalElements,
            totalPages = pagedEntities.totalPages,
            currentPage = pagedEntities.number,
            pageSize = pagedEntities.size
        )
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
            type = transaction.type,
            status = transaction.getStatus(),
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
            type = jpaEntity.type,
            status = jpaEntity.status,
            description = jpaEntity.description,
            reference = jpaEntity.reference,
            createdAt = jpaEntity.createdAt,
            updatedAt = jpaEntity.updatedAt
        )
    }
}

