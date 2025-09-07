package com.wowo.wowo.domain.user.valueobjects;

/**
 * User profile value object containing personal information
 */
public record UserProfile(String firstName, String lastName, String phoneNumber) {
    
    public UserProfile {
        if (firstName == null || firstName.trim().isEmpty()) {
            throw new IllegalArgumentException("First name cannot be null or empty");
        }
        if (lastName == null || lastName.trim().isEmpty()) {
            throw new IllegalArgumentException("Last name cannot be null or empty");
        }
        firstName = firstName != null ? firstName.trim() : "";
        lastName = lastName != null ? lastName.trim() : "";
        phoneNumber = phoneNumber != null ? phoneNumber.trim() : "";
    }
    
    public String getFullName() {
        return firstName + " " + lastName;
    }
}