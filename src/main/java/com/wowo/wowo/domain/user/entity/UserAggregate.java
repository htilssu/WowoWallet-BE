package com.wowo.wowo.domain.user.entity;

import com.wowo.wowo.domain.shared.BaseAggregateRoot;
import com.wowo.wowo.domain.user.events.UserCreatedEvent;
import com.wowo.wowo.domain.user.events.UserVerifiedEvent;
import com.wowo.wowo.domain.user.valueobjects.Email;
import com.wowo.wowo.domain.user.valueobjects.UserId;
import com.wowo.wowo.domain.user.valueobjects.UserProfile;
import com.wowo.wowo.domain.shared.valueobjects.Money;
import lombok.Getter;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

/**
 * User Aggregate - Core business entity for user management
 * Encapsulates user business rules and state management
 */
@Getter
public class UserAggregate extends BaseAggregateRoot {
    
    private final UserId id;
    private Email email;
    private UserProfile profile;
    private boolean isActive;
    private boolean isVerified;
    private Money totalMoney;
    private final Instant createdAt;
    private Instant updatedAt;
    
    // Constructor for new users
    public UserAggregate(UserId id, Email email, UserProfile profile) {
        super();
        this.id = id;
        this.email = email;
        this.profile = profile;
        this.isActive = true;
        this.isVerified = false;
        this.totalMoney = Money.zero("VND");
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
        
        // Raise domain event
        addDomainEvent(new UserCreatedEvent(id, email, profile.getFullName()));
    }
    
    // Constructor for loading from persistence
    public UserAggregate(UserId id, Email email, UserProfile profile, boolean isActive, 
                        boolean isVerified, Money totalMoney, Instant createdAt, Instant updatedAt) {
        super();
        this.id = id;
        this.email = email;
        this.profile = profile;
        this.isActive = isActive;
        this.isVerified = isVerified;
        this.totalMoney = totalMoney;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
    
    /**
     * Verify the user account
     */
    public void verify() {
        if (this.isVerified) {
            throw new IllegalStateException("User is already verified");
        }
        
        this.isVerified = true;
        this.updatedAt = Instant.now();
        
        addDomainEvent(new UserVerifiedEvent(id, email));
    }
    
    /**
     * Deactivate the user account
     */
    public void deactivate() {
        if (!this.isActive) {
            throw new IllegalStateException("User is already deactivated");
        }
        
        this.isActive = false;
        this.updatedAt = Instant.now();
    }
    
    /**
     * Activate the user account
     */
    public void activate() {
        if (this.isActive) {
            throw new IllegalStateException("User is already active");
        }
        
        this.isActive = true;
        this.updatedAt = Instant.now();
    }
    
    /**
     * Update user profile
     */
    public void updateProfile(UserProfile newProfile) {
        this.profile = newProfile;
        this.updatedAt = Instant.now();
    }
    
    /**
     * Update total money (for analytics)
     */
    public void updateTotalMoney(Money newTotal) {
        this.totalMoney = newTotal;
        this.updatedAt = Instant.now();
    }
    
    /**
     * Check if user can perform financial operations
     */
    public boolean canPerformFinancialOperations() {
        return isActive && isVerified;
    }
    
    /**
     * Get full name
     */
    public String getFullName() {
        return profile.getFullName();
    }
}