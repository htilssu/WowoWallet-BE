package com.wowo.wowo.contexts.transaction.application.usecase

import com.wowo.wowo.contexts.transaction.application.dto.TransactionDTO
import com.wowo.wowo.contexts.transaction.application.dto.TransferMoneyCommand
import com.wowo.wowo.contexts.transaction.application.mapper.TransactionMapper
import com.wowo.wowo.contexts.transaction.domain.entity.Transaction
import com.wowo.wowo.shared.domain.DomainEventPublisher
import com.wowo.wowo.shared.exception.EntityNotFoundException
import com.wowo.wowo.shared.exception.InsufficientBalanceException
import com.wowo.wowo.shared.valueobject.Currency
import com.wowo.wowo.shared.valueobject.Money
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import java.math.BigDecimal
import java.time.Instant
import org.junit.jupiter.api.Assertions.*
import com.wowo.wowo.contexts.transaction.domain.repository.TransactionRepository
import com.wowo.wowo.contexts.transaction.domain.service.TransferDomainService

class TransferMoneyUseCaseTest {

    lateinit var transactionRepository: TransactionRepository
    lateinit var transferDomainService: TransferDomainService
    lateinit var eventPublisher: DomainEventPublisher
    lateinit var transactionMapper: TransactionMapper

    lateinit var transferMoneyUseCase: TransferMoneyUseCase

    lateinit var sampleTransaction: Transaction

    @BeforeEach
    fun setUp() {
        transactionRepository = Mockito.mock(TransactionRepository::class.java)
        transferDomainService = Mockito.mock(TransferDomainService::class.java)
        eventPublisher = Mockito.mock(DomainEventPublisher::class.java)
        transactionMapper = Mockito.mock(TransactionMapper::class.java)

        transferMoneyUseCase = TransferMoneyUseCase(
            transactionRepository,
            transferDomainService,
            eventPublisher,
            transactionMapper
        )

        sampleTransaction = Transaction.create(
            sourceWalletId = "wallet-1",
            targetWalletId = "wallet-2",
            amount = Money(BigDecimal("100.00"), Currency.VND),
            type = com.wowo.wowo.contexts.transaction.domain.valueobject.TransactionType.TRANSFER,
            description = "Test"
        )
    }

    @Test
    fun `should transfer money successfully`() {
        val money = Money(BigDecimal("100.00"), Currency.VND)
        val mappedDto = TransactionDTO.unenriched(
            id = sampleTransaction.id.value.toString(),
            sourceWalletId = "wallet-1",
            targetWalletId = "wallet-2",
            amount = BigDecimal("100.00"),
            currency = "VND",
            type = sampleTransaction.type.name,
            status = sampleTransaction.getStatus().name,
            description = sampleTransaction.description,
            reference = sampleTransaction.reference,
            createdAt = Instant.now(),
            updatedAt = Instant.now()
        )
        
        // Given
        Mockito.`when`(transferDomainService.executeTransfer("wallet-1", "wallet-2", money, "Test transfer"))
            .thenReturn(sampleTransaction)
        Mockito.`when`(transactionRepository.save(sampleTransaction)).thenReturn(sampleTransaction)
        Mockito.`when`(transactionMapper.toDTO(sampleTransaction)).thenReturn(mappedDto)

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
        assertEquals("wallet-1", result.sourceWalletId)
        assertEquals("wallet-2", result.targetWalletId)
        Mockito.verify(transferDomainService).executeTransfer("wallet-1", "wallet-2", money, "Test transfer")
        Mockito.verify(transactionRepository).save(sampleTransaction)
        Mockito.verify(transactionMapper).toDTO(sampleTransaction)
        Mockito.verify(eventPublisher).publish(emptyList())
    }

    @Test
    fun `should throw when source wallet not found`() {
        val money = Money(BigDecimal("100.00"), Currency.VND)

        // Given
        Mockito.`when`(transferDomainService.executeTransfer("wallet-1", "wallet-2", money, "x"))
            .thenThrow(EntityNotFoundException("Source wallet not found"))

        val command = TransferMoneyCommand("wallet-1", "wallet-2", "100.00", "VND", "x")

        // When & Then
        assertThrows(EntityNotFoundException::class.java) {
            transferMoneyUseCase.execute(command)
        }
    }

    @Test
    fun `should throw when destination wallet not found`() {
        val money = Money(BigDecimal("100.00"), Currency.VND)

        // Given
        Mockito.`when`(transferDomainService.executeTransfer("wallet-1", "wallet-2", money, "x"))
            .thenThrow(EntityNotFoundException("Destination wallet not found"))

        val command = TransferMoneyCommand("wallet-1", "wallet-2", "100.00", "VND", "x")

        // When & Then
        assertThrows(EntityNotFoundException::class.java) {
            transferMoneyUseCase.execute(command)
        }
    }

    @Test
    fun `should throw when insufficient balance`() {
        val money = Money(BigDecimal("100.00"), Currency.VND)

        // Given
        Mockito.`when`(transferDomainService.executeTransfer("wallet-1", "wallet-2", money, "x"))
            .thenThrow(InsufficientBalanceException("Insufficient balance"))

        val command = TransferMoneyCommand("wallet-1", "wallet-2", "100.00", "VND", "x")

        // When & Then
        assertThrows(InsufficientBalanceException::class.java) {
            transferMoneyUseCase.execute(command)
        }
    }
}

