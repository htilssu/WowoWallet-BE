package com.wowo.wowo.domain.wallet.repository;

import com.wowo.wowo.domain.wallet.entity.WalletAggregate;
import com.wowo.wowo.domain.wallet.valueobjects.WalletId;
import com.wowo.wowo.exception.NotFoundException;

import java.util.Optional;

/**
 * Domain repository interface for Wallet aggregate.
 * Defines the contract for wallet persistence operations.
 */
public interface WalletRepository {
    
    /**
     * Saves a wallet aggregate.
     * 
     * @param wallet the wallet to save
     * @return the saved wallet
     */
    WalletAggregate save(WalletAggregate wallet);
    
    /**
     * Finds a wallet by its ID.
     * 
     * @param walletId the wallet ID
     * @return the wallet if found
     */
    Optional<WalletAggregate> findById(WalletId walletId);
    
    /**
     * Finds a wallet by its ID or throws an exception.
     * 
     * @param walletId the wallet ID
     * @return the wallet
     * @throws NotFoundException if wallet not found
     */
    default WalletAggregate findByIdOrThrow(WalletId walletId) {
        return findById(walletId)
            .orElseThrow(() -> new NotFoundException("Wallet not found: " + walletId.getValue()));
    }
    
    /**
     * Checks if a wallet exists by its ID.
     * 
     * @param walletId the wallet ID
     * @return true if the wallet exists
     */
    boolean existsById(WalletId walletId);
    
    /**
     * Deletes a wallet by its ID.
     * 
     * @param walletId the wallet ID
     */
    void deleteById(WalletId walletId);
}