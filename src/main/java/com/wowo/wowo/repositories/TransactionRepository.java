package com.wowo.wowo.repositories;

import com.wowo.wowo.models.Transaction;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, String> {

    List<Transaction> findByIdOrderByUpdatedAsc(String id, Pageable pageable);
    List<Transaction> findByWalletTransaction_SenderWallet_OwnerIdOrWalletTransaction_ReceiverWallet_OwnerIdOrderByUpdatedDesc(
            String ownerId, String ownerId1, Pageable pageable);
}