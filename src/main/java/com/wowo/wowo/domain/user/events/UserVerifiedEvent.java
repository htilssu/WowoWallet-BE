package com.wowo.wowo.domain.user.events;

import com.wowo.wowo.domain.shared.BaseDomainEvent;
import com.wowo.wowo.domain.user.valueobjects.Email;
import com.wowo.wowo.domain.user.valueobjects.UserId;
import lombok.Getter;

/**
 * Domain event raised when a user is verified
 */
@Getter
public class UserVerifiedEvent extends BaseDomainEvent {
    
    private final UserId userId;
    private final Email email;
    
    public UserVerifiedEvent(UserId userId, Email email) {
        super("UserVerified");
        this.userId = userId;
        this.email = email;
    }
}