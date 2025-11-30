package com.wowo.wowo.contexts.transaction.application.usecase

import com.wowo.wowo.contexts.transaction.application.dto.TransactionDTO
import com.wowo.wowo.contexts.transaction.application.dto.TransferMoneyCommand
import com.wowo.wowo.contexts.transaction.domain.entity.Transaction
import com.wowo.wowo.contexts.transaction.domain.repository.TransactionRepository
import com.wowo.wowo.contexts.transaction.domain.service.TransferDomainService
import com.wowo.wowo.shared.domain.DomainEventPublisher
import com.wowo.wowo.shared.exception.EntityNotFoundException
import com.wowo.wowo.shared.exception.InsufficientBalanceException
import com.wowo.wowo.shared.valueobject.Currency
import com.wowo.wowo.shared.valueobject.Money
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.jupiter.MockitoExtension
import java.math.BigDecimal
import org.junit.jupiter.api.Assertions.*

@ExtendWith(MockitoExtension::class)
class TransferMoneyUseCaseTest {

    @Mock
    lateinit var transactionRepository: TransactionRepository

    @Mock
    lateinit var transferDomainService: TransferDomainService

    @Mock
    lateinit var eventPublisher: DomainEventPublisher

    @InjectMocks
    lateinit var transferMoneyUseCase: TransferMoneyUseCase

    lateinit var sampleTransaction: Transaction

    @BeforeEach
    fun setUp() {
        sampleTransaction = Transaction.create(
            fromWalletId = "wallet-1",
            toWalletId = "wallet-2",
            amount = Money(BigDecimal("100.00"), Currency.VND),
            type = com.wowo.wowo.contexts.transaction.domain.valueobject.TransactionType.TRANSFER,
            description = "Test"
        )
    }

    @Test
    fun `should transfer money successfully`() {
        // Given
        Mockito.`when`(transferDomainService.executeTransfer(
            org.mockito.ArgumentMatchers.anyString(),
            org.mockito.ArgumentMatchers.anyString(),
            org.mockito.ArgumentMatchers.any(Money::class.java),
            org.mockito.ArgumentMatchers.nullable(String::class.java)
        )).thenReturn(sampleTransaction)
        Mockito.`when`(transactionRepository.save(org.mockito.ArgumentMatchers.any(Transaction::class.java)))
            .thenReturn(sampleTransaction)

        val command = TransferMoneyCommand(
            fromWalletId = "wallet-1",
            toWalletId = "wallet-2",
            amount = "100.00",
            currency = "VND",
            description = "Test transfer"
        )

        // When
        val result: TransactionDTO = transferMoneyUseCase.execute(command)

        // Then
        assertNotNull(result)
        assertEquals(sampleTransaction.id.toString(), result.id)
        Mockito.verify(transferDomainService).executeTransfer(
            org.mockito.ArgumentMatchers.anyString(),
            org.mockito.ArgumentMatchers.anyString(),
            org.mockito.ArgumentMatchers.any(Money::class.java),
            org.mockito.ArgumentMatchers.nullable(String::class.java)
        )
        Mockito.verify(transactionRepository).save(org.mockito.ArgumentMatchers.any(Transaction::class.java))
    }

    @Test
    fun `should throw when source wallet not found`() {
        // Given
        Mockito.`when`(transferDomainService.executeTransfer(
            org.mockito.ArgumentMatchers.anyString(),
            org.mockito.ArgumentMatchers.anyString(),
            org.mockito.ArgumentMatchers.any(Money::class.java),
            org.mockito.ArgumentMatchers.nullable(String::class.java)
        )).thenThrow(EntityNotFoundException("Source wallet not found"))

        val command = TransferMoneyCommand("wallet-1", "wallet-2", "100.00", "VND", "x")

        // When & Then
        assertThrows(EntityNotFoundException::class.java) {
            transferMoneyUseCase.execute(command)
        }
    }

    @Test
    fun `should throw when destination wallet not found`() {
        // Given
        Mockito.`when`(transferDomainService.executeTransfer(
            org.mockito.ArgumentMatchers.anyString(),
            org.mockito.ArgumentMatchers.anyString(),
            org.mockito.ArgumentMatchers.any(Money::class.java),
            org.mockito.ArgumentMatchers.nullable(String::class.java)
        )).thenThrow(EntityNotFoundException("Destination wallet not found"))

        val command = TransferMoneyCommand("wallet-1", "wallet-2", "100.00", "VND", "x")

        // When & Then
        assertThrows(EntityNotFoundException::class.java) {
            transferMoneyUseCase.execute(command)
        }
    }

    @Test
    fun `should throw when insufficient balance`() {
        // Given
        Mockito.`when`(transferDomainService.executeTransfer(
            org.mockito.ArgumentMatchers.anyString(),
            org.mockito.ArgumentMatchers.anyString(),
            org.mockito.ArgumentMatchers.any(Money::class.java),
            org.mockito.ArgumentMatchers.nullable(String::class.java)
        )).thenThrow(InsufficientBalanceException("Insufficient balance"))

        val command = TransferMoneyCommand("wallet-1", "wallet-2", "100.00", "VND", "x")

        // When & Then
        assertThrows(InsufficientBalanceException::class.java) {
            transferMoneyUseCase.execute(command)
        }
    }
}

