package com.wowo.wowo.application.facade;

import com.wowo.wowo.application.wallet.WalletApplicationService;
import com.wowo.wowo.domain.transaction.entity.TransactionAggregate;
import com.wowo.wowo.domain.wallet.entity.WalletAggregate;
import com.wowo.wowo.model.Transaction;
import com.wowo.wowo.model.Wallet;
import com.wowo.wowo.service.TransactionService;
import com.wowo.wowo.service.WalletService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Facade service that bridges between the legacy architecture and new DDD architecture.
 * This allows gradual migration while maintaining backward compatibility.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class WalletMigrationFacade {
    
    private final WalletService legacyWalletService;
    private final TransactionService legacyTransactionService;
    private final WalletApplicationService dddWalletService;
    
    /**
     * Creates a wallet using the new DDD approach but returns legacy format for compatibility.
     * This demonstrates how to gradually migrate to DDD while maintaining existing API contracts.
     */
    public Wallet createWalletWithDddBackend(String currency) {
        log.info("Creating wallet using DDD backend");
        
        // Use new DDD service
        WalletAggregate dddWallet = dddWalletService.createWallet(currency);
        
        // Convert to legacy format for backward compatibility
        Wallet legacyWallet = new Wallet();
        legacyWallet.setBalance(dddWallet.getCurrentBalance().getAmount());
        legacyWallet.setCurrency(dddWallet.getCurrentBalance().getCurrency());
        
        log.info("Created wallet with DDD backend: ID={}", dddWallet.getId());
        return legacyWallet;
    }
    
    /**
     * Demonstrates how to use both legacy and DDD services together.
     * In a real migration, you would gradually replace legacy calls with DDD calls.
     */
    public String performHybridOperation(Long walletId, Long amount) {
        log.info("Performing hybrid operation - mixing legacy and DDD approaches");
        
        try {
            // Use legacy service for some operations
            Wallet legacyWallet = legacyWalletService.getWallet(walletId);
            log.info("Retrieved wallet using legacy service: balance={}", legacyWallet.getBalance());
            
            // Use DDD service for core business operations
            WalletAggregate dddWallet = dddWalletService.getWallet(walletId);
            log.info("Retrieved wallet using DDD service: balance={}", dddWallet.getCurrentBalance().getAmount());
            
            // Demonstrate that both approaches can coexist
            boolean hasSufficientBalance = dddWallet.hasEnoughBalance(
                new com.wowo.wowo.domain.shared.valueobjects.Money(amount, "VND")
            );
            
            return String.format("Hybrid operation completed. Legacy balance: %d, DDD balance: %d, Sufficient for %d: %s",
                legacyWallet.getBalance(), 
                dddWallet.getCurrentBalance().getAmount(),
                amount,
                hasSufficientBalance);
                
        } catch (Exception e) {
            log.error("Error in hybrid operation", e);
            return "Hybrid operation failed: " + e.getMessage();
        }
    }
    
    /**
     * Migrates a wallet operation from legacy to DDD approach.
     * This shows how to gradually replace legacy code with DDD code.
     */
    public String migrateWalletOperation(Long walletId, Long depositAmount) {
        log.info("Migrating wallet operation from legacy to DDD");
        
        try {
            // Old way (legacy)
            Wallet legacyResult = legacyWalletService.plusBalance(walletId.toString(), depositAmount);
            log.info("Legacy deposit completed: new balance = {}", legacyResult.getBalance());
            
            // New way (DDD) - comment out to show both approaches
            // TransactionAggregate dddResult = dddWalletService.depositMoney(walletId, depositAmount, "VND", "Migration Test");
            // log.info("DDD deposit completed: transaction ID = {}", dddResult.getTransactionId());
            
            return String.format("Migration test completed. Legacy approach worked with new balance: %d", 
                legacyResult.getBalance());
                
        } catch (Exception e) {
            log.error("Error in migration test", e);
            return "Migration test failed: " + e.getMessage();
        }
    }
}