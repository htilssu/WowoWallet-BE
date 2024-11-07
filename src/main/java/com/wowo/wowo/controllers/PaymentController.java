package com.wowo.wowo.controllers;

import com.wowo.wowo.annotations.authorized.IsUser;
import com.wowo.wowo.models.Order;
import com.wowo.wowo.services.PaymentService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@ApiResponse(responseCode = "200", description = "Ok")
@ApiResponse(responseCode = "400", description = "Bad request")
@RequestMapping("v1/pay")
@Tag(name = "Payment", description = "Thanh toán")
@AllArgsConstructor
@IsUser
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/{id}")
    public ResponseEntity<?> makePay(@PathVariable Long id, Authentication authentication) {

        final Order order = paymentService.pay(id, authentication);
        return ResponseEntity.ok(order);
    }
}
