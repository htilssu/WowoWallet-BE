package com.wowo.wowo.controllers;

import com.wowo.wowo.annotations.authorized.IsUser;
import com.wowo.wowo.data.dto.PaymentDto;
import com.wowo.wowo.models.Order;
import com.wowo.wowo.services.PaymentService;
import com.wowo.wowo.services.PaypalService;
import com.wowo.wowo.services.WalletService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<?> makePay(@PathVariable Long id,
            @RequestBody @Valid PaymentDto paymentDto) {

        paymentDto.setOrderId(id);
        final Order order = paymentService.pay(paymentDto);
        return ResponseEntity.ok(order);
    }
}
