package com.wowo.wowo.contexts.wallet.application.usecase

import com.wowo.wowo.contexts.wallet.application.dto.CreateWalletCommand
import com.wowo.wowo.contexts.wallet.application.dto.WalletDTO
import com.wowo.wowo.contexts.wallet.domain.entity.Wallet
import com.wowo.wowo.contexts.wallet.domain.repository.WalletRepository
import com.wowo.wowo.shared.domain.DomainEventPublisher
import com.wowo.wowo.shared.valueobject.Currency
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

/**
 * Use Case: Create a new wallet
 */
@Service
@Transactional
class CreateWalletUseCase(
    private val walletRepository: WalletRepository,
    private val eventPublisher: DomainEventPublisher
) {
    fun execute(command: CreateWalletCommand): WalletDTO {
        val currency = Currency.valueOf(command.currency)

        val wallet = Wallet.create(command.userId, currency)

        val savedWallet = walletRepository.save(wallet)

        eventPublisher.publish(savedWallet.getDomainEvents())
        savedWallet.clearDomainEvents()

        return WalletDTO.fromDomain(savedWallet)
    }
}

