package com.wowo.wowo.application.usecases

import com.wowo.wowo.contexts.transaction.application.usecase.TransferMoneyUseCase
import com.wowo.wowo.contexts.transaction.application.dto.TransactionDTO
import com.wowo.wowo.contexts.transaction.application.dto.TransferMoneyCommand
import com.wowo.wowo.contexts.transaction.domain.entity.Transaction
import com.wowo.wowo.contexts.transaction.domain.repository.TransactionRepository
import com.wowo.wowo.contexts.wallet.domain.entity.Wallet
import com.wowo.wowo.contexts.wallet.domain.repository.WalletRepository
import com.wowo.wowo.shared.domain.DomainEventPublisher
import com.wowo.wowo.shared.domain.DomainEvent
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
class TransferMoneyUseCaseImplTest {

    @Mock
    lateinit var transactionRepository: TransactionRepository

    @Mock
    lateinit var walletRepository: WalletRepository

    @Mock
    lateinit var eventPublisher: DomainEventPublisher

    @InjectMocks
    lateinit var transferMoneyUseCase: TransferMoneyUseCase

    lateinit var fromWallet: Wallet
    lateinit var toWallet: Wallet
    lateinit var sampleTransaction: Transaction

    @BeforeEach
    fun setUp() {
        fromWallet = Wallet.create("user1", Currency.VND)
        // set initial balance by crediting
        fromWallet.credit(Money(BigDecimal("1000.00"), Currency.VND))

        toWallet = Wallet.create("user2", Currency.VND)
        toWallet.credit(Money(BigDecimal("0.00"), Currency.VND))

        sampleTransaction = Transaction.create(
            fromWalletId = fromWallet.id.toString(),
            toWalletId = toWallet.id.toString(),
            amount = Money(BigDecimal("100.00"), Currency.VND),
            type = com.wowo.wowo.contexts.transaction.domain.valueobject.TransactionType.TRANSFER,
            description = "Test"
        )
    }

    @Test
    fun `should transfer money successfully`() {
        Mockito.`when`(walletRepository.findById(fromWallet.id)).thenReturn(fromWallet)
        Mockito.`when`(walletRepository.findById(toWallet.id)).thenReturn(toWallet)
        Mockito.`when`(transactionRepository.save(Mockito.any(Transaction::class.java))).thenReturn(sampleTransaction)

        val command = TransferMoneyCommand(
            fromWalletId = fromWallet.id.toString(),
            toWalletId = toWallet.id.toString(),
            amount = "100.00",
            currency = "VND",
            description = "Test transfer"
        )

        val result: TransactionDTO = transferMoneyUseCase.execute(command)

        assertNotNull(result)
        assertEquals(sampleTransaction.id.toString(), result.id)
        Mockito.verify(walletRepository, Mockito.times(2)).save(Mockito.any())
        Mockito.verify(transactionRepository).save(Mockito.any())
        Mockito.verify(eventPublisher, Mockito.times(3)).publish(Mockito.any(DomainEvent::class.java))
    }

    @Test
    fun `should throw when source wallet not found`() {
        Mockito.`when`(walletRepository.findById(fromWallet.id)).thenReturn(null)

        val command = TransferMoneyCommand(fromWallet.id.toString(), toWallet.id.toString(), "100.00", "VND", "x")

        assertThrows(EntityNotFoundException::class.java) {
            transferMoneyUseCase.execute(command)
        }
    }

    @Test
    fun `should throw when destination wallet not found`() {
        Mockito.`when`(walletRepository.findById(fromWallet.id)).thenReturn(fromWallet)
        Mockito.`when`(walletRepository.findById(toWallet.id)).thenReturn(null)

        val command = TransferMoneyCommand(fromWallet.id.toString(), toWallet.id.toString(), "100.00", "VND", "x")

        assertThrows(EntityNotFoundException::class.java) {
            transferMoneyUseCase.execute(command)
        }
    }

    @Test
    fun `should throw when insufficient balance`() {
        // debit so balance < 100
        fromWallet = Wallet.create("user1b", Currency.VND)
        fromWallet.credit(Money(BigDecimal("50.00"), Currency.VND))

        Mockito.`when`(walletRepository.findById(fromWallet.id)).thenReturn(fromWallet)
        Mockito.`when`(walletRepository.findById(toWallet.id)).thenReturn(toWallet)

        val command = TransferMoneyCommand(fromWallet.id.toString(), toWallet.id.toString(), "100.00", "VND", "x")

        assertThrows(InsufficientBalanceException::class.java) {
            transferMoneyUseCase.execute(command)
        }
    }
}
