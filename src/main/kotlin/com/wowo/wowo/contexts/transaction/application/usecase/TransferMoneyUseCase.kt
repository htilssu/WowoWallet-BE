package com.wowo.wowo.contexts.transaction.application.usecase

import com.wowo.wowo.contexts.transaction.application.dto.TransferMoneyCommand
import com.wowo.wowo.contexts.transaction.application.dto.TransactionDTO
import com.wowo.wowo.contexts.transaction.domain.entity.Transaction
import com.wowo.wowo.contexts.transaction.domain.repository.TransactionRepository
import com.wowo.wowo.contexts.transaction.domain.valueobject.TransactionType
import com.wowo.wowo.contexts.wallet.domain.repository.WalletRepository
import com.wowo.wowo.contexts.wallet.domain.valueobject.WalletId
import com.wowo.wowo.shared.domain.DomainEventPublisher
import com.wowo.wowo.shared.exception.EntityNotFoundException
import com.wowo.wowo.shared.exception.InsufficientBalanceException
import com.wowo.wowo.shared.valueobject.Currency
import com.wowo.wowo.shared.valueobject.Money
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.math.BigDecimal

/**
 * Use Case: Transfer money between wallets
 */
@Service
@Transactional
class TransferMoneyUseCase(
    private val transactionRepository: TransactionRepository,
    private val walletRepository: WalletRepository,
    private val eventPublisher: DomainEventPublisher
) {
    fun execute(command: TransferMoneyCommand): TransactionDTO {
        // Validate wallets exist
        val fromWallet = walletRepository.findById(WalletId.fromString(command.fromWalletId))
            ?: throw EntityNotFoundException("Source wallet not found: ${command.fromWalletId}")

        val toWallet = walletRepository.findById(WalletId.fromString(command.toWalletId))
            ?: throw EntityNotFoundException("Destination wallet not found: ${command.toWalletId}")

        // Create transaction
        val amount = Money(BigDecimal(command.amount), Currency.valueOf(command.currency))
        val transaction = Transaction.create(
            fromWalletId = command.fromWalletId,
            toWalletId = command.toWalletId,
            amount = amount,
            type = TransactionType.TRANSFER,
            description = command.description
        )

        try {
            fromWallet.debit(amount)
            walletRepository.save(fromWallet)

            toWallet.credit(amount)
            walletRepository.save(toWallet)

            transaction.complete()

        } catch (e: Exception) {
            if (e is InsufficientBalanceException) {
                throw e
            }

            transaction.fail(e.message ?: "Transfer failed")
        }

        val savedTransaction = transactionRepository.save(transaction)

        // Publish events
        eventPublisher.publish(fromWallet.getDomainEvents())
        eventPublisher.publish(toWallet.getDomainEvents())
        eventPublisher.publish(savedTransaction.getDomainEvents())

        fromWallet.clearDomainEvents()
        toWallet.clearDomainEvents()
        savedTransaction.clearDomainEvents()

        return TransactionDTO.fromDomain(savedTransaction)
    }
}
