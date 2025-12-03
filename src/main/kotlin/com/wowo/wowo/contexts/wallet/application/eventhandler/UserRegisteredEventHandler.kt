package com.wowo.wowo.contexts.wallet.application.eventhandler

import com.wowo.wowo.contexts.user.domain.event.UserRegisteredEvent
import com.wowo.wowo.contexts.wallet.application.dto.CreateWalletCommand
import com.wowo.wowo.contexts.wallet.application.usecase.CreateWalletUseCase
import com.wowo.wowo.contexts.wallet.domain.repository.WalletRepository
import com.wowo.wowo.shared.valueobject.Currency
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.springframework.transaction.PlatformTransactionManager
import org.springframework.transaction.TransactionDefinition
import org.springframework.transaction.event.TransactionPhase
import org.springframework.transaction.event.TransactionalEventListener
import org.springframework.transaction.support.TransactionCallbackWithoutResult
import org.springframework.transaction.support.TransactionTemplate

/**
 * Event Handler: Automatically create a default wallet when a user is registered
 */
@Component
class UserRegisteredEventHandler(
    private val createWalletUseCase: CreateWalletUseCase,
    private val walletRepository: WalletRepository,
    private val transactionManager: PlatformTransactionManager,
    @param:Value("\${wallet.default-currency:VND}") private val defaultCurrency: String
) {
    private val logger = LoggerFactory.getLogger(UserRegisteredEventHandler::class.java)

    /**
     * Handle UserRegisteredEvent by creating a default wallet
     *
     * Uses AFTER_COMMIT phase to ensure wallet creation only happens
     * after user registration transaction commits successfully.
     * If wallet creation fails, it won't affect the user registration.
     */
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    fun handleUserRegistered(event: UserRegisteredEvent) {
        try {
            logger.info("User registered event received for user ID: ${event.aggregateId}, creating default wallet")

            val currencyEnum = try {
                Currency.valueOf(defaultCurrency)
            } catch (e: Exception) {
                logger.warn("Invalid default currency configured: $defaultCurrency, falling back to VND")
                Currency.VND
            }

            // Idempotency check: if a wallet with the same currency already exists for the user, skip creation
            if (walletRepository.existsByUserIdAndCurrency(event.aggregateId, currencyEnum)) {
                logger.info("User ${event.aggregateId} already has a wallet with currency $currencyEnum, skipping creation")
                return
            }

            val command = CreateWalletCommand(
                userId = event.aggregateId,
                currency = currencyEnum.name
            )

            // Execute wallet creation in a new transaction so saveAndFlush has an active transaction
            val txTemplate = TransactionTemplate(transactionManager)
            txTemplate.propagationBehavior = TransactionDefinition.PROPAGATION_REQUIRES_NEW

            txTemplate.execute(object : TransactionCallbackWithoutResult() {
                override fun doInTransactionWithoutResult(status: org.springframework.transaction.TransactionStatus) {
                    createWalletUseCase.execute(command)
                }
            })

            logger.info("Default wallet created successfully for user ID: ${event.aggregateId}")
        } catch (e: Exception) { // Log error but don't throw - wallet creation failure shouldn't affect user registration
            logger.error("Failed to create default wallet for user ID: ${event.aggregateId}", e)
        }
    }
}
