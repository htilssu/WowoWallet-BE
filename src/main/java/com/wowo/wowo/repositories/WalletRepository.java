package com.wowo.wowo.repositories;

import com.wowo.wowo.models.Wallet;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface WalletRepository extends CrudRepository<Wallet, String> {
    Optional<Wallet> findByOwnerIdAndOwnerType(String ownerId, String ownerType);
    Optional<Wallet> findByOwnerId(String senderId);
}
