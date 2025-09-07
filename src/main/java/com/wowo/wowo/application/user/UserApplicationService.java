package com.wowo.wowo.application.user;

import com.wowo.wowo.domain.user.entity.UserAggregate;
import com.wowo.wowo.domain.user.repository.UserRepository;
import com.wowo.wowo.domain.user.valueobjects.Email;
import com.wowo.wowo.domain.user.valueobjects.UserId;
import com.wowo.wowo.domain.user.valueobjects.UserProfile;
import com.wowo.wowo.domain.shared.valueobjects.Money;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.List;

/**
 * User Application Service - Orchestrates user domain operations
 * Provides use cases for user management with proper transaction boundaries
 */
@Service
@RequiredArgsConstructor
@Transactional
public class UserApplicationService {
    
    private final UserRepository userRepository;
    
    /**
     * Create a new user
     */
    public UserId createUser(String email, String firstName, String lastName, String phoneNumber) {
        // Validate email uniqueness
        if (userRepository.findByEmail(new Email(email)).isPresent()) {
            throw new IllegalArgumentException("Email already exists: " + email);
        }
        
        UserProfile profile = new UserProfile(firstName, lastName, phoneNumber);
        UserAggregate user = new UserAggregate(
            UserId.generate(),
            new Email(email),
            profile
        );
        
        userRepository.save(user);
        return user.getId();
    }
    
    /**
     * Get user by ID
     */
    @Transactional(readOnly = true)
    public Optional<UserAggregate> getUserById(UserId userId) {
        return userRepository.findById(userId);
    }
    
    /**
     * Get user by email
     */
    @Transactional(readOnly = true)
    public Optional<UserAggregate> getUserByEmail(String email) {
        return userRepository.findByEmail(new Email(email));
    }
    
    /**
     * Verify user account
     */
    public void verifyUser(UserId userId) {
        UserAggregate user = userRepository.findById(userId)
            .orElseThrow(() -> new IllegalArgumentException("User not found: " + userId));
        
        user.verify();
        userRepository.save(user);
    }
    
    /**
     * Update user profile
     */
    public void updateUserProfile(UserId userId, String firstName, String lastName, String phoneNumber) {
        UserAggregate user = userRepository.findById(userId)
            .orElseThrow(() -> new IllegalArgumentException("User not found: " + userId));
        
        UserProfile newProfile = new UserProfile(firstName, lastName, phoneNumber);
        user.updateProfile(newProfile);
        userRepository.save(user);
    }
    
    /**
     * Deactivate user account
     */
    public void deactivateUser(UserId userId) {
        UserAggregate user = userRepository.findById(userId)
            .orElseThrow(() -> new IllegalArgumentException("User not found: " + userId));
        
        user.deactivate();
        userRepository.save(user);
    }
    
    /**
     * Activate user account
     */
    public void activateUser(UserId userId) {
        UserAggregate user = userRepository.findById(userId)
            .orElseThrow(() -> new IllegalArgumentException("User not found: " + userId));
        
        user.activate();
        userRepository.save(user);
    }
    
    /**
     * Update user total money for analytics
     */
    public void updateUserTotalMoney(UserId userId, Money totalMoney) {
        UserAggregate user = userRepository.findById(userId)
            .orElseThrow(() -> new IllegalArgumentException("User not found: " + userId));
        
        user.updateTotalMoney(totalMoney);
        userRepository.save(user);
    }
    
    /**
     * Get all users with pagination
     */
    @Transactional(readOnly = true)
    public List<UserAggregate> getAllUsers(int page, int size) {
        return userRepository.findAll(page, size);
    }
    
    /**
     * Check if user can perform financial operations
     */
    @Transactional(readOnly = true)
    public boolean canUserPerformFinancialOperations(UserId userId) {
        return userRepository.findById(userId)
            .map(UserAggregate::canPerformFinancialOperations)
            .orElse(false);
    }
}