package com.utility.billing.billing;

import com.utility.billing.customer.Customer;
import com.utility.billing.meter.Meter;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "bills")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
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
}
