package com.wowo.wowo.infrastructure.wallet;

import com.wowo.wowo.domain.wallet.entity.WalletAggregate;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data JPA repository for WalletAggregate.
 */
public interface SpringDataWalletRepository extends JpaRepository<WalletAggregate, Long> {
}