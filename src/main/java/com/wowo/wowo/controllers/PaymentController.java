package com.wowo.wowo.controllers;

import com.wowo.wowo.annotations.authorized.IsUser;
import com.wowo.wowo.services.PaymentService;
import com.wowo.wowo.services.WalletService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@ApiResponse(responseCode = "200", description = "Ok")
@ApiResponse(responseCode = "400", description = "Bad request")
@RequestMapping("v1/payment")
@Tag(name = "Payment", description = "Thanh toán")
@AllArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;
    private final WalletService walletService;

    @PostMapping("/{id}")
    @IsUser
    public ResponseEntity<?> makePay(@PathVariable String id,
            @RequestBody String sourceId,
            Authentication authentication) {

        if (!walletService.isWalletOwner((String) authentication.getPrincipal(), sourceId)) {
            throw new RuntimeException("Không tìm thấy ví");
        }

        paymentService.makePayment(id, sourceId);
        return ResponseEntity.ok().build();
    }
}
