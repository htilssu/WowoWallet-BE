package com.wowo.wowo.contexts.transaction.application.usecase

import com.wowo.wowo.contexts.transaction.application.dto.TransferMoneyCommand
import com.wowo.wowo.contexts.transaction.application.dto.TransactionDTO
import com.wowo.wowo.contexts.transaction.domain.repository.TransactionRepository
import com.wowo.wowo.contexts.transaction.domain.service.TransferDomainService
import com.wowo.wowo.shared.domain.DomainEventPublisher
import com.wowo.wowo.shared.valueobject.Currency
import com.wowo.wowo.shared.valueobject.Money
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Isolation
import org.springframework.transaction.annotation.Transactional
import java.math.BigDecimal

/**
 * Use Case: Transfer money between wallets
 * Orchestrates the transfer operation using domain service
 */
@Service
@Transactional(isolation = Isolation.REPEATABLE_READ)
class TransferMoneyUseCase(
    private val transactionRepository: TransactionRepository,
    private val transferDomainService: TransferDomainService,
    private val eventPublisher: DomainEventPublisher
) {
    fun execute(command: TransferMoneyCommand): TransactionDTO {
        // Parse command
        val amount = Money(BigDecimal(command.amount), Currency.valueOf(command.currency))

        // Execute transfer through domain service
        val transaction = transferDomainService.executeTransfer(
            fromWalletId = command.fromWalletId,
            toWalletId = command.toWalletId,
            amount = amount,
            description = command.description
        )

        // Persist transaction
        val savedTransaction = transactionRepository.save(transaction)

        // Publish domain events
        eventPublisher.publish(savedTransaction.getDomainEvents())
        savedTransaction.clearDomainEvents()

        return TransactionDTO.fromDomain(savedTransaction)
    }
}
