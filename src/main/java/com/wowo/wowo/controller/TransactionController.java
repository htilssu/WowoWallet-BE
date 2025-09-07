package com.wowo.wowo.controller;

import com.wowo.wowo.annotation.authorized.IsUser;
import com.wowo.wowo.data.dto.PagingDTO;
import com.wowo.wowo.data.dto.TransactionDTO;
import com.wowo.wowo.data.dto.TransactionHistoryResponseDTO;
import com.wowo.wowo.service.TransactionService;
import com.wowo.wowo.model.PaymentStatus;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping(value = "transaction", produces = "application/json; charset=UTF-8")
@IsUser
@Tag(name = "Transaction", description = "Giao dịch")
public class TransactionController {

    private final TransactionService transactionService;

    @GetMapping("/{id}")
    public ResponseEntity<TransactionDTO> getTransaction(@PathVariable String id,
            Authentication authentication) {
        log.info("User with id {} is getting transaction with id {}", authentication.getPrincipal(),
                id);
        final TransactionDTO transactionDetail = transactionService.getTransactionDetail(id,
                authentication);
        return ResponseEntity.ok(transactionDetail);
    }

    @GetMapping("/history")
    public TransactionHistoryResponseDTO getAllTransaction(@ModelAttribute @Validated PagingDTO allParams,
            Authentication authentication) {
        return getTransactionHistory(allParams, authentication.getPrincipal()
                .toString());
    }

    private TransactionHistoryResponseDTO getTransactionHistory(PagingDTO allParams,
            String userId) {
        int offset = allParams.getOffset();
        int page = allParams.getPage();
        offset = Math.min(30, Math.max(offset, 0));

        final List<TransactionDTO> recentTransactions = transactionService.getRecentTransactions(
                userId,
                offset,
                page);

        long total = transactionService.getTotalTransactions(
                (userId));
        return new TransactionHistoryResponseDTO(recentTransactions, total);
    }

    //    @IsAdmin
    @GetMapping("/{userId}/history")
    public TransactionHistoryResponseDTO getAllTransactionsByUserId(
            @ModelAttribute @Validated PagingDTO allParams,
            @PathVariable String userId) {

        return getTransactionHistory(allParams, userId);
    }

    @PostMapping("{id}/refund")
    public ResponseEntity<?> refundTransaction(@PathVariable String id) {
        //TODO: Implement refundTransaction
        return ResponseEntity.ok()
                .build();
    }

    //Thống kê
    @GetMapping("/stats")
    public List<Map<String, Object>> getTransactionStats() {
        return transactionService.getTransactionStats();
    }

}
