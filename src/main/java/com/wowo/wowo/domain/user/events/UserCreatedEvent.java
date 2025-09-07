package com.wowo.wowo.domain.user.events;

import com.wowo.wowo.domain.shared.BaseDomainEvent;
import com.wowo.wowo.domain.user.valueobjects.Email;
import com.wowo.wowo.domain.user.valueobjects.UserId;
import lombok.Getter;

/**
 * Domain event raised when a user is created
 */
@Getter
public class UserCreatedEvent extends BaseDomainEvent {
    
    private final UserId userId;
    private final Email email;
    private final String fullName;
    
    public UserCreatedEvent(UserId userId, Email email, String fullName) {
        super("UserCreated");
        this.userId = userId;
        this.email = email;
        this.fullName = fullName;
    }
}