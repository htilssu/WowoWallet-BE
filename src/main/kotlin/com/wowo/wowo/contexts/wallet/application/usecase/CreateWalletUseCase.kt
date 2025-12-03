package com.wowo.wowo.contexts.wallet.application.usecase

import com.wowo.wowo.contexts.wallet.application.dto.CreateWalletCommand
import com.wowo.wowo.contexts.wallet.application.dto.WalletDTO
import com.wowo.wowo.contexts.wallet.domain.entity.Wallet
import com.wowo.wowo.contexts.wallet.domain.repository.WalletRepository
import com.wowo.wowo.shared.domain.DomainEventPublisher
import com.wowo.wowo.shared.valueobject.Currency
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional

/**
 * Use Case: Create a new wallet
 */
@Service
@Transactional(propagation = Propagation.REQUIRES_NEW)
class CreateWalletUseCase(
    private val walletRepository: WalletRepository,
    private val eventPublisher: DomainEventPublisher
) {
    private val logger = LoggerFactory.getLogger(CreateWalletUseCase::class.java)

    fun execute(command: CreateWalletCommand): WalletDTO {
        val currency = Currency.valueOf(command.currency)

        val wallet = Wallet.create(command.userId, currency)

        logger.debug("Creating wallet for user=${command.userId} with currency=${command.currency}")
        val savedWallet = walletRepository.save(wallet)
        logger.debug("Wallet saved with id={}", savedWallet.id)

        val events = wallet.getDomainEvents()
        logger.debug("Publishing ${events.size} domain events for wallet id=${wallet.id}")
        eventPublisher.publish(events)
        wallet.clearDomainEvents()

        return WalletDTO.fromDomain(savedWallet)
    }
}
