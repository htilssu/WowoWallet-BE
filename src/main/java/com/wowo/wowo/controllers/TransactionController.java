package com.wowo.wowo.controllers;

import com.wowo.wowo.annotations.authorized.IsUser;
import com.wowo.wowo.data.dto.TransactionDto;
import com.wowo.wowo.data.dto.TransactionHistoryParams;
import com.wowo.wowo.data.dto.TransactionHistoryResponseDto;
import com.wowo.wowo.data.mapper.TransactionMapper;
import com.wowo.wowo.exceptions.NotFoundException;
import com.wowo.wowo.models.Transaction;
import com.wowo.wowo.repositories.TransactionRepository;
import com.wowo.wowo.services.TransactionService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping(value = "v1/transaction", produces = "application/json; charset=UTF-8")
@IsUser
@Tag(name = "Transaction", description = "Giao dịch")
public class TransactionController {

    private final TransactionRepository transactionRepository;
    private final TransactionMapper transactionMapper;
    private final TransactionService transactionService;

    @GetMapping("/{id}")
    public ResponseEntity<Object> getTransaction(@PathVariable String id) {
        Transaction transaction = transactionRepository.findById(id).orElse(null);

        if (transaction == null) {
            throw new NotFoundException("Giao dịch không tồn tại");
        }
        final TransactionDto transactionResponse = transactionMapper.toDto(transaction);

        return ResponseEntity.ok(transactionResponse);

    }

    @GetMapping("/history")
    public TransactionHistoryResponseDto getAllTransaction(@ModelAttribute TransactionHistoryParams allParams,
            Authentication authentication) {
        int offset = allParams.getOffset();
        int page = allParams.getPage();
        offset = Math.min(30, Math.max(offset, 0));

        final List<Transaction> recentTransactions = transactionService.getRecentTransactions(
                ((String) authentication.getPrincipal()),
                offset,
                page);

        long total = transactionService.getTotalTransactions(((String) authentication.getPrincipal()));
        return new TransactionHistoryResponseDto(recentTransactions.stream().map(transactionMapper::toDto).toList(),total);
    }

    @PostMapping("{id}/refund")
    public ResponseEntity<?> refundTransaction(@PathVariable String id) {
        //        transactionService.refundTransaction(id);
        //TODO: Implement refundTransaction
        return ResponseEntity.ok().build();
    }

}
