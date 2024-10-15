package com.wowo.wowo.repositories;

import com.wowo.wowo.models.Wallet;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface WalletRepository extends CrudRepository<Wallet, String> {

    Optional<Wallet> findByOwnerIdAndOwnerType(String ownerId, String ownerType);
    Optional<Wallet> findByOwnerId(String senderId);
    
    @Query("SELECT w FROM Wallet w JOIN User u ON w.ownerId = u.id WHERE u.id = :userId OR u" +
            ".username = :username OR u.email = :email")
    Optional<Wallet> findByUserIdOrUsernameOrEmail(@Param("userId") String userId,
            @Param("username") String username,
            @Param("email") String email);
}
