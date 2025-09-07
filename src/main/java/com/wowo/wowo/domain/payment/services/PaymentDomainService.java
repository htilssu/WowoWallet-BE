package com.wowo.wowo.domain.payment.services;

import com.wowo.wowo.domain.payment.entity.PaymentAggregate;
import com.wowo.wowo.domain.payment.valueobjects.ApplicationId;
import com.wowo.wowo.domain.shared.valueobjects.Money;
import com.wowo.wowo.domain.wallet.entity.WalletAggregate;
import com.wowo.wowo.domain.wallet.repository.WalletRepository;
import com.wowo.wowo.domain.transaction.service.MoneyTransferDomainService;
import com.wowo.wowo.domain.wallet.valueobjects.WalletId;
import com.wowo.wowo.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Payment Domain Service - Orchestrates complex payment operations
 * that span multiple aggregates (Payment, Wallet, Transaction)
 */
@Service
@RequiredArgsConstructor
public class PaymentDomainService {
    
    private final WalletRepository walletRepository;
    private final MoneyTransferDomainService moneyTransferDomainService;
    
    /**
     * Process payment by transferring money from user wallet to application wallet
     * This is a domain service because it coordinates operations across multiple aggregates
     */
    public com.wowo.wowo.domain.payment.valueobjects.TransactionId processPayment(PaymentAggregate payment, WalletId userWalletId, 
                                       WalletId applicationWalletId, String userName, String applicationName) {
        
        // Get wallets
        WalletAggregate userWallet = walletRepository.findByIdOrThrow(userWalletId);
        WalletAggregate applicationWallet = walletRepository.findByIdOrThrow(applicationWalletId);
        
        // Verify user wallet has sufficient balance
        if (!userWallet.hasEnoughBalance(payment.getEffectiveAmount())) {
            throw new IllegalStateException("Insufficient balance for payment");
        }
        
        // Perform the money transfer using the existing domain service
        var transaction = moneyTransferDomainService.transferMoney(
            userWalletId.getValue(), 
            applicationWalletId.getValue(), 
            payment.getEffectiveAmount(),
            userName,
            applicationName,
            String.format("Payment for %s", payment.getServiceName())
        );
        
        return com.wowo.wowo.domain.payment.valueobjects.TransactionId.of(transaction.getId());
    }
    
    /**
     * Validate payment requirements before processing
     */
    public void validatePaymentRequirements(PaymentAggregate payment, WalletId userWalletId) {
        // Check if payment is valid for processing
        if (!payment.canModifyPayment()) {
            throw new IllegalStateException("Payment is not in a valid state for processing");
        }
        
        // Verify user wallet exists and has sufficient balance
        WalletAggregate userWallet = walletRepository.findByIdOrThrow(userWalletId);
        if (!userWallet.hasEnoughBalance(payment.getEffectiveAmount())) {
            throw new IllegalStateException("Insufficient balance for payment");
        }
    }
    
    /**
     * Process refund by returning money from application wallet to user wallet
     */
    public void processRefund(PaymentAggregate payment, WalletId userWalletId, 
                             WalletId applicationWalletId, Money refundAmount,
                             String userName, String applicationName) {
        
        // Get wallets
        WalletAggregate userWallet = walletRepository.findByIdOrThrow(userWalletId);
        WalletAggregate applicationWallet = walletRepository.findByIdOrThrow(applicationWalletId);
        
        // Verify application wallet has sufficient balance for refund
        if (!applicationWallet.hasEnoughBalance(refundAmount)) {
            throw new IllegalStateException("Application wallet has insufficient balance for refund");
        }
        
        // Perform the refund transfer
        moneyTransferDomainService.transferMoney(
            applicationWalletId.getValue(),
            userWalletId.getValue(),
            refundAmount,
            applicationName,
            userName,
            String.format("Refund for payment %s", payment.getId().value())
        );
    }
}