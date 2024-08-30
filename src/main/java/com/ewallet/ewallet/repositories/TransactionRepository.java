package com.ewallet.ewallet.repositories;

import com.ewallet.ewallet.models.Transaction;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, String> {

    List<Transaction> findBySenderIdOrReceiverId(String senderId,
            String receiverId,
            Pageable pageable);
}
