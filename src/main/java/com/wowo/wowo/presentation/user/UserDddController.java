package com.wowo.wowo.presentation.user;

import com.wowo.wowo.application.user.UserApplicationService;
import com.wowo.wowo.domain.user.entity.UserAggregate;
import com.wowo.wowo.domain.user.valueobjects.UserId;
import com.wowo.wowo.presentation.user.UserRequestDTOs.CreateUserRequest;
import com.wowo.wowo.presentation.user.UserRequestDTOs.UpdateUserProfileRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

/**
 * DDD User Controller - Clean API endpoints for user management
 * Follows DDD principles with thin controller delegating to application services
 */
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Tag(name = "Users (DDD)", description = "User management with Domain-Driven Design")
public class UserDddController {
    
    private final UserApplicationService userApplicationService;
    
    @PostMapping
    @Operation(summary = "Create new user")
    @ApiResponse(responseCode = "201", description = "User created successfully")
    @ApiResponse(responseCode = "400", description = "Invalid request data")
    @ApiResponse(responseCode = "409", description = "Email already exists")
    public ResponseEntity<UserResponse> createUser(@Valid @RequestBody CreateUserRequest request) {
        try {
            UserId userId = userApplicationService.createUser(
                request.getEmail(),
                request.getFirstName(),
                request.getLastName(),
                request.getPhoneNumber()
            );
            
            UserAggregate user = userApplicationService.getUserById(userId)
                .orElseThrow(() -> new RuntimeException("Failed to retrieve created user"));
            
            return ResponseEntity.status(HttpStatus.CREATED)
                .body(toResponse(user));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Get user by ID")
    @ApiResponse(responseCode = "200", description = "User found")
    @ApiResponse(responseCode = "404", description = "User not found")
    public ResponseEntity<UserResponse> getUserById(@PathVariable String id) {
        return userApplicationService.getUserById(new UserId(id))
            .map(user -> ResponseEntity.ok(toResponse(user)))
            .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/me")
    @Operation(summary = "Get current user profile")
    @ApiResponse(responseCode = "200", description = "Current user profile")
    @ApiResponse(responseCode = "404", description = "User not found")
    public ResponseEntity<UserResponse> getCurrentUser(Authentication authentication) {
        String userId = authentication.getPrincipal().toString();
        
        return userApplicationService.getUserById(new UserId(userId))
            .map(user -> ResponseEntity.ok(toResponse(user)))
            .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/email/{email}")
    @Operation(summary = "Get user by email")
    @ApiResponse(responseCode = "200", description = "User found")
    @ApiResponse(responseCode = "404", description = "User not found")
    public ResponseEntity<UserResponse> getUserByEmail(@PathVariable String email) {
        return userApplicationService.getUserByEmail(email)
            .map(user -> ResponseEntity.ok(toResponse(user)))
            .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping
    @Operation(summary = "Get all users with pagination")
    @ApiResponse(responseCode = "200", description = "Users retrieved successfully")
    public ResponseEntity<List<UserResponse>> getAllUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        List<UserAggregate> users = userApplicationService.getAllUsers(page, size);
        List<UserResponse> responses = users.stream()
            .map(this::toResponse)
            .collect(Collectors.toList());
        
        return ResponseEntity.ok(responses);
    }
    
    @PostMapping("/{id}/verify")
    @Operation(summary = "Verify user account")
    @ApiResponse(responseCode = "200", description = "User verified successfully")
    @ApiResponse(responseCode = "404", description = "User not found")
    @ApiResponse(responseCode = "400", description = "User already verified")
    public ResponseEntity<UserResponse> verifyUser(@PathVariable String id) {
        try {
            userApplicationService.verifyUser(new UserId(id));
            
            UserAggregate user = userApplicationService.getUserById(new UserId(id))
                .orElseThrow(() -> new RuntimeException("User not found after verification"));
            
            return ResponseEntity.ok(toResponse(user));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @PutMapping("/{id}")
    @Operation(summary = "Update user profile")
    @ApiResponse(responseCode = "200", description = "Profile updated successfully")
    @ApiResponse(responseCode = "404", description = "User not found")
    public ResponseEntity<UserResponse> updateUserProfile(
            @PathVariable String id,
            @Valid @RequestBody UpdateUserProfileRequest request) {
        
        try {
            userApplicationService.updateUserProfile(
                new UserId(id),
                request.getFirstName(),
                request.getLastName(),
                request.getPhoneNumber()
            );
            
            UserAggregate user = userApplicationService.getUserById(new UserId(id))
                .orElseThrow(() -> new RuntimeException("User not found after update"));
            
            return ResponseEntity.ok(toResponse(user));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @PostMapping("/{id}/deactivate")
    @Operation(summary = "Deactivate user account")
    @ApiResponse(responseCode = "200", description = "User deactivated successfully")
    @ApiResponse(responseCode = "404", description = "User not found")
    @ApiResponse(responseCode = "400", description = "User already deactivated")
    public ResponseEntity<UserResponse> deactivateUser(@PathVariable String id) {
        try {
            userApplicationService.deactivateUser(new UserId(id));
            
            UserAggregate user = userApplicationService.getUserById(new UserId(id))
                .orElseThrow(() -> new RuntimeException("User not found after deactivation"));
            
            return ResponseEntity.ok(toResponse(user));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @PostMapping("/{id}/activate")
    @Operation(summary = "Activate user account")
    @ApiResponse(responseCode = "200", description = "User activated successfully")
    @ApiResponse(responseCode = "404", description = "User not found")
    @ApiResponse(responseCode = "400", description = "User already activated")
    public ResponseEntity<UserResponse> activateUser(@PathVariable String id) {
        try {
            userApplicationService.activateUser(new UserId(id));
            
            UserAggregate user = userApplicationService.getUserById(new UserId(id))
                .orElseThrow(() -> new RuntimeException("User not found after activation"));
            
            return ResponseEntity.ok(toResponse(user));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @GetMapping("/{id}/financial-capabilities")
    @Operation(summary = "Check if user can perform financial operations")
    @ApiResponse(responseCode = "200", description = "Financial capabilities checked")
    public ResponseEntity<Boolean> canPerformFinancialOperations(@PathVariable String id) {
        boolean canPerform = userApplicationService.canUserPerformFinancialOperations(new UserId(id));
        return ResponseEntity.ok(canPerform);
    }
    
    /**
     * Convert domain model to response DTO
     */
    private UserResponse toResponse(UserAggregate user) {
        return UserResponse.builder()
            .id(user.getId().value())
            .email(user.getEmail().value())
            .firstName(user.getProfile().firstName())
            .lastName(user.getProfile().lastName())
            .fullName(user.getFullName())
            .phoneNumber(user.getProfile().phoneNumber())
            .isActive(user.isActive())
            .isVerified(user.isVerified())
            .totalMoney(BigDecimal.valueOf(user.getTotalMoney().getAmount()))
            .currency(user.getTotalMoney().getCurrency())
            .createdAt(user.getCreatedAt())
            .updatedAt(user.getUpdatedAt())
            .canPerformFinancialOperations(user.canPerformFinancialOperations())
            .build();
    }
}