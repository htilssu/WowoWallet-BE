package com.wowo.wowo.contexts.transaction.application.usecase

import com.wowo.wowo.contexts.transaction.application.dto.*
import com.wowo.wowo.contexts.transaction.application.mapper.*
import com.wowo.wowo.contexts.transaction.domain.repository.*
import com.wowo.wowo.contexts.transaction.domain.service.*
import com.wowo.wowo.shared.domain.*
import com.wowo.wowo.shared.valueobject.*
import org.springframework.stereotype.*
import org.springframework.transaction.annotation.*
import java.math.*

/**
 * Use Case: Transfer money between wallets
 * Orchestrates the transfer operation using domain service
 */
@Service
@Transactional(isolation = Isolation.REPEATABLE_READ)
class TransferMoneyUseCase(
    private val transactionRepository: TransactionRepository,
    private val transferDomainService: TransferDomainService,
    private val eventPublisher: DomainEventPublisher,
    private val transactionMapper: TransactionMapper,
) {
    fun execute(command: TransferMoneyCommand): TransactionDTO { // Parse command
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


        val dto = transactionMapper.toDTO(savedTransaction)

        return dto
    }
}
