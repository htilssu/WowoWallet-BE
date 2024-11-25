package com.wowo.wowo.service;

import com.wowo.wowo.data.dto.TransactionDto;
import com.wowo.wowo.data.mapper.TransactionMapper;
import com.wowo.wowo.exception.NotFoundException;
import com.wowo.wowo.model.FlowType;
import com.wowo.wowo.model.PaymentStatus;
import com.wowo.wowo.model.Transaction;
import com.wowo.wowo.model.TransactionVariant;
import com.wowo.wowo.repository.TransactionRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final TransactionMapper transactionMapper;

    public void refund(Transaction transaction) {
        if (transaction.getVariant() == TransactionVariant.WALLET) {
            //Todo: implement refund
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

    public List<TransactionDto> getRecentTransactions(String userId, int offset, int page) {
        var transactions =
                transactionRepository.findByWalletTransaction_SenderWallet_OwnerIdOrWalletTransaction_ReceiverWallet_OwnerIdOrderByUpdatedDesc(
                        userId, userId, Pageable.ofSize(offset)
                                .withPage(page));

        transactions = transactions.stream()
                .peek(transaction -> {
                    if (transaction.getWalletTransaction()
                            .getReceiverWallet()
                            .getOwnerId()
                            .equals(userId)) {
                        transaction.setType(FlowType.IN);
                    }
                })
                .toList();
        return transactions.stream()
                .map(transactionMapper::toDto)
                .toList();
    }

    public long getTotalTransactions(String userId) {
        return transactionRepository.countByWalletTransaction_SenderWallet_OwnerIdOrWalletTransaction_ReceiverWallet_OwnerId(
                userId, userId);
    }

    public TransactionDto getTransactionDetail(String id, Authentication authentication) {
        String userId = authentication.getPrincipal()
                .toString();
        final Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Giao dịch không tồn tại"));

        var walletTransaction = transaction.getWalletTransaction();
        if (walletTransaction.getReceiverWallet()
                .getOwnerId()
                .equals(userId)) {
            transaction.setType(FlowType.IN);
        }
        return transactionMapper.toDto(transaction);
    }

    //Thống kê
    public List<Map<String, Object>> getTransactionStats() {
        List<Object[]> stats = transactionRepository.getTransactionStats();
        List<Map<String, Object>> results = new ArrayList<>();

        for (Object[] row : stats) {
            Map<String, Object> data = new HashMap<>();
            data.put("totalTransactions", row[0]);
            data.put("totalAmount", row[1]);
            data.put("status", row[2]);
            results.add(data);
        }

        return results;
    }

}
