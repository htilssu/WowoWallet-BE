package com.wowo.wowo.controller;

import com.wowo.wowo.Strategy.AtmCardPaymentStrategy;
import com.wowo.wowo.Strategy.PaypalPaymentStrategy;
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
    private final PaypalPaymentStrategy paypalPaymentStrategy;
    private final AtmCardPaymentStrategy atmCardPaymentStrategy;

    @PostMapping
    public ResponseEntity<String> topUp(@RequestBody @Validated TopUpRequestDTO topUpRequestDTO) {
        try {
            switch (topUpRequestDTO.getMethod()) {
                case PAYPAL:
                    paypalPaymentStrategy.processPayment(topUpRequestDTO);
                    break;
                case ATM_CARD:
                    atmCardPaymentStrategy.processPayment(topUpRequestDTO);
                    break;
                default:
                    throw new BadRequest("Phương thức thanh toán không được hỗ trợ");
            }
            return ResponseEntity.ok("Giao dịch đang được xử lý");
        } catch (BadRequest | InsufficientBalanceException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Internal Server Error");
        }
    }
}