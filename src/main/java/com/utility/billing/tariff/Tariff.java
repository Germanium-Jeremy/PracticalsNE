package com.utility.billing.tariff;

import com.utility.billing.meter.MeterType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "tariffs")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Tariff {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MeterType meterType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TariffType tariffType;

    @Column(nullable = false)
    private Double rate;

    @Column(nullable = false)
    private LocalDateTime effectiveFrom;

    @Column(nullable = false)
    private Integer version;

    @Column(nullable = false)
    private boolean active;
}
