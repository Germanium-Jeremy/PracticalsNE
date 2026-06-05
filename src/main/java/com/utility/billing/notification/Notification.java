package com.utility.billing.notification;

import com.utility.billing.billing.Bill;
import com.utility.billing.customer.Customer;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "notifications")
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bill_id")
    private Bill bill;

    @Column(nullable = false, length = 500)
    private String message;

    @Enumerated(EnumType.STRING)
    private NotificationStatus status;

    private LocalDateTime createdAt;

    public Notification() {}

    public Notification(Long id, Customer customer, Bill bill, String message, NotificationStatus status, LocalDateTime createdAt) {
        this.id = id;
        this.customer = customer;
        this.bill = bill;
        this.message = message;
        this.status = status;
        this.createdAt = createdAt;
    }

    public static NotificationBuilder builder() {
        return new NotificationBuilder();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Customer getCustomer() { return customer; }
    public void setCustomer(Customer customer) { this.customer = customer; }
    public Bill getBill() { return bill; }
    public void setBill(Bill bill) { this.bill = bill; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    public NotificationStatus getStatus() { return status; }
    public void setStatus(NotificationStatus status) { this.status = status; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public static class NotificationBuilder {
        private Long id;
        private Customer customer;
        private Bill bill;
        private String message;
        private NotificationStatus status;
        private LocalDateTime createdAt;

        public NotificationBuilder id(Long id) { this.id = id; return this; }
        public NotificationBuilder customer(Customer customer) { this.customer = customer; return this; }
        public NotificationBuilder bill(Bill bill) { this.bill = bill; return this; }
        public NotificationBuilder message(String message) { this.message = message; return this; }
        public NotificationBuilder status(NotificationStatus status) { this.status = status; return this; }
        public NotificationBuilder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }

        public Notification build() {
            return new Notification(id, customer, bill, message, status, createdAt);
        }
    }
}
