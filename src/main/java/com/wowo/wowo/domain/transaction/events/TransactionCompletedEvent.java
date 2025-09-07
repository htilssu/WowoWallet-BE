package com.wowo.wowo.domain.transaction.events;

import com.wowo.wowo.domain.shared.BaseDomainEvent;
import com.wowo.wowo.domain.shared.valueobjects.Money;
import com.wowo.wowo.domain.transaction.valueobjects.TransactionId;
import com.wowo.wowo.domain.transaction.valueobjects.TransactionType;
import com.wowo.wowo.domain.wallet.valueobjects.WalletId;
import lombok.Getter;

/**
 * Domain event raised when a transaction is completed.
 */
@Getter
public class TransactionCompletedEvent extends BaseDomainEvent {
    
    private final TransactionId transactionId;
    private final WalletId sourceWalletId;
    private final WalletId targetWalletId;
    private final Money amount;
    private final TransactionType transactionType;
    private final String message;
    
    public TransactionCompletedEvent(TransactionId transactionId, 
                                   WalletId sourceWalletId, 
                                   WalletId targetWalletId,
                                   Money amount, 
                                   TransactionType transactionType,
                                   String message) {
        super("TransactionCompleted");
        this.transactionId = transactionId;
        this.sourceWalletId = sourceWalletId;
        this.targetWalletId = targetWalletId;
        this.amount = amount;
        this.transactionType = transactionType;
        this.message = message;
    }
}