package com.wowo.wowo.contexts.transaction.application.usecase

import com.wowo.wowo.contexts.transaction.application.dto.TransactionDTO
import com.wowo.wowo.contexts.transaction.application.dto.TransferMoneyCommand
import com.wowo.wowo.contexts.transaction.domain.entity.Transaction
import com.wowo.wowo.contexts.transaction.domain.repository.TransactionRepository
import com.wowo.wowo.contexts.transaction.domain.service.TransferDomainService
import com.wowo.wowo.contexts.transaction.domain.acl.WalletACL
import com.wowo.wowo.contexts.transaction.domain.acl.UserACL
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

class TransferMoneyUseCaseTest {

    lateinit var transactionRepository: TransactionRepository
    lateinit var transferDomainService: TransferDomainService
    lateinit var eventPublisher: DomainEventPublisher
    lateinit var walletACL: WalletACL
    lateinit var userACL: UserACL

    lateinit var transferMoneyUseCase: TransferMoneyUseCase

    lateinit var sampleTransaction: Transaction

    @BeforeEach
    fun setUp() {
        transactionRepository = Mockito.mock(TransactionRepository::class.java)
        transferDomainService = Mockito.mock(TransferDomainService::class.java)
        eventPublisher = Mockito.mock(DomainEventPublisher::class.java)
        walletACL = Mockito.mock(WalletACL::class.java)
        userACL = Mockito.mock(UserACL::class.java)

        transferMoneyUseCase = TransferMoneyUseCase(
            transactionRepository,
            transferDomainService,
            eventPublisher,
            walletACL,
            userACL
        )

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
        val money = Money(BigDecimal("100.00"), Currency.VND)
        
        // Given
        Mockito.`when`(transferDomainService.executeTransfer(
            org.mockito.ArgumentMatchers.eq("wallet-1"),
            org.mockito.ArgumentMatchers.eq("wallet-2"),
            org.mockito.ArgumentMatchers.eq(money),
            org.mockito.ArgumentMatchers.any()
        )).thenReturn(sampleTransaction)
        Mockito.`when`(transactionRepository.save(org.mockito.ArgumentMatchers.any(Transaction::class.java)))
            .thenReturn(sampleTransaction)

        Mockito.`when`(walletACL.getWalletOwner(org.mockito.ArgumentMatchers.eq("wallet-1"))).thenReturn("user-1")
        Mockito.`when`(userACL.getUserName(org.mockito.ArgumentMatchers.eq("user-1"))).thenReturn("Jane Doe")

        Mockito.`when`(walletACL.getWalletOwner(org.mockito.ArgumentMatchers.eq("wallet-2"))).thenReturn("user-2")
        Mockito.`when`(userACL.getUserName(org.mockito.ArgumentMatchers.eq("user-2"))).thenReturn("John Doe")

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
        assertEquals("Jane Doe", result.fromWalletName)
        assertEquals("John Doe", result.toWalletName)
        Mockito.verify(transferDomainService).executeTransfer(
            org.mockito.ArgumentMatchers.eq("wallet-1"),
            org.mockito.ArgumentMatchers.eq("wallet-2"),
            org.mockito.ArgumentMatchers.eq(money),
            org.mockito.ArgumentMatchers.any()
        )
        Mockito.verify(transactionRepository).save(org.mockito.ArgumentMatchers.any(Transaction::class.java))
    }

    @Test
    fun `should throw when source wallet not found`() {
        val money = Money(BigDecimal("100.00"), Currency.VND)

        // Given
        Mockito.`when`(transferDomainService.executeTransfer(
            org.mockito.ArgumentMatchers.eq("wallet-1"),
            org.mockito.ArgumentMatchers.eq("wallet-2"),
            org.mockito.ArgumentMatchers.eq(money),
            org.mockito.ArgumentMatchers.any()
        )).thenThrow(EntityNotFoundException("Source wallet not found"))

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
        Mockito.`when`(transferDomainService.executeTransfer(
            org.mockito.ArgumentMatchers.eq("wallet-1"),
            org.mockito.ArgumentMatchers.eq("wallet-2"),
            org.mockito.ArgumentMatchers.eq(money),
            org.mockito.ArgumentMatchers.any()
        )).thenThrow(EntityNotFoundException("Destination wallet not found"))

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
        Mockito.`when`(transferDomainService.executeTransfer(
            org.mockito.ArgumentMatchers.eq("wallet-1"),
            org.mockito.ArgumentMatchers.eq("wallet-2"),
            org.mockito.ArgumentMatchers.eq(money),
            org.mockito.ArgumentMatchers.any()
        )).thenThrow(InsufficientBalanceException("Insufficient balance"))

        val command = TransferMoneyCommand("wallet-1", "wallet-2", "100.00", "VND", "x")

        // When & Then
        assertThrows(InsufficientBalanceException::class.java) {
            transferMoneyUseCase.execute(command)
        }
    }
}

