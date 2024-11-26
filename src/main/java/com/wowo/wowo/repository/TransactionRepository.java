package com.wowo.wowo.repository;

import com.wowo.wowo.model.Transaction;
import com.wowo.wowo.model.Wallet;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, String> {

    @Query("SELECT t FROM Transaction t, GroupFund g WHERE (t.senderWallet.id = g.wallet.id OR t" +
            ".receiveWallet.id = g.wallet.id) AND g.id = :groupId ORDER BY t.updated DESC")
    List<Transaction> getGroupFundTransaction(Long groupId, Pageable pageable);
    List<Transaction> findTransactionsByReceiveWalletOrSenderWallet(@NotNull Wallet receiveWallet, @NotNull Wallet senderWallet, Pageable pageable);
    long countTransactionBySenderWalletOrReceiveWallet(@NotNull Wallet senderWallet, @NotNull Wallet receiveWallet);

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
    List<Transaction> findTransactionsByReceiveWalletOrSenderWalletOrderByUpdatedDesc(@NotNull Wallet receiveWallet,
            @NotNull Wallet senderWallet,
            Pageable pageable);
}