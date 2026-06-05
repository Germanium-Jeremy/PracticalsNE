package com.utility.billing.customer;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "customers")
@EntityListeners(AuditingEntityListener.class)
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String fullNames;

    @Column(unique = true, nullable = false)
    private String nationalId;

    private String email;
    private String phone;
    private String address;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CustomerStatus status;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @OneToOne
    @JoinColumn(name = "user_id")
    private com.utility.billing.user.User user;

    public Customer() {}

    public Customer(Long id, String fullNames, String nationalId, String email, String phone, String address, CustomerStatus status, LocalDateTime createdAt, com.utility.billing.user.User user) {
        this.id = id;
        this.fullNames = fullNames;
        this.nationalId = nationalId;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.status = status;
        this.createdAt = createdAt;
        this.user = user;
    }

    public static CustomerBuilder builder() {
        return new CustomerBuilder();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getFullNames() { return fullNames; }
    public void setFullNames(String fullNames) { this.fullNames = fullNames; }
    public String getNationalId() { return nationalId; }
    public void setNationalId(String nationalId) { this.nationalId = nationalId; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    public CustomerStatus getStatus() { return status; }
    public void setStatus(CustomerStatus status) { this.status = status; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public com.utility.billing.user.User getUser() { return user; }
    public void setUser(com.utility.billing.user.User user) { this.user = user; }

    public static class CustomerBuilder {
        private Long id;
        private String fullNames;
        private String nationalId;
        private String email;
        private String phone;
        private String address;
        private CustomerStatus status;
        private LocalDateTime createdAt;
        private com.utility.billing.user.User user;

        public CustomerBuilder id(Long id) { this.id = id; return this; }
        public CustomerBuilder fullNames(String fullNames) { this.fullNames = fullNames; return this; }
        public CustomerBuilder nationalId(String nationalId) { this.nationalId = nationalId; return this; }
        public CustomerBuilder email(String email) { this.email = email; return this; }
        public CustomerBuilder phone(String phone) { this.phone = phone; return this; }
        public CustomerBuilder address(String address) { this.address = address; return this; }
        public CustomerBuilder status(CustomerStatus status) { this.status = status; return this; }
        public CustomerBuilder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }
        public CustomerBuilder user(com.utility.billing.user.User user) { this.user = user; return this; }

        public Customer build() {
            return new Customer(id, fullNames, nationalId, email, phone, address, status, createdAt, user);
        }
    }
}
