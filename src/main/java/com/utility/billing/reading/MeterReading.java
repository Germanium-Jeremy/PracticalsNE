package com.utility.billing.reading;

import com.utility.billing.meter.Meter;
import com.utility.billing.user.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "meter_readings", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"meter_id", "billing_month", "billing_year"})
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MeterReading {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "meter_id", nullable = false)
    private Meter meter;

    @Column(nullable = false)
    private Double previousReading;

    @Column(nullable = false)
    private Double currentReading;

    @Column(nullable = false)
    private LocalDateTime readingDate;

    @Column(name = "billing_month", nullable = false)
    private Integer month;

    @Column(name = "billing_year", nullable = false)
    private Integer year;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "captured_by", nullable = false)
    private User capturedBy;
}
