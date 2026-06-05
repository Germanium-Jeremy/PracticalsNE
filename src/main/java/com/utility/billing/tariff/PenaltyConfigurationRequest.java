package com.utility.billing.tariff;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

/**
 * Data Transfer Object for creating or updating penalty configurations.
 * Used by administrators to define penalty rules.
 */
public class PenaltyConfigurationRequest {
    @NotBlank(message = "Penalty name is required")
    private String name;

    @NotNull(message = "Fixed amount is required")
    @PositiveOrZero(message = "Fixed amount must be positive or zero")
    private Double fixedAmount;

    @NotNull(message = "Percentage per month is required")
    @PositiveOrZero(message = "Percentage must be positive or zero")
    private Double percentagePerMonth;

    private boolean active;

    public PenaltyConfigurationRequest() {}

    public PenaltyConfigurationRequest(String name, Double fixedAmount, Double percentagePerMonth, boolean active) {
        this.name = name;
        this.fixedAmount = fixedAmount;
        this.percentagePerMonth = percentagePerMonth;
        this.active = active;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public Double getFixedAmount() { return fixedAmount; }
    public void setFixedAmount(Double fixedAmount) { this.fixedAmount = fixedAmount; }
    public Double getPercentagePerMonth() { return percentagePerMonth; }
    public void setPercentagePerMonth(Double percentagePerMonth) { this.percentagePerMonth = percentagePerMonth; }
    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
}
