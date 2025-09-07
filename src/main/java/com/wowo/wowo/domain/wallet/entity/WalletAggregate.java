package com.wowo.wowo.domain.wallet.entity;

import com.wowo.wowo.domain.shared.BaseAggregateRoot;
import com.wowo.wowo.domain.shared.valueobjects.Money;
import com.wowo.wowo.domain.wallet.events.MoneyDepositedEvent;
import com.wowo.wowo.domain.wallet.events.MoneyWithdrawnEvent;
import com.wowo.wowo.domain.wallet.valueobjects.WalletId;
import com.wowo.wowo.exception.InsufficientBalanceException;
import com.wowo.wowo.exception.NegativeMoneyException;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Wallet aggregate root representing a digital wallet in the system.
 * Contains business logic for balance management and transaction validation.
 */
@Entity
@Table(name = "wallet")
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "type", discriminatorType = DiscriminatorType.STRING)
@Getter
@NoArgsConstructor
public class WalletAggregate extends BaseAggregateRoot {
    
    @Id
    @Column(name = "id", nullable = false)
    @SequenceGenerator(name = "wallet_id_seq", sequenceName = "wallet_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "wallet_id_seq")
    private Long id;
    
    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "amount", column = @Column(name = "balance")),
        @AttributeOverride(name = "currency", column = @Column(name = "currency"))
    })
    private Money balance;
    
    @Version
    private Long version;
    
    public WalletAggregate(String currency) {
        this.balance = Money.zero(currency);
    }
    
    public WalletId getWalletId() {
        return WalletId.of(id);
    }
    
    /**
     * Deposits money into the wallet.
     * 
     * @param amount the amount to deposit
     * @throws NegativeMoneyException if the amount is negative or zero
     */
    public void deposit(Money amount) {
        if (amount.isNegative() || amount.isZero()) {
            throw new NegativeMoneyException("Cannot deposit negative or zero amount");
        }
        
        if (!amount.getCurrency().equals(balance.getCurrency())) {
            throw new IllegalArgumentException("Currency mismatch: wallet uses " + 
                balance.getCurrency() + " but trying to deposit " + amount.getCurrency());
        }
        
        Money previousBalance = this.balance;
        this.balance = this.balance.add(amount);
        
        addDomainEvent(new MoneyDepositedEvent(getWalletId(), amount, previousBalance, this.balance));
    }
    
    /**
     * Withdraws money from the wallet.
     * 
     * @param amount the amount to withdraw
     * @throws InsufficientBalanceException if the wallet doesn't have enough balance
     * @throws NegativeMoneyException if the amount is negative or zero
     */
    public void withdraw(Money amount) {
        if (amount.isNegative() || amount.isZero()) {
            throw new NegativeMoneyException("Cannot withdraw negative or zero amount");
        }
        
        if (!amount.getCurrency().equals(balance.getCurrency())) {
            throw new IllegalArgumentException("Currency mismatch: wallet uses " + 
                balance.getCurrency() + " but trying to withdraw " + amount.getCurrency());
        }
        
        if (!hasEnoughBalance(amount)) {
            throw new InsufficientBalanceException("Insufficient balance. Available: " + 
                balance + ", Requested: " + amount);
        }
        
        Money previousBalance = this.balance;
        this.balance = this.balance.subtract(amount);
        
        addDomainEvent(new MoneyWithdrawnEvent(getWalletId(), amount, previousBalance, this.balance));
    }
    
    /**
     * Checks if the wallet has enough balance for a withdrawal.
     * 
     * @param amount the amount to check
     * @return true if the wallet has enough balance
     */
    public boolean hasEnoughBalance(Money amount) {
        return balance.isGreaterThanOrEqual(amount);
    }
    
    /**
     * Gets the current balance of the wallet.
     * 
     * @return the current balance
     */
    public Money getCurrentBalance() {
        return balance;
    }
}