package com.wowo.wowo.application.wallet;

import com.wowo.wowo.application.dto.TransferMoneyCommand;
import com.wowo.wowo.domain.shared.valueobjects.Money;
import com.wowo.wowo.domain.transaction.entity.TransactionAggregate;
import com.wowo.wowo.domain.transaction.service.MoneyTransferDomainService;
import com.wowo.wowo.domain.wallet.entity.WalletAggregate;
import com.wowo.wowo.domain.wallet.repository.WalletRepository;
import com.wowo.wowo.domain.wallet.valueobjects.WalletId;
import com.wowo.wowo.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Application service for wallet operations.
 * Orchestrates domain services and manages application-level concerns.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class WalletApplicationService {
    
    private final WalletRepository walletRepository;
    private final MoneyTransferDomainService moneyTransferDomainService;
    
    /**
     * Creates a new wallet with the specified currency.
     * 
     * @param currency the currency for the wallet
     * @return the created wallet
     */
    @Transactional
    public WalletAggregate createWallet(String currency) {
        log.info("Creating new wallet with currency: {}", currency);
        
        WalletAggregate wallet = new WalletAggregate(currency);
        WalletAggregate savedWallet = walletRepository.save(wallet);
        
        log.info("Created wallet with ID: {}", savedWallet.getWalletId());
        return savedWallet;
    }
    
    /**
     * Gets a wallet by its ID.
     * 
     * @param walletId the wallet ID
     * @return the wallet
     * @throws NotFoundException if wallet not found
     */
    public WalletAggregate getWallet(Long walletId) {
        return walletRepository.findById(WalletId.of(walletId))
            .orElseThrow(() -> new NotFoundException("Wallet not found with ID: " + walletId));
    }
    
    /**
     * Transfers money between wallets.
     * 
     * @param command the transfer command
     * @return the completed transaction
     */
    @Transactional
    public TransactionAggregate transferMoney(TransferMoneyCommand command) {
        log.info("Processing money transfer from wallet {} to wallet {}, amount: {} {}", 
            command.getSourceWalletId(), command.getTargetWalletId(), 
            command.getAmount(), command.getCurrency());
        
        Money amount = new Money(command.getAmount(), command.getCurrency());
        
        TransactionAggregate transaction = moneyTransferDomainService.transferMoney(
            command.getSourceWalletId(),
            command.getTargetWalletId(),
            amount,
            command.getSenderName(),
            command.getReceiverName(),
            command.getMessage()
        );
        
        log.info("Money transfer completed with transaction ID: {}", transaction.getTransactionId());
        return transaction;
    }
    
    /**
     * Deposits money to a wallet.
     * 
     * @param walletId the wallet ID
     * @param amount the amount to deposit
     * @param currency the currency
     * @param source the source of the deposit
     * @return the completed transaction
     */
    @Transactional
    public TransactionAggregate depositMoney(Long walletId, Long amount, String currency, String source) {
        log.info("Processing deposit to wallet {}, amount: {} {}", walletId, amount, currency);
        
        Money money = new Money(amount, currency);
        
        TransactionAggregate transaction = moneyTransferDomainService.depositMoney(
            walletId,
            money,
            source
        );
        
        log.info("Deposit completed with transaction ID: {}", transaction.getTransactionId());
        return transaction;
    }
    
    /**
     * Withdraws money from a wallet.
     * 
     * @param walletId the wallet ID
     * @param amount the amount to withdraw
     * @param currency the currency
     * @param destination the destination of the withdrawal
     * @return the completed transaction
     */
    @Transactional
    public TransactionAggregate withdrawMoney(Long walletId, Long amount, String currency, String destination) {
        log.info("Processing withdrawal from wallet {}, amount: {} {}", walletId, amount, currency);
        
        Money money = new Money(amount, currency);
        
        TransactionAggregate transaction = moneyTransferDomainService.withdrawMoney(
            walletId,
            money,
            destination
        );
        
        log.info("Withdrawal completed with transaction ID: {}", transaction.getTransactionId());
        return transaction;
    }
}