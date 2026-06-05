package com.utility.billing.tariff;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "penalty_configurations")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PenaltyConfiguration {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Double fixedAmount;

    @Column(nullable = false)
    private Double percentagePerMonth;

    private boolean active;
}
