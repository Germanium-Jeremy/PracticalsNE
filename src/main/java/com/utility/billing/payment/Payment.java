package com.utility.billing.payment;

import com.utility.billing.billing.Bill;
import com.utility.billing.user.User;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "payments")
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bill_id", nullable = false)
    private Bill bill;

    @Column(nullable = false)
    private Double amountPaid;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentMethod paymentMethod;

    @Column(nullable = false)
    private LocalDateTime paymentDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recorded_by", nullable = false)
    private User recordedBy;

    public Payment() {}

    public Payment(Long id, Bill bill, Double amountPaid, PaymentMethod paymentMethod, LocalDateTime paymentDate, User recordedBy) {
        this.id = id;
        this.bill = bill;
        this.amountPaid = amountPaid;
        this.paymentMethod = paymentMethod;
        this.paymentDate = paymentDate;
        this.recordedBy = recordedBy;
    }

    public static PaymentBuilder builder() {
        return new PaymentBuilder();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Bill getBill() { return bill; }
    public void setBill(Bill bill) { this.bill = bill; }
    public Double getAmountPaid() { return amountPaid; }
    public void setAmountPaid(Double amountPaid) { this.amountPaid = amountPaid; }
    public PaymentMethod getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(PaymentMethod paymentMethod) { this.paymentMethod = paymentMethod; }
    public LocalDateTime getPaymentDate() { return paymentDate; }
    public void setPaymentDate(LocalDateTime paymentDate) { this.paymentDate = paymentDate; }
    public User getRecordedBy() { return recordedBy; }
    public void setRecordedBy(User recordedBy) { this.recordedBy = recordedBy; }

    public static class PaymentBuilder {
        private Long id;
        private Bill bill;
        private Double amountPaid;
        private PaymentMethod paymentMethod;
        private LocalDateTime paymentDate;
        private User recordedBy;

        public PaymentBuilder id(Long id) { this.id = id; return this; }
        public PaymentBuilder bill(Bill bill) { this.bill = bill; return this; }
        public PaymentBuilder amountPaid(Double amountPaid) { this.amountPaid = amountPaid; return this; }
        public PaymentBuilder paymentMethod(PaymentMethod paymentMethod) { this.paymentMethod = paymentMethod; return this; }
        public PaymentBuilder paymentDate(LocalDateTime paymentDate) { this.paymentDate = paymentDate; return this; }
        public PaymentBuilder recordedBy(User recordedBy) { this.recordedBy = recordedBy; return this; }

        public Payment build() {
            return new Payment(id, bill, amountPaid, paymentMethod, paymentDate, recordedBy);
        }
    }
}
