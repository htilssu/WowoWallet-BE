package com.wowo.wowo.controllers;

import com.paypal.sdk.exceptions.ApiException;
import com.wowo.wowo.annotations.authorized.IsUser;
import com.wowo.wowo.services.PaymentService;
import com.wowo.wowo.services.PaypalService;
import com.wowo.wowo.services.WalletService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@ApiResponse(responseCode = "200", description = "Ok")
@ApiResponse(responseCode = "400", description = "Bad request")
@RequestMapping("v1/payment")
@Tag(name = "Payment", description = "Thanh toán")
@AllArgsConstructor
@IsUser
public class PaymentController {

    private final PaymentService paymentService;
    private final WalletService walletService;
    private final PaypalService paypalService;

    @PostMapping("/{id}")
    public ResponseEntity<?> makePay(@PathVariable String id,
            @RequestBody String sourceId,
            Authentication authentication) {

        if (!walletService.isWalletOwner((String) authentication.getPrincipal(), sourceId)) {
            throw new RuntimeException("Không tìm thấy ví");
        }

        paymentService.makePayment(id, sourceId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/paypal")
    public ResponseEntity<?> makePaypalPayment(
            Authentication authentication) throws IOException, ApiException {

        return ResponseEntity.ok(paypalService.createOrder());
    }
}
