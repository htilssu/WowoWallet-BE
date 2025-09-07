package com.wowo.wowo.domain.wallet.valueobjects;

import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Value object representing a wallet identifier.
 */
@Embeddable
@Getter
@EqualsAndHashCode
@NoArgsConstructor
public class WalletId {
    
    private Long value;
    
    public WalletId(Long value) {
        if (value == null || value <= 0) {
            throw new IllegalArgumentException("Wallet ID must be a positive number");
        }
        this.value = value;
    }
    
    public static WalletId of(Long value) {
        return new WalletId(value);
    }
    
    @Override
    public String toString() {
        return value.toString();
    }
}