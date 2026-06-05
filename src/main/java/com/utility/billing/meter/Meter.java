package com.utility.billing.meter;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.utility.billing.customer.Customer;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "meters")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Meter {
    // Unique identifier for the utility meter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Unique serial number of the meter (e.g., WTR-12345)
    @Column(unique = true, nullable = false)
    private String meterNumber;

    // Type of utility the meter measures (e.g., WATER, ELECTRICITY)
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MeterType meterType;

    // Date when the meter was installed at the customer's location
    private LocalDateTime installationDate;

    // Current operational status of the meter (e.g., ACTIVE, INACTIVE)
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MeterStatus status;

    // Customer who owns and is responsible for this meter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    public Meter() {}

    public Meter(Long id, String meterNumber, MeterType meterType, LocalDateTime installationDate, MeterStatus status, Customer customer) {
        this.id = id;
        this.meterNumber = meterNumber;
        this.meterType = meterType;
        this.installationDate = installationDate;
        this.status = status;
        this.customer = customer;
    }

    public static MeterBuilder builder() {
        return new MeterBuilder();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getMeterNumber() { return meterNumber; }
    public void setMeterNumber(String meterNumber) { this.meterNumber = meterNumber; }
    public MeterType getMeterType() { return meterType; }
    public void setMeterType(MeterType meterType) { this.meterType = meterType; }
    public LocalDateTime getInstallationDate() { return installationDate; }
    public void setInstallationDate(LocalDateTime installationDate) { this.installationDate = installationDate; }
    public MeterStatus getStatus() { return status; }
    public void setStatus(MeterStatus status) { this.status = status; }
    public Customer getCustomer() { return customer; }
    public void setCustomer(Customer customer) { this.customer = customer; }

    public static class MeterBuilder {
        private Long id;
        private String meterNumber;
        private MeterType meterType;
        private LocalDateTime installationDate;
        private MeterStatus status;
        private Customer customer;

        public MeterBuilder id(Long id) { this.id = id; return this; }
        public MeterBuilder meterNumber(String meterNumber) { this.meterNumber = meterNumber; return this; }
        public MeterBuilder meterType(MeterType meterType) { this.meterType = meterType; return this; }
        public MeterBuilder installationDate(LocalDateTime installationDate) { this.installationDate = installationDate; return this; }
        public MeterBuilder status(MeterStatus status) { this.status = status; return this; }
        public MeterBuilder customer(Customer customer) { this.customer = customer; return this; }

        public Meter build() {
            return new Meter(id, meterNumber, meterType, installationDate, status, customer);
        }
    }
}
