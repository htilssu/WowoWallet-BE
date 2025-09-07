package com.wowo.wowo.domain.transaction.service;

import com.wowo.wowo.domain.shared.valueobjects.Money;
import com.wowo.wowo.domain.transaction.entity.TransactionAggregate;
import com.wowo.wowo.domain.transaction.valueobjects.TransactionType;
import com.wowo.wowo.domain.wallet.entity.WalletAggregate;
import com.wowo.wowo.domain.wallet.repository.WalletRepository;
import com.wowo.wowo.exception.InsufficientBalanceException;
import com.wowo.wowo.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Domain service for handling money transfer operations between wallets.
 * Coordinates business operations that span multiple aggregates.
 */
@Service
@RequiredArgsConstructor
public class MoneyTransferDomainService {
    
    private final WalletRepository walletRepository;
    
    /**
     * Transfers money from source wallet to target wallet.
     * 
     * @param sourceWalletId the source wallet ID
     * @param targetWalletId the target wallet ID  
     * @param amount the amount to transfer
     * @param senderName the name of the sender
     * @param receiverName the name of the receiver
     * @param message optional message for the transfer
     * @return the completed transaction
     */
    @Transactional
    public TransactionAggregate transferMoney(Long sourceWalletId, 
                                            Long targetWalletId,
                                            Money amount,
                                            String senderName,
                                            String receiverName,
                                            String message) {
        
        // Retrieve wallets
        WalletAggregate sourceWallet = walletRepository.findById(com.wowo.wowo.domain.wallet.valueobjects.WalletId.of(sourceWalletId))
            .orElseThrow(() -> new NotFoundException("Source wallet not found"));
            
        WalletAggregate targetWallet = walletRepository.findById(com.wowo.wowo.domain.wallet.valueobjects.WalletId.of(targetWalletId))
            .orElseThrow(() -> new NotFoundException("Target wallet not found"));
        
        // Validate transfer is possible
        if (!sourceWallet.hasEnoughBalance(amount)) {
            throw new InsufficientBalanceException("Insufficient balance in source wallet");
        }
        
        // Perform the transfer
        sourceWallet.withdraw(amount);
        targetWallet.deposit(amount);
        
        // Save wallets
        walletRepository.save(sourceWallet);
        walletRepository.save(targetWallet);
        
        // Create and complete transaction record
        TransactionAggregate transaction = new TransactionAggregate(
            amount,
            sourceWallet.getWalletId(),
            targetWallet.getWalletId(),
            TransactionType.TRANSFER_MONEY,
            senderName,
            receiverName,
            message
        );
        
        transaction.complete();
        
        return transaction;
    }
    
    /**
     * Deposits money to a wallet (e.g., from external source).
     * 
     * @param targetWalletId the target wallet ID
     * @param amount the amount to deposit
     * @param source the source of the deposit
     * @return the completed transaction
     */
    @Transactional
    public TransactionAggregate depositMoney(Long targetWalletId,
                                           Money amount,
                                           String source) {
        
        WalletAggregate targetWallet = walletRepository.findById(com.wowo.wowo.domain.wallet.valueobjects.WalletId.of(targetWalletId))
            .orElseThrow(() -> new NotFoundException("Target wallet not found"));
        
        targetWallet.deposit(amount);
        walletRepository.save(targetWallet);
        
        // For deposits, we use a system wallet as source
        TransactionAggregate transaction = new TransactionAggregate(
            amount,
            com.wowo.wowo.domain.wallet.valueobjects.WalletId.of(-1L), // System wallet
            targetWallet.getWalletId(),
            TransactionType.DEPOSIT,
            "System",
            targetWallet.getWalletId().toString(),
            "Deposit from " + source
        );
        
        transaction.complete();
        
        return transaction;
    }
    
    /**
     * Withdraws money from a wallet (e.g., to external destination).
     * 
     * @param sourceWalletId the source wallet ID
     * @param amount the amount to withdraw
     * @param destination the destination of the withdrawal
     * @return the completed transaction
     */
    @Transactional
    public TransactionAggregate withdrawMoney(Long sourceWalletId,
                                            Money amount,
                                            String destination) {
        
        WalletAggregate sourceWallet = walletRepository.findById(com.wowo.wowo.domain.wallet.valueobjects.WalletId.of(sourceWalletId))
            .orElseThrow(() -> new NotFoundException("Source wallet not found"));
        
        sourceWallet.withdraw(amount);
        walletRepository.save(sourceWallet);
        
        // For withdrawals, we use a system wallet as target
        TransactionAggregate transaction = new TransactionAggregate(
            amount,
            sourceWallet.getWalletId(),
            com.wowo.wowo.domain.wallet.valueobjects.WalletId.of(-1L), // System wallet
            TransactionType.WITHDRAW,
            sourceWallet.getWalletId().toString(),
            "System",
            "Withdrawal to " + destination
        );
        
        transaction.complete();
        
        return transaction;
    }
}