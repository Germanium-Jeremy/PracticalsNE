package com.utility.billing.tariff;

import com.utility.billing.meter.MeterType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class TariffRequest {
    @NotNull(message = "Meter type is required")
    private MeterType meterType;

    @NotNull(message = "Tariff type is required")
    private TariffType tariffType;

    @NotNull(message = "Rate is required")
    @Positive(message = "Rate must be positive")
    private Double rate;

    public MeterType getMeterType() { return meterType; }
    public void setMeterType(MeterType meterType) { this.meterType = meterType; }

    public TariffType getTariffType() { return tariffType; }
    public void setTariffType(TariffType tariffType) { this.tariffType = tariffType; }

    public Double getRate() { return rate; }
    public void setRate(Double rate) { this.rate = rate; }
}
