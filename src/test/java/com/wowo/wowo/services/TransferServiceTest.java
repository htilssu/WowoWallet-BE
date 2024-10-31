package com.wowo.wowo.services;

import com.wowo.wowo.exceptions.InsufficientBalanceException;
import com.wowo.wowo.models.Wallet;
import com.wowo.wowo.repositories.WalletRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

class TransferServiceTest {

    @Mock
    private WalletRepository walletRepository;

    @InjectMocks
    private TransferService transferService;

    private Wallet sourceWallet;
    private Wallet destinationWallet;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        sourceWallet = new Wallet();
        sourceWallet.setId(1L);
        sourceWallet.setBalance(1000L);

        destinationWallet = new Wallet();
        destinationWallet.setId(2L);
        destinationWallet.setBalance(1000L);
    }

    @Test
    void transferMoney_SuccessfulTransfer() {
        transferService.transferMoney(sourceWallet, destinationWallet, 100L);

        assertEquals(900L, sourceWallet.getBalance());
        assertEquals(1100L, destinationWallet.getBalance());

        verify(walletRepository, times(1)).saveAll(anyList());
    }

    @Test
    void transferMoney_InsufficientBalance() {
        InsufficientBalanceException exception = assertThrows(InsufficientBalanceException.class, () ->
                transferService.transferMoney(sourceWallet, destinationWallet, 1200L)
        );

        assertEquals("Số dư không đủ", exception.getMessage());

        verify(walletRepository, never()).saveAll(anyList());
    }
}