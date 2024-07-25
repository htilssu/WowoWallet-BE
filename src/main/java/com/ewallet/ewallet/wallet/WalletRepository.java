package com.ewallet.ewallet.wallet;

import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface WalletRepository extends CrudRepository<Wallet, String> {

    Optional<Wallet> findByOwnerIdAndOwnerType(String ownerId, String ownerType);
}
