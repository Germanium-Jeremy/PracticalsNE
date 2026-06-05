package com.utility.billing.billing;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.utility.billing.customer.Customer;
import com.utility.billing.meter.Meter;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "bills")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Bill {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String billNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "meter_id", nullable = false)
    private Meter meter;

    private Integer billingMonth;
    private Integer billingYear;

    private Double consumption;
    private Double tariffAmount;
    private Double taxAmount;
    private Double penaltyAmount;
    private Double totalAmount;
    private Double paidAmount;
    private Double balance;

    @Enumerated(EnumType.STRING)
    private BillStatus status;

    private LocalDateTime generatedDate;
    private LocalDateTime approvedDate;

    public Bill() {}

    public Bill(Long id, String billNumber, Customer customer, Meter meter, Integer billingMonth, Integer billingYear, Double consumption, Double tariffAmount, Double taxAmount, Double penaltyAmount, Double totalAmount, Double paidAmount, Double balance, BillStatus status, LocalDateTime generatedDate, LocalDateTime approvedDate) {
        this.id = id;
        this.billNumber = billNumber;
        this.customer = customer;
        this.meter = meter;
        this.billingMonth = billingMonth;
        this.billingYear = billingYear;
        this.consumption = consumption;
        this.tariffAmount = tariffAmount;
        this.taxAmount = taxAmount;
        this.penaltyAmount = penaltyAmount;
        this.totalAmount = totalAmount;
        this.paidAmount = paidAmount;
        this.balance = balance;
        this.status = status;
        this.generatedDate = generatedDate;
        this.approvedDate = approvedDate;
    }

    public static BillBuilder builder() {
        return new BillBuilder();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getBillNumber() { return billNumber; }
    public void setBillNumber(String billNumber) { this.billNumber = billNumber; }
    public Customer getCustomer() { return customer; }
    public void setCustomer(Customer customer) { this.customer = customer; }
    public Meter getMeter() { return meter; }
    public void setMeter(Meter meter) { this.meter = meter; }
    public Integer getBillingMonth() { return billingMonth; }
    public void setBillingMonth(Integer billingMonth) { this.billingMonth = billingMonth; }
    public Integer getBillingYear() { return billingYear; }
    public void setBillingYear(Integer billingYear) { this.billingYear = billingYear; }
    public Double getConsumption() { return consumption; }
    public void setConsumption(Double consumption) { this.consumption = consumption; }
    public Double getTariffAmount() { return tariffAmount; }
    public void setTariffAmount(Double tariffAmount) { this.tariffAmount = tariffAmount; }
    public Double getTaxAmount() { return taxAmount; }
    public void setTaxAmount(Double taxAmount) { this.taxAmount = taxAmount; }
    public Double getPenaltyAmount() { return penaltyAmount; }
    public void setPenaltyAmount(Double penaltyAmount) { this.penaltyAmount = penaltyAmount; }
    public Double getTotalAmount() { return totalAmount; }
    public void setTotalAmount(Double totalAmount) { this.totalAmount = totalAmount; }
    public Double getPaidAmount() { return paidAmount; }
    public void setPaidAmount(Double paidAmount) { this.paidAmount = paidAmount; }
    public Double getBalance() { return balance; }
    public void setBalance(Double balance) { this.balance = balance; }
    public BillStatus getStatus() { return status; }
    public void setStatus(BillStatus status) { this.status = status; }
    public LocalDateTime getGeneratedDate() { return generatedDate; }
    public void setGeneratedDate(LocalDateTime generatedDate) { this.generatedDate = generatedDate; }
    public LocalDateTime getApprovedDate() { return approvedDate; }
    public void setApprovedDate(LocalDateTime approvedDate) { this.approvedDate = approvedDate; }

    public static class BillBuilder {
        private Long id;
        private String billNumber;
        private Customer customer;
        private Meter meter;
        private Integer billingMonth;
        private Integer billingYear;
        private Double consumption;
        private Double tariffAmount;
        private Double taxAmount;
        private Double penaltyAmount;
        private Double totalAmount;
        private Double paidAmount;
        private Double balance;
        private BillStatus status;
        private LocalDateTime generatedDate;
        private LocalDateTime approvedDate;

        public BillBuilder id(Long id) { this.id = id; return this; }
        public BillBuilder billNumber(String billNumber) { this.billNumber = billNumber; return this; }
        public BillBuilder customer(Customer customer) { this.customer = customer; return this; }
        public BillBuilder meter(Meter meter) { this.meter = meter; return this; }
        public BillBuilder billingMonth(Integer billingMonth) { this.billingMonth = billingMonth; return this; }
        public BillBuilder billingYear(Integer billingYear) { this.billingYear = billingYear; return this; }
        public BillBuilder consumption(Double consumption) { this.consumption = consumption; return this; }
        public BillBuilder tariffAmount(Double tariffAmount) { this.tariffAmount = tariffAmount; return this; }
        public BillBuilder taxAmount(Double taxAmount) { this.taxAmount = taxAmount; return this; }
        public BillBuilder penaltyAmount(Double penaltyAmount) { this.penaltyAmount = penaltyAmount; return this; }
        public BillBuilder totalAmount(Double totalAmount) { this.totalAmount = totalAmount; return this; }
        public BillBuilder paidAmount(Double paidAmount) { this.paidAmount = paidAmount; return this; }
        public BillBuilder balance(Double balance) { this.balance = balance; return this; }
        public BillBuilder status(BillStatus status) { this.status = status; return this; }
        public BillBuilder generatedDate(LocalDateTime generatedDate) { this.generatedDate = generatedDate; return this; }
        public BillBuilder approvedDate(LocalDateTime approvedDate) { this.approvedDate = approvedDate; return this; }

        public Bill build() {
            return new Bill(id, billNumber, customer, meter, billingMonth, billingYear, consumption, tariffAmount, taxAmount, penaltyAmount, totalAmount, paidAmount, balance, status, generatedDate, approvedDate);
        }
    }
}
