package com.wowo.wowo.services;

import com.wowo.wowo.data.mapper.TransactionMapper;
import com.wowo.wowo.models.Transaction;
import com.wowo.wowo.models.TransactionVariant;
import com.wowo.wowo.models.WalletTransaction;
import com.wowo.wowo.repositories.OrderRepository;
import com.wowo.wowo.repositories.TransactionRepository;
import com.wowo.wowo.repositories.WalletTransactionRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final TransactionMapper transactionMapper;
    private final OrderRepository orderRepository;


    public void refund(Transaction transaction) {
        if (transaction.getVariant() == TransactionVariant.WALLET) {
            //TODo: implement refund
        }
        else {
            throw new RuntimeException("Transaction target not found");
        }
    }

    public Transaction createTransaction(Transaction transaction) {
        try {
            return transactionRepository.save(transaction);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public List<?> getRecentTransactions(String principal, int offset, int page) {
        return List.of();
    }
}
