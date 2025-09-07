package com.wowo.wowo.domain.user.valueobjects;

/**
 * User profile value object containing personal information
 */
public record UserProfile(String firstName, String lastName, String username, String job, String avatar) {
    
    public UserProfile {
        if (firstName == null || firstName.trim().isEmpty()) {
            throw new IllegalArgumentException("First name cannot be null or empty");
        }
        if (lastName == null || lastName.trim().isEmpty()) {
            throw new IllegalArgumentException("Last name cannot be null or empty");
        }
    }
    
    public static UserProfile of(String firstName, String lastName, String username, String job, String avatar) {
        return new UserProfile(
            firstName.trim(), 
            lastName.trim(), 
            username != null ? username.trim() : null,
            job != null ? job.trim() : null,
            avatar != null ? avatar.trim() : null
        );
    }
    
    public String getFullName() {
        return firstName + " " + lastName;
    }
    
    public UserProfile updateAvatar(String newAvatar) {
        return new UserProfile(firstName, lastName, username, job, newAvatar);
    }
    
    public UserProfile updateJob(String newJob) {
        return new UserProfile(firstName, lastName, username, newJob, avatar);
    }
}