package com.wowo.wowo.domain.transaction.entity;

import com.wowo.wowo.annotation.id_generator.TransactionIdSequence;
import com.wowo.wowo.domain.shared.BaseAggregateRoot;
import com.wowo.wowo.domain.shared.valueobjects.Money;
import com.wowo.wowo.domain.transaction.events.TransactionCompletedEvent;
import com.wowo.wowo.domain.transaction.valueobjects.TransactionId;
import com.wowo.wowo.domain.transaction.valueobjects.TransactionType;
import com.wowo.wowo.domain.wallet.valueobjects.WalletId;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.Instant;

/**
 * Transaction aggregate root representing a financial transaction in the system.
 */
@Entity
@Table(name = "transaction")
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "type", discriminatorType = DiscriminatorType.STRING)
@Getter
@NoArgsConstructor
public class TransactionAggregate extends BaseAggregateRoot {
    
    @Id
    @TransactionIdSequence
    @Column(name = "id", nullable = false, length = 40)
    private String id;
    
    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "amount", column = @Column(name = "amount")),
        @AttributeOverride(name = "currency", column = @Column(name = "currency"))
    })
    private Money amount;
    
    @Column(name = "receiver_name")
    private String receiverName;
    
    @Column(name = "sender_name")
    private String senderName;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "flow_type", nullable = false)
    private TransactionType transactionType;
    
    @Column(length = 300)
    private String message;
    
    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "source_wallet"))
    private WalletId sourceWalletId;
    
    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "receive_wallet"))
    private WalletId targetWalletId;
    
    @CreatedDate
    @Column(name = "created", nullable = false)
    private Instant created = Instant.now();
    
    @LastModifiedDate
    @Column(name = "updated", nullable = false)
    private Instant updated = Instant.now();
    
    public TransactionAggregate(Money amount, 
                               WalletId sourceWalletId, 
                               WalletId targetWalletId,
                               TransactionType transactionType, 
                               String senderName,
                               String receiverName,
                               String message) {
        if (amount == null) {
            throw new IllegalArgumentException("Amount cannot be null");
        }
        if (amount.isNegative() || amount.isZero()) {
            throw new IllegalArgumentException("Amount must be positive");
        }
        if (sourceWalletId == null) {
            throw new IllegalArgumentException("Source wallet ID cannot be null");
        }
        if (targetWalletId == null) {
            throw new IllegalArgumentException("Target wallet ID cannot be null");
        }
        if (transactionType == null) {
            throw new IllegalArgumentException("Transaction type cannot be null");
        }
        
        this.amount = amount;
        this.sourceWalletId = sourceWalletId;
        this.targetWalletId = targetWalletId;
        this.transactionType = transactionType;
        this.senderName = senderName;
        this.receiverName = receiverName;
        this.message = message;
        this.created = Instant.now();
        this.updated = Instant.now();
    }
    
    public TransactionId getTransactionId() {
        return TransactionId.of(id);
    }
    
    /**
     * Marks the transaction as completed and raises a domain event.
     */
    public void complete() {
        this.updated = Instant.now();
        
        addDomainEvent(new TransactionCompletedEvent(
            getTransactionId(),
            sourceWalletId,
            targetWalletId,
            amount,
            transactionType,
            message
        ));
    }
    
    /**
     * Checks if this is a transfer between different wallets.
     */
    public boolean isTransfer() {
        return !sourceWalletId.equals(targetWalletId);
    }
    
    /**
     * Gets the transaction amount.
     */
    public Money getTransactionAmount() {
        return amount;
    }
}