package com.wowo.wowo.service;

import com.wowo.wowo.data.dto.TransactionDTO;
import com.wowo.wowo.data.mapper.TransactionMapper;
import com.wowo.wowo.exception.NotFoundException;
import com.wowo.wowo.model.FlowType;
import com.wowo.wowo.model.Transaction;
import com.wowo.wowo.repository.TransactionRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final TransactionMapper transactionMapper;

    public void refund(Transaction transaction) {
       //TODO: implement refund
    }

    public Transaction save(Transaction transaction) {
        try {
            return transactionRepository.save(transaction);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public List<TransactionDTO> getRecentTransactions(String userId, int offset, int page) {
        var transactions =
                transactionRepository.findByTransaction_SenderWallet_OwnerIdOrTransaction_ReceiverWallet_OwnerIdOrderByUpdatedDesc(
                        userId, userId, Pageable.ofSize(offset)
                                .withPage(page));

        transactions = transactions.stream()
                .peek(transaction -> {
                    if (transaction.getTransaction()
                            .getReceiverUserWallet()
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
        return transactionRepository.countByTransaction_SenderWallet_OwnerIdOrTransaction_ReceiverWallet_OwnerId(
                userId, userId);
    }

    public TransactionDTO getTransactionDetail(String id, Authentication authentication) {
        String userId = authentication.getPrincipal()
                .toString();
        final Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Giao dịch không tồn tại"));

        var walletTransaction = transaction.getTransaction();
        if (walletTransaction.getReceiverUserWallet()
                .getOwnerId()
                .equals(userId)) {
            transaction.setFlowType(FlowType.IN);
        }
        return transactionMapper.toDto(transaction);
    }

}
