package com.wowo.wowo.contexts.wallet.application.usecase

import com.wowo.wowo.contexts.wallet.application.dto.CreditWalletCommand
import com.wowo.wowo.contexts.wallet.application.dto.WalletDTO
import com.wowo.wowo.contexts.wallet.domain.repository.WalletRepository
import com.wowo.wowo.contexts.wallet.domain.valueobject.WalletId
import com.wowo.wowo.shared.domain.DomainEventPublisher
import com.wowo.wowo.shared.exception.EntityNotFoundException
import com.wowo.wowo.shared.valueobject.Currency
import com.wowo.wowo.shared.valueobject.Money
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.math.BigDecimal

/**
 * Use Case: Credit (add money to) wallet
 */
@Service
@Transactional
class CreditWalletUseCase(
    private val walletRepository: WalletRepository,
    private val eventPublisher: DomainEventPublisher
) {
    fun execute(command: CreditWalletCommand): WalletDTO {
        val wallet = walletRepository.findById(WalletId.fromString(command.walletId))
            ?: throw EntityNotFoundException("Wallet not found with ID: ${command.walletId}")

        val amount = Money(BigDecimal(command.amount), Currency.valueOf(command.currency))
        wallet.credit(amount)

        val savedWallet = walletRepository.save(wallet)

        eventPublisher.publish(savedWallet.getDomainEvents())
        savedWallet.clearDomainEvents()

        return WalletDTO.fromDomain(savedWallet)
    }
}

