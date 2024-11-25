package com.wowo.wowo.repository;

import com.wowo.wowo.model.Transaction;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, String> {

    List<Transaction> findByIdOrderByUpdatedAsc(String id, Pageable pageable);
    List<Transaction> findByWalletTransaction_SenderWallet_OwnerIdOrWalletTransaction_ReceiverWallet_OwnerIdOrderByUpdatedDesc(
            String ownerId, String ownerId1, Pageable pageable);
    long countByWalletTransaction_SenderWallet_OwnerIdOrWalletTransaction_ReceiverWallet_OwnerId(
            String ownerId,
            String ownerId1);

    //Thống kê
    @Query(value = """
            SELECT 
                COUNT(*) AS total_transactions,
                SUM(amount) AS total_amount,
            
                status,
                COUNT(*) FILTER (WHERE status = 2) AS total_success,
                COUNT(*) FILTER (WHERE status = 1) AS total_pending,
                COUNT(*) FILTER (WHERE status = 3) AS total_cancelled,
                COUNT(*) FILTER (WHERE status = 4) AS total_refunded
            FROM transaction
            GROUP BY status
            """, nativeQuery = true)
    List<Object[]> getTransactionStats();

}