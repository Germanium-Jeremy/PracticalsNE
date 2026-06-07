package com.utility.billing.user;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@EntityListeners(AuditingEntityListener.class)
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "password"})
public class User {
    // Unique identifier for the user
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Full name of the user for identification and profile display
    @Column(nullable = false)
    private String fullName;

    // Email address used for authentication and notifications
    @Column(unique = true, nullable = false)
    private String email;

    // Contact phone number for the user
    @Column(nullable = false)
    private String phoneNumber;

    // Encrypted password for secure authentication
    @Column(nullable = false)
    private String password;

    // Current status of the user account (e.g., ACTIVE, INACTIVE, PENDING_ACTIVATION)
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserStatus status;

    // Activation token for account verification (OTP)
    @Column
    private String activationToken;

    // Role assigned to the user for access control (e.g., ROLE_ADMIN, ROLE_CUSTOMER)
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    // Timestamp when the user record was created
    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    // Timestamp when the user record was last updated
    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    public User() {}

    public User(Long id, String fullName, String email, String phoneNumber, String password, UserStatus status, Role role, String activationToken, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.fullName = fullName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.password = password;
        this.status = status;
        this.role = role;
        this.activationToken = activationToken;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // Builder pattern for convenient object instantiation
    public static UserBuilder builder() {
        return new UserBuilder();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
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
    public String getActivationToken() { return activationToken; }
    public void setActivationToken(String activationToken) { this.activationToken = activationToken; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    public static class UserBuilder {
        private Long id;
        private String fullName;
        private String email;
        private String phoneNumber;
        private String password;
        private UserStatus status;
        private Role role;
        private String activationToken;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        public UserBuilder id(Long id) { this.id = id; return this; }
        public UserBuilder fullName(String fullName) { this.fullName = fullName; return this; }
        public UserBuilder email(String email) { this.email = email; return this; }
        public UserBuilder phoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; return this; }
        public UserBuilder password(String password) { this.password = password; return this; }
        public UserBuilder status(UserStatus status) { this.status = status; return this; }
        public UserBuilder role(Role role) { this.role = role; return this; }
        public UserBuilder activationToken(String activationToken) { this.activationToken = activationToken; return this; }
        public UserBuilder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }
        public UserBuilder updatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; return this; }

        public User build() {
            return new User(id, fullName, email, phoneNumber, password, status, role, activationToken, createdAt, updatedAt);
        }
    }
}
