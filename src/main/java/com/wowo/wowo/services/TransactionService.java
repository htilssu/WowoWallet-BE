package com.wowo.wowo.services;

import com.wowo.wowo.data.mapper.TransactionMapper;
import com.wowo.wowo.data.dto.response.WalletTransactionDto;
import com.wowo.wowo.data.mapper.WalletTransactionMapperImpl;
import com.wowo.wowo.models.Order;
import com.wowo.wowo.models.Transaction;
import com.wowo.wowo.models.Wallet;
import com.wowo.wowo.repositories.OrderRepository;
import com.wowo.wowo.repositories.TransactionRepository;
import com.wowo.wowo.repositories.WalletTransactionRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final TransactionMapper transactionMapper;
    private final WalletTransactionService walletTransactionService;
    private final WalletTransactionRepository walletTransactionRepository;
    private final WalletTransactionMapperImpl walletTransactionMapperImpl;
    private final OrderRepository orderRepository;

    @Transactional(readOnly = true)
    public WalletTransactionDto getTransactionDetail(String id) {
        var transaction = transactionRepository.findById(id).orElse(null);

        if (transaction == null) {
            throw new RuntimeException("Transaction not found");
        }

        var transactionResponse = transactionMapper.toResponse(transaction);

        if ("wallet".equals(transaction.getTransactionTarget())) {
            var walletTransaction = walletTransactionRepository.findById(transaction.getId()).get();

            return walletTransactionMapperImpl.toDto(walletTransaction);
        }
        // TODO: Xử lý các loại giao dịch khác nếu cần

        return null;
    }

    public void refund(Transaction transaction) {
        if (transaction.getTransactionTarget().equals("wallet")) {
            walletTransactionService.refund(transaction);
        }
        else {
            throw new RuntimeException("Transaction target not found");
        }
    }

    public Transaction createTransaction(String userId,
            Order order,
            Wallet sender,
            Wallet receiver) {
        final Transaction transaction = createTransaction(userId, order);

        if (order.getStatus().equals("SUCCESS")) {
            walletTransactionService.createWalletTransaction(transaction, sender, receiver);
        }

        order.setTransaction(transaction);
        orderRepository.save(order);
        return transaction;
    }

    public Transaction createTransaction(String userId, Order order) {
        var transaction = new Transaction();
        transaction.setSenderId(userId);
        transaction.setSenderType("user");
        transaction.setReceiverId(order.getPartner().getId());
        transaction.setReceiverType("partner");
        transaction.setMoney(order.getMoney());
        transaction.setTransactionTarget("wallet");
        transaction.setStatus(order.getStatus());

        transactionRepository.save(transaction);


        return transaction;
    }
}
