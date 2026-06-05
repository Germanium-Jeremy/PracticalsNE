package com.utility.billing.tariff;

import jakarta.persistence.*;

@Entity
@Table(name = "tax_configurations")
public class TaxConfiguration {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Double percentage;

    private boolean active;

    public TaxConfiguration() {}

    public TaxConfiguration(Long id, String name, Double percentage, boolean active) {
        this.id = id;
        this.name = name;
        this.percentage = percentage;
        this.active = active;
    }

    public static TaxConfigurationBuilder builder() {
        return new TaxConfigurationBuilder();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public Double getPercentage() { return percentage; }
    public void setPercentage(Double percentage) { this.percentage = percentage; }
    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }

    public static class TaxConfigurationBuilder {
        private Long id;
        private String name;
        private Double percentage;
        private boolean active;

        public TaxConfigurationBuilder id(Long id) { this.id = id; return this; }
        public TaxConfigurationBuilder name(String name) { this.name = name; return this; }
        public TaxConfigurationBuilder percentage(Double percentage) { this.percentage = percentage; return this; }
        public TaxConfigurationBuilder active(boolean active) { this.active = active; return this; }

        public TaxConfiguration build() {
            return new TaxConfiguration(id, name, percentage, active);
        }
    }
}
