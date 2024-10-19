package com.wowo.wowo.repositories;

import com.wowo.wowo.models.Wallet;
import jakarta.validation.constraints.Size;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface WalletRepository extends CrudRepository<Wallet, Long> {

    Optional<Wallet> findByOwnerIdAndOwnerType(String ownerId, String ownerType);
    Optional<Wallet> findByOwnerId(String senderId);
    boolean existsByOwnerId(String ownerId);
    boolean existsByOwnerIdAndId(@Size(max = 32) String ownerId, Long id);
}
