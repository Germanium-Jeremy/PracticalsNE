package com.utility.billing.tariff;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "tax_configurations")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaxConfiguration {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Double percentage;

    private boolean active;
}
