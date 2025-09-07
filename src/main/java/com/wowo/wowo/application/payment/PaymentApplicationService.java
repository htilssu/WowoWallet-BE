package com.wowo.wowo.application.payment;

import com.wowo.wowo.domain.payment.entity.PaymentAggregate;
import com.wowo.wowo.domain.payment.repository.PaymentRepository;
import com.wowo.wowo.domain.payment.services.PaymentDomainService;
import com.wowo.wowo.domain.payment.valueobjects.*;
import com.wowo.wowo.domain.shared.valueobjects.Money;
import com.wowo.wowo.domain.wallet.valueobjects.WalletId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Application Service for Payment operations
 * Orchestrates domain operations and manages transaction boundaries
 */
@Service
@RequiredArgsConstructor
@Transactional
public class PaymentApplicationService {
    
    private final PaymentRepository paymentRepository;
    private final PaymentDomainService paymentDomainService;
    
    /**
     * Create a new payment
     */
    public PaymentAggregate createPayment(ApplicationId applicationId, Money amount, 
                                         PaymentUrls paymentUrls, String serviceName) {
        
        PaymentId paymentId = paymentRepository.nextId();
        
        PaymentAggregate payment = new PaymentAggregate(
            paymentId, 
            applicationId, 
            amount, 
            paymentUrls, 
            serviceName
        );
        
        return paymentRepository.save(payment);
    }
    
    /**
     * Process payment - complete payment flow with money transfer
     */
    public PaymentAggregate processPayment(PaymentId paymentId, WalletId userWalletId, 
                                          WalletId applicationWalletId, String userName, String applicationName) {
        
        PaymentAggregate payment = paymentRepository.findByIdOrThrow(paymentId);
        
        // Validate payment can be processed
        paymentDomainService.validatePaymentRequirements(payment, userWalletId);
        
        // Process the payment (transfer money)
        com.wowo.wowo.domain.payment.valueobjects.TransactionId transactionId = paymentDomainService.processPayment(
            payment, userWalletId, applicationWalletId, userName, applicationName
        );
        
        // Complete the payment
        payment.completePayment(transactionId);
        
        return paymentRepository.save(payment);
    }
    
    /**
     * Cancel payment
     */
    public PaymentAggregate cancelPayment(PaymentId paymentId) {
        PaymentAggregate payment = paymentRepository.findByIdOrThrow(paymentId);
        payment.cancel();
        return paymentRepository.save(payment);
    }
    
    /**
     * Refund payment
     */
    public PaymentAggregate refundPayment(PaymentId paymentId, Money refundAmount,
                                         WalletId userWalletId, WalletId applicationWalletId,
                                         String userName, String applicationName) {
        
        PaymentAggregate payment = paymentRepository.findByIdOrThrow(paymentId);
        
        // Process the refund transfer
        paymentDomainService.processRefund(
            payment, userWalletId, applicationWalletId, refundAmount, userName, applicationName
        );
        
        // Mark payment as refunded
        payment.refund(refundAmount);
        
        return paymentRepository.save(payment);
    }
    
    /**
     * Get payment by ID
     */
    @Transactional(readOnly = true)
    public PaymentAggregate getPayment(PaymentId paymentId) {
        return paymentRepository.findByIdOrThrow(paymentId);
    }
    
    /**
     * Apply discount to payment
     */
    public PaymentAggregate applyDiscount(PaymentId paymentId, Money discountAmount) {
        PaymentAggregate payment = paymentRepository.findByIdOrThrow(paymentId);
        payment.applyDiscount(discountAmount);
        return paymentRepository.save(payment);
    }
}