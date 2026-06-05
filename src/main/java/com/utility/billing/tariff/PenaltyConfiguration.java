package com.utility.billing.tariff;

import jakarta.persistence.*;

@Entity
@Table(name = "penalty_configurations")
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

    public PenaltyConfiguration() {}

    public PenaltyConfiguration(Long id, String name, Double fixedAmount, Double percentagePerMonth, boolean active) {
        this.id = id;
        this.name = name;
        this.fixedAmount = fixedAmount;
        this.percentagePerMonth = percentagePerMonth;
        this.active = active;
    }

    public static PenaltyConfigurationBuilder builder() {
        return new PenaltyConfigurationBuilder();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public Double getFixedAmount() { return fixedAmount; }
    public void setFixedAmount(Double fixedAmount) { this.fixedAmount = fixedAmount; }
    public Double getPercentagePerMonth() { return percentagePerMonth; }
    public void setPercentagePerMonth(Double percentagePerMonth) { this.percentagePerMonth = percentagePerMonth; }
    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }

    public static class PenaltyConfigurationBuilder {
        private Long id;
        private String name;
        private Double fixedAmount;
        private Double percentagePerMonth;
        private boolean active;

        public PenaltyConfigurationBuilder id(Long id) { this.id = id; return this; }
        public PenaltyConfigurationBuilder name(String name) { this.name = name; return this; }
        public PenaltyConfigurationBuilder fixedAmount(Double fixedAmount) { this.fixedAmount = fixedAmount; return this; }
        public PenaltyConfigurationBuilder percentagePerMonth(Double percentagePerMonth) { this.percentagePerMonth = percentagePerMonth; return this; }
        public PenaltyConfigurationBuilder active(boolean active) { this.active = active; return this; }

        public PenaltyConfiguration build() {
            return new PenaltyConfiguration(id, name, fixedAmount, percentagePerMonth, active);
        }
    }
}
