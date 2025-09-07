package com.wowo.wowo.infrastructure.wallet;

import com.wowo.wowo.domain.wallet.entity.WalletAggregate;
import com.wowo.wowo.domain.wallet.repository.WalletRepository;
import com.wowo.wowo.domain.wallet.valueobjects.WalletId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * JPA implementation of the WalletRepository domain interface.
 */
@Repository
@RequiredArgsConstructor
public class JpaWalletRepository implements WalletRepository {
    
    private final SpringDataWalletRepository springDataRepository;
    
    @Override
    public WalletAggregate save(WalletAggregate wallet) {
        return springDataRepository.save(wallet);
    }
    
    @Override
    public Optional<WalletAggregate> findById(WalletId walletId) {
        return springDataRepository.findById(walletId.getValue());
    }
    
    @Override
    public boolean existsById(WalletId walletId) {
        return springDataRepository.existsById(walletId.getValue());
    }
    
    @Override
    public void deleteById(WalletId walletId) {
        springDataRepository.deleteById(walletId.getValue());
    }
}