package com.wowo.wowo.presentation.payment;

import com.wowo.wowo.application.payment.PaymentApplicationService;
import com.wowo.wowo.domain.payment.entity.PaymentAggregate;
import com.wowo.wowo.domain.payment.valueobjects.ApplicationId;
import com.wowo.wowo.domain.payment.valueobjects.PaymentId;
import com.wowo.wowo.domain.payment.valueobjects.PaymentUrls;
import com.wowo.wowo.domain.shared.valueobjects.Money;
import com.wowo.wowo.domain.wallet.valueobjects.WalletId;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * DDD Payment Controller - Thin layer for payment operations
 */
@RestController
@RequestMapping("/payments")
@RequiredArgsConstructor
@Tag(name = "Payment DDD", description = "Payment operations using Domain-Driven Design")
public class PaymentDddController {
    
    private final PaymentApplicationService paymentApplicationService;
    
    @Operation(summary = "Create a new payment")
    @PostMapping
    public ResponseEntity<PaymentResponse> createPayment(@RequestBody CreatePaymentRequest request) {
        
        PaymentAggregate payment = paymentApplicationService.createPayment(
            ApplicationId.of(request.getApplicationId()),
            new Money(request.getAmount(), request.getCurrency()),
            PaymentUrls.of(request.getReturnUrl(), request.getSuccessUrl()),
            request.getServiceName()
        );
        
        return ResponseEntity.ok(PaymentResponse.from(payment));
    }
    
    @Operation(summary = "Get payment by ID")
    @GetMapping("/{paymentId}")
    public ResponseEntity<PaymentResponse> getPayment(@PathVariable Long paymentId) {
        
        PaymentAggregate payment = paymentApplicationService.getPayment(PaymentId.of(paymentId));
        return ResponseEntity.ok(PaymentResponse.from(payment));
    }
    
    @Operation(summary = "Process payment")
    @PostMapping("/{paymentId}/process")
    public ResponseEntity<PaymentResponse> processPayment(
            @PathVariable Long paymentId,
            @RequestBody ProcessPaymentRequest request) {
        
        PaymentAggregate payment = paymentApplicationService.processPayment(
            PaymentId.of(paymentId),
            WalletId.of(request.getUserWalletId()),
            WalletId.of(request.getApplicationWalletId()),
            request.getUserName(),
            request.getApplicationName()
        );
        
        return ResponseEntity.ok(PaymentResponse.from(payment));
    }
    
    @Operation(summary = "Cancel payment")
    @PostMapping("/{paymentId}/cancel")
    public ResponseEntity<PaymentResponse> cancelPayment(@PathVariable Long paymentId) {
        
        PaymentAggregate payment = paymentApplicationService.cancelPayment(PaymentId.of(paymentId));
        return ResponseEntity.ok(PaymentResponse.from(payment));
    }
    
    @Operation(summary = "Refund payment")
    @PostMapping("/{paymentId}/refund")
    public ResponseEntity<PaymentResponse> refundPayment(
            @PathVariable Long paymentId,
            @RequestParam Long refundAmount,
            @RequestParam(defaultValue = "VND") String currency,
            @RequestBody ProcessPaymentRequest request) {
        
        PaymentAggregate payment = paymentApplicationService.refundPayment(
            PaymentId.of(paymentId),
            new Money(refundAmount, currency),
            WalletId.of(request.getUserWalletId()),
            WalletId.of(request.getApplicationWalletId()),
            request.getUserName(),
            request.getApplicationName()
        );
        
        return ResponseEntity.ok(PaymentResponse.from(payment));
    }
    
    @Operation(summary = "Apply discount to payment")
    @PostMapping("/{paymentId}/discount")
    public ResponseEntity<PaymentResponse> applyDiscount(
            @PathVariable Long paymentId,
            @RequestParam Long discountAmount,
            @RequestParam(defaultValue = "VND") String currency) {
        
        PaymentAggregate payment = paymentApplicationService.applyDiscount(
            PaymentId.of(paymentId),
            new Money(discountAmount, currency)
        );
        
        return ResponseEntity.ok(PaymentResponse.from(payment));
    }
}