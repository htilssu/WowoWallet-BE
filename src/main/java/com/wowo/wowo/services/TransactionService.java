package com.wowo.wowo.services;

import com.wowo.wowo.data.dto.TransactionDto;
import com.wowo.wowo.data.mapper.TransactionMapper;
import com.wowo.wowo.exceptions.NotFoundException;
import com.wowo.wowo.models.FlowType;
import com.wowo.wowo.models.Transaction;
import com.wowo.wowo.models.TransactionVariant;
import com.wowo.wowo.repositories.TransactionRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

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
                        userId, userId, Pageable.ofSize(offset).withPage(page));


        return transactions.stream().map(transactionMapper::toDto).toList();
    }

    public long getTotalTransactions(String userId) {
        return transactionRepository.countByWalletTransaction_SenderWallet_OwnerIdOrWalletTransaction_ReceiverWallet_OwnerId(
                userId, userId);
    }

    public TransactionDto getTransactionDetail(String id, Authentication authentication) {
        String userId = authentication.getPrincipal().toString();
        final Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Giao dịch không tồn tại"));

        var walletTransaction = transaction.getWalletTransaction();
        if (walletTransaction.getReceiverWallet().getOwnerId().equals(userId)) {
            transaction.setType(FlowType.IN);
        }
        return transactionMapper.toDto(transaction);
    }


}
