package com.wowo.wowo.presentation.wallet;

import com.wowo.wowo.application.dto.TransferMoneyCommand;
import com.wowo.wowo.application.wallet.WalletApplicationService;
import com.wowo.wowo.domain.transaction.entity.TransactionAggregate;
import com.wowo.wowo.domain.wallet.entity.WalletAggregate;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for wallet operations using DDD architecture.
 * Demonstrates clean separation of concerns with application services.
 */
@RestController
@RequestMapping("/api/v2/wallets")
@RequiredArgsConstructor
@Tag(name = "Wallet DDD", description = "Wallet operations using Domain-Driven Design")
public class WalletDddController {
    
    private final WalletApplicationService walletApplicationService;
    
    @Operation(summary = "Create a new wallet")
    @PostMapping
    public ResponseEntity<WalletResponse> createWallet(@RequestParam(defaultValue = "VND") String currency) {
        WalletAggregate wallet = walletApplicationService.createWallet(currency);
        return ResponseEntity.ok(WalletResponse.from(wallet));
    }
    
    @Operation(summary = "Get wallet by ID")
    @GetMapping("/{walletId}")
    public ResponseEntity<WalletResponse> getWallet(@PathVariable Long walletId) {
        WalletAggregate wallet = walletApplicationService.getWallet(walletId);
        return ResponseEntity.ok(WalletResponse.from(wallet));
    }
    
    @Operation(summary = "Transfer money between wallets")
    @PostMapping("/transfer")
    public ResponseEntity<TransactionResponse> transferMoney(@RequestBody TransferMoneyCommand command) {
        TransactionAggregate transaction = walletApplicationService.transferMoney(command);
        return ResponseEntity.ok(TransactionResponse.from(transaction));
    }
    
    @Operation(summary = "Deposit money to wallet")
    @PostMapping("/{walletId}/deposit")
    public ResponseEntity<TransactionResponse> depositMoney(
            @PathVariable Long walletId,
            @RequestParam Long amount,
            @RequestParam(defaultValue = "VND") String currency,
            @RequestParam(defaultValue = "External") String source) {
        
        TransactionAggregate transaction = walletApplicationService.depositMoney(walletId, amount, currency, source);
        return ResponseEntity.ok(TransactionResponse.from(transaction));
    }
    
    @Operation(summary = "Withdraw money from wallet")
    @PostMapping("/{walletId}/withdraw")
    public ResponseEntity<TransactionResponse> withdrawMoney(
            @PathVariable Long walletId,
            @RequestParam Long amount,
            @RequestParam(defaultValue = "VND") String currency,
            @RequestParam(defaultValue = "External") String destination) {
        
        TransactionAggregate transaction = walletApplicationService.withdrawMoney(walletId, amount, currency, destination);
        return ResponseEntity.ok(TransactionResponse.from(transaction));
    }
}