package com.wowo.wowo.controllers;

import com.wowo.wowo.data.dto.response.TransactionResponse;
import com.wowo.wowo.data.mapper.TransactionMapper;
import com.wowo.wowo.models.Transaction;
import com.wowo.wowo.repositories.PartnerRepository;
import com.wowo.wowo.repositories.TransactionRepository;
import com.wowo.wowo.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@AllArgsConstructor
@RestController
@RequestMapping(value = "v1/transaction", produces = "application/json; charset=UTF-8")
public class TransactionController {

    private final TransactionRepository transactionRepository;
    private final TransactionMapper transactionMapper;
    private final TransactionMapper transactionMapperImpl;
    private final UserRepository userRepository;
    private final PartnerRepository partnerRepository;

    @GetMapping("/{id}")
    public ResponseEntity<Object> getTransaction(@PathVariable String id) {
        Transaction transaction = transactionRepository.findById(id).orElse(null);

        if (transaction == null) {
            return ResponseEntity.notFound().build();
        }
        final TransactionResponse transactionResponse = transactionMapper.toResponse(transaction);

        return ResponseEntity.ok(transactionResponse);

    }

    @GetMapping("/history")
    public List<?> getAllTransaction(@RequestParam Map<String, String> allParams,
            Authentication authentication) {
        int offset = Integer.parseInt(allParams.get("offset"));
        int page = Integer.parseInt(allParams.get("page"));
        String id = ((String) authentication.getPrincipal());


//        TODO: Implement this method
        return null;
    }

}
