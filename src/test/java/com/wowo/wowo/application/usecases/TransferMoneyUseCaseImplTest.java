package com.wowo.wowo.application.usecases;

import com.wowo.wowo.application.ports.out.NotificationPort;
import com.wowo.wowo.domain.events.DomainEventPublisher;
import com.wowo.wowo.domain.repositories.TransactionDomainRepository;
import com.wowo.wowo.domain.repositories.UserDomainRepository;
import com.wowo.wowo.domain.repositories.WalletDomainRepository;
import com.wowo.wowo.domain.services.TransactionFactory;
import com.wowo.wowo.exception.BadRequest;
import com.wowo.wowo.exception.NotFoundException;
import com.wowo.wowo.model.*;
import com.wowo.wowo.repository.ConstantRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

/**
 * Unit tests for TransferMoneyUseCaseImpl
 */
@ExtendWith(MockitoExtension.class)
class TransferMoneyUseCaseImplTest {

    @Mock
    private WalletDomainRepository walletRepository;

    @Mock
    private UserDomainRepository userRepository;

    @Mock
    private TransactionDomainRepository transactionRepository;

    @Mock
    private ConstantRepository constantRepository;

    @Mock
    private TransactionFactory transactionFactory;

    @Mock
    private DomainEventPublisher eventPublisher;

    @Mock
    private NotificationPort notificationPort;

    @InjectMocks
    private TransferMoneyUseCaseImpl transferMoneyUseCase;

    private User sender;
    private User receiver;
    private UserWallet sourceWallet;
    private UserWallet destWallet;
    private Transaction mockTransaction;

    @BeforeEach
    void setUp() {
        // Setup sender
        sender = new User();
        sender.setId("user1");
        sender.setFullName("Sender Name");

        sourceWallet = new UserWallet();
        sourceWallet.setId(1L);
        sourceWallet.setBalance(1000L);
        sourceWallet.setUser(sender);
        sender.setWallet(sourceWallet);

        // Setup receiver
        receiver = new User();
        receiver.setId("user2");
        receiver.setFullName("Receiver Name");

        destWallet = new UserWallet();
        destWallet.setId(2L);
        destWallet.setBalance(0L);
        destWallet.setUser(receiver);
        receiver.setWallet(destWallet);

        // Setup mock transaction
        mockTransaction = new Transaction();
        mockTransaction.setId("tx123");
        mockTransaction.setAmount(100.0);
    }

    @Test
    void shouldTransferMoneySuccessfully() {
        // Given
        when(walletRepository.findById("1")).thenReturn(Optional.of(sourceWallet));
        when(userRepository.findByIdOrEmailOrUsername(anyString(), anyString(), anyString()))
                .thenReturn(Optional.of(receiver));
        when(transactionFactory.createTransferTransaction(any(), any(), anyDouble(), anyString()))
                .thenReturn(mockTransaction);
        when(walletRepository.save(any())).thenReturn(sourceWallet);
        when(transactionRepository.save(any())).thenReturn(mockTransaction);

        // When
        Transaction result = transferMoneyUseCase.transferMoney(
                "1", "user2", 100.0, "Test transfer", "user1"
        );

        // Then
        assertNotNull(result);
        assertEquals("tx123", result.getId());
        verify(walletRepository, times(2)).save(any(Wallet.class));
        verify(transactionRepository).save(any(Transaction.class));
        verify(eventPublisher).publish(any());
        verify(notificationPort, times(2)).sendTransactionNotification(anyString(), anyString(), anyString());
    }

    @Test
    void shouldThrowExceptionWhenSourceWalletNotFound() {
        // Given
        when(walletRepository.findById(anyString())).thenReturn(Optional.empty());

        // When & Then
        assertThrows(NotFoundException.class, () -> {
            transferMoneyUseCase.transferMoney(
                    "999", "user2", 100.0, "Test", "user1"
            );
        });
    }

    @Test
    void shouldThrowExceptionWhenReceiverNotFound() {
        // Given
        when(walletRepository.findById("1")).thenReturn(Optional.of(sourceWallet));
        when(userRepository.findByIdOrEmailOrUsername(anyString(), anyString(), anyString()))
                .thenReturn(Optional.empty());

        // When & Then
        assertThrows(NotFoundException.class, () -> {
            transferMoneyUseCase.transferMoney(
                    "1", "nonexistent", 100.0, "Test", "user1"
            );
        });
    }

    @Test
    void shouldThrowExceptionWhenInsufficientBalance() {
        // Given
        sourceWallet.setBalance(50L); // Less than transfer amount
        when(walletRepository.findById("1")).thenReturn(Optional.of(sourceWallet));
        when(userRepository.findByIdOrEmailOrUsername(anyString(), anyString(), anyString()))
                .thenReturn(Optional.of(receiver));

        // When & Then
        assertThrows(BadRequest.class, () -> {
            transferMoneyUseCase.transferMoney(
                    "1", "user2", 100.0, "Test", "user1"
            );
        });
    }

    @Test
    void shouldThrowExceptionWhenUnauthorized() {
        // Given
        when(walletRepository.findById("1")).thenReturn(Optional.of(sourceWallet));

        // When & Then
        assertThrows(BadRequest.class, () -> {
            transferMoneyUseCase.transferMoney(
                    "1", "user2", 100.0, "Test", "wrongUser"
            );
        });
    }

    @Test
    void shouldUpdateBalancesCorrectly() {
        // Given
        when(walletRepository.findById("1")).thenReturn(Optional.of(sourceWallet));
        when(userRepository.findByIdOrEmailOrUsername(anyString(), anyString(), anyString()))
                .thenReturn(Optional.of(receiver));
        when(transactionFactory.createTransferTransaction(any(), any(), anyDouble(), anyString()))
                .thenReturn(mockTransaction);
        when(transactionRepository.save(any())).thenReturn(mockTransaction);

        Wallet[] capturedWallets = new Wallet[2];
        when(walletRepository.save(any())).thenAnswer(invocation -> {
            Wallet wallet = invocation.getArgument(0);
            if (capturedWallets[0] == null) {
                capturedWallets[0] = wallet;
            } else {
                capturedWallets[1] = wallet;
            }
            return wallet;
        });

        // When
        transferMoneyUseCase.transferMoney(
                "1", "user2", 100.0, "Test", "user1"
        );

        // Then
        verify(walletRepository, times(2)).save(any());
        // Source wallet should have 900 (1000 - 100)
        // Dest wallet should have 100 (0 + 100)
    }
}

