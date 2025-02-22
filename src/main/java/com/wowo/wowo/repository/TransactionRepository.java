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
            flow_type,
            COUNT(CASE WHEN flow_type = 'TRANSFER_MONEY' THEN 1 END) AS total_transfer,
            COUNT(CASE WHEN flow_type = 'RECEIVE_MONEY' THEN 1 END) AS total_receive,
            COUNT(CASE WHEN flow_type = 'TOP_UP' THEN 1 END) AS total_top_up,
            COUNT(CASE WHEN flow_type = 'WITHDRAW' THEN 1 END) AS total_withdraw,
            COUNT(CASE WHEN flow_type = 'TOP_UP_GROUP_FUND' THEN 1 END) AS total_top_up_group_fund,
            COUNT(CASE WHEN flow_type = 'WITHDRAW_GROUP_FUND' THEN 1 END) AS total_withdraw_group_fund
        FROM transaction
        GROUP BY flow_type
        """, nativeQuery = true)
    List<Object[]> getTransactionStats();

    List<Transaction> findTransactionsByReceiveWalletOrSenderWalletOrderByUpdatedDesc(@NotNull Wallet receiveWallet,
            @NotNull Wallet senderWallet,
            Pageable pageable);
}