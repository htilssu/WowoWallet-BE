package com.wowo.wowo.controller;

import com.wowo.wowo.strategy.PaymentStrategy;
import com.wowo.wowo.strategy.PaymentStrategyFactory;
import com.wowo.wowo.data.dto.TopUpRequestDTO;
import com.wowo.wowo.exception.BadRequest;
import com.wowo.wowo.exception.InsufficientBalanceException;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/top-up")
@AllArgsConstructor
public class TopUpController {
    private final PaymentStrategyFactory paymentStrategyFactory;

    @PostMapping
    public ResponseEntity<String> topUp(@RequestBody @Validated TopUpRequestDTO topUpRequestDTO) {
        try {
            PaymentStrategy strategy = paymentStrategyFactory.getStrategy(topUpRequestDTO.getMethod());
            strategy.processPayment(topUpRequestDTO);
            return ResponseEntity.ok("Giao dịch đang được xử lý");
        } catch (BadRequest | InsufficientBalanceException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Internal Server Error");
        }
    }
}