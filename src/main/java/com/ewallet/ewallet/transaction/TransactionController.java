package com.ewallet.ewallet.transaction;

import com.ewallet.ewallet.dto.mapper.TransactionMapper;
import com.ewallet.ewallet.models.Transaction;
import com.ewallet.ewallet.repository.TransactionRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@AllArgsConstructor
@RestController
@RequestMapping(value = "/api/v1/transaction", produces = "application/json; charset=UTF-8")
public class TransactionController {

    private final TransactionRepository transactionRepository;
    private final TransactionMapper transactionMapper;

    @GetMapping("/{id}")
    public ResponseEntity<Object> getTransaction(@PathVariable String id) {
        Transaction transaction = transactionRepository.findById(id).orElse(null);

        if (transaction == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(transactionMapper.toResponse(transaction));

    }

    @GetMapping("/history")
    public List<?> getAllTransaction(@RequestParam Map<String, String> allParams,
            Authentication authentication) {
        int offset = Integer.parseInt(allParams.get("offset"));
        int page = Integer.parseInt(allParams.get("page"));
        String id = ((String) authentication.getPrincipal());

        return transactionRepository.findBySenderIdOrReceiverId(id, id,
                Pageable.ofSize(offset).withPage(page));

    }

}
