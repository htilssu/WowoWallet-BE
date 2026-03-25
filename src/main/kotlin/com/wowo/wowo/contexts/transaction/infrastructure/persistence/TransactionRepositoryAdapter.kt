package com.wowo.wowo.contexts.transaction.infrastructure.persistence

import com.wowo.wowo.contexts.transaction.domain.entity.Transaction
import com.wowo.wowo.contexts.transaction.domain.repository.TransactionRepository
import com.wowo.wowo.contexts.transaction.domain.repository.TransactionSearchCriteria
import com.wowo.wowo.contexts.transaction.domain.valueobject.TransactionId
import com.wowo.wowo.contexts.transaction.domain.valueobject.TransactionStatus
import com.wowo.wowo.contexts.transaction.domain.valueobject.TransactionType
import com.wowo.wowo.shared.domain.PagedResult
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
    private val jpaRepository: TransactionJpaRepository,
    private val persistenceMapper: TransactionPersistenceMapper
) : TransactionRepository {

    override fun save(transaction: Transaction): Transaction {
        val jpaEntity = persistenceMapper.toJpaEntity(transaction)
        val savedEntity = jpaRepository.save(jpaEntity)
        return persistenceMapper.toDomainEntity(savedEntity)
    }

    override fun findById(id: TransactionId): Transaction? {
        return jpaRepository.findById(id.value).map { persistenceMapper.toDomainEntity(it) }.orElse(null)
    }

    override fun findByWalletId(walletId: String): List<Transaction> {
        return jpaRepository.findByWalletId(walletId).map { persistenceMapper.toDomainEntity(it) }
    }

    override fun findByStatus(status: TransactionStatus): List<Transaction> {
        return jpaRepository.findByStatus(status).map { persistenceMapper.toDomainEntity(it) }
    }

    override fun search(criteria: TransactionSearchCriteria): PagedResult<Transaction> {
        val pageable = PageRequest.of(criteria.page, criteria.size, Sort.by(Sort.Direction.DESC, "createdAt"))
        
        val spec = Specification<TransactionJpaEntity> { root, _, cb ->
            val predicates = mutableListOf<Predicate>()
            
            // Wallet ID (either from or to)
            // Use explicit generic types to avoid ambiguity
            val sourceWallet = cb.equal(root.get<String>("sourceWalletId"), criteria.walletId)
            val targetWallet = cb.equal(root.get<String>("targetWalletId"), criteria.walletId)
            predicates.add(cb.or(sourceWallet, targetWallet))
            
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
            items = pagedEntities.content.map { persistenceMapper.toDomainEntity(it) },
            totalItems = pagedEntities.totalElements,
            totalPages = pagedEntities.totalPages,
            currentPage = pagedEntities.number,
            pageSize = pagedEntities.size
        )
    }

    override fun delete(transaction: Transaction) {

        jpaRepository.deleteById(transaction.id.value)
    }
}

