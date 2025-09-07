package com.wowo.wowo.domain.wallet.events;

import com.wowo.wowo.domain.shared.BaseDomainEvent;
import com.wowo.wowo.domain.shared.valueobjects.Money;
import com.wowo.wowo.domain.wallet.valueobjects.WalletId;
import lombok.Getter;

/**
 * Domain event raised when money is deposited to a wallet.
 */
@Getter
public class MoneyDepositedEvent extends BaseDomainEvent {
    
    private final WalletId walletId;
    private final Money amount;
    private final Money previousBalance;
    private final Money newBalance;
    
    public MoneyDepositedEvent(WalletId walletId, Money amount, Money previousBalance, Money newBalance) {
        super("MoneyDeposited");
        this.walletId = walletId;
        this.amount = amount;
        this.previousBalance = previousBalance;
        this.newBalance = newBalance;
    }
}