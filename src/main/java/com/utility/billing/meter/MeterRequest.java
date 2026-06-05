package com.utility.billing.meter;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class MeterRequest {
    @NotBlank(message = "Meter number is required")
    private String meterNumber;
    @NotNull(message = "Meter type is required")
    private MeterType meterType;
    @NotNull(message = "Customer ID is required")
    private Long customerId;
    private MeterStatus status;

    public String getMeterNumber() { return meterNumber; }
    public void setMeterNumber(String meterNumber) { this.meterNumber = meterNumber; }
    public MeterType getMeterType() { return meterType; }
    public void setMeterType(MeterType meterType) { this.meterType = meterType; }
    public Long getCustomerId() { return customerId; }
    public void setCustomerId(Long customerId) { this.customerId = customerId; }
    public MeterStatus getStatus() { return status; }
    public void setStatus(MeterStatus status) { this.status = status; }
}
