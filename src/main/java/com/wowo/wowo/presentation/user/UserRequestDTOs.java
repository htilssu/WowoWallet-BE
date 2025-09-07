package com.wowo.wowo.presentation.user;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Email;

/**
 * Request DTOs for User operations
 */
public class UserRequestDTOs {
    
    @Data
    public static class CreateUserRequest {
        @NotBlank(message = "Email is required")
        @Email(message = "Email format is invalid")
        private String email;
        
        @NotBlank(message = "First name is required")
        private String firstName;
        
        @NotBlank(message = "Last name is required")
        private String lastName;
        
        private String phoneNumber;
    }
    
    @Data
    public static class UpdateUserProfileRequest {
        @NotBlank(message = "First name is required")
        private String firstName;
        
        @NotBlank(message = "Last name is required")
        private String lastName;
        
        private String phoneNumber;
    }
}