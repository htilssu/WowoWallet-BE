package com.wowo.wowo.service;

import com.wowo.wowo.data.dto.TransactionDTO;
import com.wowo.wowo.data.mapper.TransactionMapper;
import com.wowo.wowo.exception.NotFoundException;
import com.wowo.wowo.model.FlowType;
import com.wowo.wowo.model.Transaction;
import com.wowo.wowo.repository.TransactionRepository;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
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
    private final UserService userService;

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
        var user = userService.getUserByIdOrElseThrow(userId);
        var transactions =
                transactionRepository.findTransactionsByReceiveWalletOrSenderWallet(
                        user.getWallet(), user.getWallet(), Pageable.ofSize(offset)
                                .withPage(page));

        transactions = transactions.stream()
                .peek(transaction -> {
                    if (transaction.getFlowType() == FlowType.TRANSFER_MONEY && transaction
                            .getReceiveWallet()
                            .equals(user.getWallet())) {
                        transaction.setFlowType(FlowType.RECEIVE_MONEY);
                    }
                })
                .toList();


        return transactions.stream()
                .map(transactionMapper::toDto)
                .toList();
    }

    public long getTotalTransactions(String userId) {
        var user = userService.getUserByIdOrElseThrow(userId);
        return transactionRepository.countTransactionBySenderWalletOrReceiveWallet(
                user.getWallet(), user.getWallet());
    }

    public TransactionDTO getTransactionDetail(String id, Authentication authentication) {
        String userId = authentication.getPrincipal()
                .toString();
        var user = userService.getUserByIdOrElseThrow(userId);
        final Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Giao dịch không tồn tại"));

        if (transaction.getReceiveWallet()
                .equals(user.getWallet())) {
            transaction.setFlowType(FlowType.RECEIVE_MONEY);
        }
        return transactionMapper.toDto(transaction);
    }

    public List<Transaction> getGroupFundTransaction(Long groupId,
            @Min(0) @NotNull Integer offset,
            @Min(0) @NotNull Integer page) {

        return transactionRepository.getGroupFundTransaction(groupId, Pageable.ofSize(offset)
                .withPage(page));
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
