package com.wowo.wowo.presentation.wallet;

import com.wowo.wowo.application.facade.WalletMigrationFacade;
import com.wowo.wowo.model.Wallet;
import com.wowo.wowo.service.WalletService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controller demonstrating the migration from legacy architecture to DDD.
 * Shows how both approaches can coexist during the transition period.
 */
@RestController
@RequestMapping("/api/migration")
@RequiredArgsConstructor
@Tag(name = "Migration Demo", description = "Demonstrates migration from legacy to DDD architecture")
public class MigrationDemoController {
    
    private final WalletService legacyWalletService;
    private final WalletMigrationFacade migrationFacade;
    
    @Operation(summary = "Create wallet using legacy approach")
    @PostMapping("/legacy/wallet")
    public ResponseEntity<Wallet> createWalletLegacy() {
        Wallet wallet = legacyWalletService.createWallet();
        return ResponseEntity.ok(wallet);
    }
    
    @Operation(summary = "Create wallet using DDD backend but legacy response format")
    @PostMapping("/ddd/wallet")
    public ResponseEntity<Wallet> createWalletDdd(@RequestParam(defaultValue = "VND") String currency) {
        Wallet wallet = migrationFacade.createWalletWithDddBackend(currency);
        return ResponseEntity.ok(wallet);
    }
    
    @Operation(summary = "Demonstrate hybrid operation using both legacy and DDD")
    @GetMapping("/hybrid/{walletId}")
    public ResponseEntity<String> hybridOperation(
            @PathVariable Long walletId,
            @RequestParam(defaultValue = "10000") Long amount) {
        String result = migrationFacade.performHybridOperation(walletId, amount);
        return ResponseEntity.ok(result);
    }
    
    @Operation(summary = "Test migration from legacy to DDD approach")
    @PostMapping("/test/{walletId}")
    public ResponseEntity<String> testMigration(
            @PathVariable Long walletId,
            @RequestParam(defaultValue = "5000") Long depositAmount) {
        String result = migrationFacade.migrateWalletOperation(walletId, depositAmount);
        return ResponseEntity.ok(result);
    }
    
    @Operation(summary = "Get wallet using legacy service")
    @GetMapping("/legacy/wallet/{walletId}")
    public ResponseEntity<Wallet> getWalletLegacy(@PathVariable Long walletId) {
        Wallet wallet = legacyWalletService.getWallet(walletId);
        return ResponseEntity.ok(wallet);
    }
}