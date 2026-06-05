package com.utility.billing.meter;

import com.utility.billing.customer.Customer;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "meters")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Meter {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String meterNumber;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MeterType meterType;

    private LocalDateTime installationDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MeterStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;
}
