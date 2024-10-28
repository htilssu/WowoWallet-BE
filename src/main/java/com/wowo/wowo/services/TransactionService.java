package com.wowo.wowo.services;

import com.wowo.wowo.data.dto.TransactionDto;
import com.wowo.wowo.data.mapper.TransactionMapper;
import com.wowo.wowo.exceptions.NotFoundException;
import com.wowo.wowo.models.Transaction;
import com.wowo.wowo.models.TransactionVariant;
import com.wowo.wowo.repositories.OrderRepository;
import com.wowo.wowo.repositories.TransactionRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final TransactionMapper transactionMapper;
    private final OrderRepository orderRepository;
    private final ReceiverService receiverService;

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

    public List<Transaction> getRecentTransactions(String userId, int offset, int page) {
        return transactionRepository.findByWalletTransaction_SenderWallet_OwnerIdOrWalletTransaction_ReceiverWallet_OwnerIdOrderByUpdatedDesc(
                userId, userId, Pageable.ofSize(offset).withPage(page));
    }

    public long getTotalTransactions(String userId) {
        return transactionRepository.countByWalletTransaction_SenderWallet_OwnerIdOrWalletTransaction_ReceiverWallet_OwnerId(
                userId, userId);
    }

    public TransactionDto getTransactionDetail(String id) {
        Transaction transaction = transactionRepository.findById(id).orElseThrow(
                () -> new NotFoundException("Transaction not found"));
        if (transaction == null) {
            throw new RuntimeException("Transaction not found");
        }
        final TransactionDto transactionDto = transactionMapper.toDto(transaction);

        transactionDto.setReceiver(receiverService.getReceiver(transaction));

        return transactionDto;
    }
}
