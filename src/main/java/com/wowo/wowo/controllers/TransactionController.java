package com.wowo.wowo.controllers;

import com.wowo.wowo.annotations.authorized.IsUser;
import com.wowo.wowo.data.dto.TransactionDto;
import com.wowo.wowo.data.dto.PagingDto;
import com.wowo.wowo.data.dto.TransactionHistoryResponseDto;
import com.wowo.wowo.data.mapper.TransactionMapper;
import com.wowo.wowo.models.Transaction;
import com.wowo.wowo.repositories.TransactionRepository;
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

    private final TransactionRepository transactionRepository;
    private final TransactionMapper transactionMapper;
    private final TransactionService transactionService;

    @GetMapping("/{id}")
    public ResponseEntity<TransactionDto> getTransaction(@PathVariable String id) {
        final TransactionDto transactionDetail = transactionService.getTransactionDetail(id);
        return ResponseEntity.ok(transactionDetail);
    }

    @GetMapping("/history")
    public TransactionHistoryResponseDto getAllTransaction(@ModelAttribute @Validated PagingDto allParams,
            Authentication authentication) {
        int offset = allParams.getOffset();
        int page = allParams.getPage();
        offset = Math.min(30, Math.max(offset, 0));

        final List<TransactionDto> recentTransactions = transactionService.getRecentTransactions(
                ((String) authentication.getPrincipal()),
                offset,
                page);

        long total = transactionService.getTotalTransactions(
                ((String) authentication.getPrincipal()));
        return new TransactionHistoryResponseDto(recentTransactions, total);
    }
    //lấy lịch sử giao dịch của 1 user nào đó trong hệ thống
    @GetMapping("/{userId}/history")
    public ResponseEntity<TransactionHistoryResponseDto> getAllTransactionsByUserId(
            @ModelAttribute @Validated PagingDto allParams,
            @PathVariable String userId) {

        // Lấy các tham số phân trang từ PagingDto
        int offset = allParams.getOffset();
        int page = allParams.getPage();

        // Đảm bảo offset không vượt quá 30 và không nhỏ hơn 0
        offset = Math.min(30, Math.max(offset, 0));

        // Lấy danh sách giao dịch gần đây của người dùng
        final List<TransactionDto> recentTransactions = transactionService.getRecentTransactions(
                userId, offset, page);

        // Lấy tổng số giao dịch của người dùng
        long total = transactionService.getTotalTransactions(userId);

        TransactionHistoryResponseDto response = new TransactionHistoryResponseDto(recentTransactions, total);

        return ResponseEntity.ok(response);
    }


    @PostMapping("{id}/refund")
    public ResponseEntity<?> refundTransaction(@PathVariable String id) {
        //        transactionService.refundTransaction(id);
        //TODO: Implement refundTransaction
        return ResponseEntity.ok().build();
    }

}
