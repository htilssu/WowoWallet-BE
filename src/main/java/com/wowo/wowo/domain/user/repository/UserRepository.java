package com.wowo.wowo.domain.user.repository;

import com.wowo.wowo.domain.user.entity.UserAggregate;
import com.wowo.wowo.domain.user.valueobjects.Email;
import com.wowo.wowo.domain.user.valueobjects.UserId;
import com.wowo.wowo.exception.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Domain repository interface for User operations
 */
public interface UserRepository {
    
    /**
     * Save user aggregate
     */
    UserAggregate save(UserAggregate user);
    
    /**
     * Find user by ID
     */
    Optional<UserAggregate> findById(UserId userId);
    
    /**
     * Find user by ID or throw exception
     */
    default UserAggregate findByIdOrThrow(UserId userId) {
        return findById(userId)
            .orElseThrow(() -> new NotFoundException("User not found: " + userId.value()));
    }
    
    /**
     * Find user by email
     */
    Optional<UserAggregate> findByEmail(Email email);
    
    /**
     * Find user by email, username, or ID
     */
    Optional<UserAggregate> findByEmailOrUsernameOrId(String email, String username, String id);
    
    /**
     * Check if user exists by ID
     */
    boolean existsById(UserId userId);
    
    /**
     * Check if email exists
     */
    boolean existsByEmail(Email email);
    
    /**
     * Get all users with pagination
     */
    Page<UserAggregate> findAll(Pageable pageable);
}