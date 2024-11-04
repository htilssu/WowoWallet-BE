package com.wowo.wowo.repositories;

import com.wowo.wowo.models.Wallet;
import com.wowo.wowo.models.WalletOwnerType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface WalletRepository extends CrudRepository<Wallet, Long> {

    Optional<Wallet> findByOwnerIdAndOwnerType(@Size(max = 32) String ownerId,
            @NotNull WalletOwnerType ownerType);
    Optional<Wallet> findByOwnerId(String senderId);
    boolean existsByOwnerId(String ownerId);
    boolean existsByOwnerIdAndId(@Size(max = 32) String ownerId, Long id);
}
