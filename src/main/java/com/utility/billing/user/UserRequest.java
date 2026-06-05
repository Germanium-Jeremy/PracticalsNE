package com.utility.billing.user;

import jakarta.validation.constraints.*;

public class UserRequest {
    @NotBlank(message = "Full name is required")
    @Pattern(regexp = "^\\w+\\s+\\w+.*$", message = "Full name must contain at least two names (at least one space)")
    private String fullName;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "^(07[2389]\\d{7}|\\+2507[2389]\\d{7})$", 
            message = "Invalid phone number. Use 07... (10 digits) or +2507... (13 characters)")
    private String phoneNumber;

    private String password; // Optional during update

    private UserStatus status;

    private Role role;

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public UserStatus getStatus() { return status; }
    public void setStatus(UserStatus status) { this.status = status; }
    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }
}
