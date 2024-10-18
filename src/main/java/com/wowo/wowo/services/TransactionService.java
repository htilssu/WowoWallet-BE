package com.wowo.wowo.services;

import com.wowo.wowo.data.dto.WalletTransactionDto;
import com.wowo.wowo.data.mapper.TransactionMapper;
import com.wowo.wowo.data.mapper.WalletTransactionMapper;
import com.wowo.wowo.models.*;
import com.wowo.wowo.repositories.OrderRepository;
import com.wowo.wowo.repositories.TransactionRepository;
import com.wowo.wowo.repositories.WalletTransactionRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final TransactionMapper transactionMapper;
    private final WalletTransactionService walletTransactionService;
    private final WalletTransactionRepository walletTransactionRepository;
    private final WalletTransactionMapper walletTransactionMapperImpl;
    private final OrderRepository orderRepository;

    @Transactional(readOnly = true)
    public WalletTransactionDto getTransactionDetail(String id) {
        var transaction = transactionRepository.findById(id).orElse(null);

        if (transaction == null) {
            throw new RuntimeException("Transaction not found");
        }

        var transactionResponse = transactionMapper.toResponse(transaction);

        if (transaction.getVariant() == TransactionVariant.WALLET) {
            var walletTransaction = walletTransactionRepository.findById(transaction.getId()).get();

            return walletTransactionMapperImpl.toDto(walletTransaction);
        }
        // TODO: Xử lý các loại giao dịch khác nếu cần

        return null;
    }

    public void refund(Transaction transaction) {
        if (transaction.getVariant() == TransactionVariant.WALLET) {
            walletTransactionService.refund(transaction);
        }
        else {
            throw new RuntimeException("Transaction target not found");
        }
    }

    @Transactional
    public Transaction createTransaction(Transaction transaction) {
        return transactionRepository.save(transaction);
    }

}
