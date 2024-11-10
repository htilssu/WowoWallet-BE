package com.wowo.wowo.controllers;

import com.wowo.wowo.annotations.authorized.IsUser;
import com.wowo.wowo.data.dto.PagingDto;
import com.wowo.wowo.data.dto.TransactionDto;
import com.wowo.wowo.data.dto.TransactionHistoryResponseDto;
import com.wowo.wowo.services.TransactionService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping(value = "v1/transaction", produces = "application/json; charset=UTF-8")
@IsUser
@Tag(name = "Transaction", description = "Giao dịch")
public class TransactionController {

    private final TransactionService transactionService;

    @GetMapping("/{id}")
    public ResponseEntity<TransactionDto> getTransaction(@PathVariable String id,
            Authentication authentication) {
        final TransactionDto transactionDetail = transactionService.getTransactionDetail(id,
                authentication);
        return ResponseEntity.ok(transactionDetail);
    }

    @GetMapping("/history")
    public TransactionHistoryResponseDto getAllTransaction(@ModelAttribute @Validated PagingDto allParams,
            Authentication authentication) {
        return getTransactionHistory(allParams, authentication.getPrincipal().toString());
    }

    private TransactionHistoryResponseDto getTransactionHistory(PagingDto allParams,
            String userId) {
        int offset = allParams.getOffset();
        int page = allParams.getPage();
        offset = Math.min(30, Math.max(offset, 0));

        final List<TransactionDto> recentTransactions = transactionService.getRecentTransactions(
                (userId),
                offset,
                page);

        long total = transactionService.getTotalTransactions(
                (userId));
        return new TransactionHistoryResponseDto(recentTransactions, total);
    }


    //    @IsAdmin
    @GetMapping("/{userId}/history")
    public TransactionHistoryResponseDto getAllTransactionsByUserId(
            @ModelAttribute @Validated PagingDto allParams,
            @PathVariable String userId) {

        return getTransactionHistory(allParams, userId);
    }

    @PostMapping("{id}/refund")
    public ResponseEntity<?> refundTransaction(@PathVariable String id) {
        //TODO: Implement refundTransaction
        return ResponseEntity.ok().build();
    }

}
