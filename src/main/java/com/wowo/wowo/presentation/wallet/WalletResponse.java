package com.wowo.wowo.presentation.wallet;

import com.wowo.wowo.domain.wallet.entity.WalletAggregate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Response DTO for wallet operations.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WalletResponse {
    
    private Long id;
    private Long balance;
    private String currency;
    private Long version;
    
    public static WalletResponse from(WalletAggregate wallet) {
        return new WalletResponse(
            wallet.getId(),
            wallet.getCurrentBalance().getAmount(),
            wallet.getCurrentBalance().getCurrency(),
            wallet.getVersion()
        );
    }
}