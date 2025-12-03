package com.wowo.wowo.contexts.wallet.infrastructure.persistence

import com.wowo.wowo.contexts.wallet.domain.entity.Wallet
import com.wowo.wowo.contexts.wallet.domain.repository.WalletRepository
import com.wowo.wowo.contexts.wallet.domain.valueobject.Balance
import com.wowo.wowo.contexts.wallet.domain.valueobject.WalletId
import com.wowo.wowo.shared.valueobject.Currency
import com.wowo.wowo.shared.valueobject.Money
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

/**
 * Adapter implementing WalletRepository using JPA
 */
@Component
class WalletRepositoryAdapter(
    private val jpaRepository: WalletJpaRepository
) : WalletRepository {

    private val logger = LoggerFactory.getLogger(WalletRepositoryAdapter::class.java)

    override fun save(wallet: Wallet): Wallet {
        val jpaEntity = toJpaEntity(wallet)
        logger.debug("Saving WalletJpaEntity to DB for userId={}, currency={}", wallet.userId, wallet.currency)
        val savedEntity = jpaRepository.saveAndFlush(jpaEntity)
        logger.debug("Saved WalletJpaEntity.id={}", savedEntity.id)
        return toDomainEntity(savedEntity)
    }

    override fun findById(id: WalletId): Wallet? {
        return jpaRepository.findById(id.value).map { toDomainEntity(it) }.orElse(null)
    }

    override fun findByUserId(userId: String): List<Wallet> {
        return jpaRepository.findByUserId(userId).map { toDomainEntity(it) }
    }

    override fun existsByUserId(userId: String): Boolean {
        return jpaRepository.existsByUserId(userId)
    }

    override fun existsByUserIdAndCurrency(userId: String, currency: Currency): Boolean {
        return jpaRepository.existsByUserIdAndCurrency(userId, currency)
    }

    override fun delete(wallet: Wallet) {
        jpaRepository.deleteById(wallet.id.value)
    }

    private fun toJpaEntity(wallet: Wallet): WalletJpaEntity {
        return WalletJpaEntity(
            id = wallet.id.value,
            userId = wallet.userId,
            balance = wallet.getBalance().money.amount,
            currency = wallet.currency,
            isActive = wallet.isActive,
            createdAt = wallet.createdAt,
            updatedAt = wallet.updatedAt
        )
    }

    private fun toDomainEntity(jpaEntity: WalletJpaEntity): Wallet {
        return Wallet(
            id = WalletId(jpaEntity.id),
            userId = jpaEntity.userId,
            balance = Balance(Money(jpaEntity.balance, jpaEntity.currency)),
            currency = jpaEntity.currency,
            isActive = jpaEntity.isActive,
            createdAt = jpaEntity.createdAt,
            updatedAt = jpaEntity.updatedAt
        )
    }
}
