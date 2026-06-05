package com.utility.billing.meter;

import java.time.LocalDateTime;

public class MeterResponse {
    private Long id;
    private String meterNumber;
    private MeterType meterType;
    private MeterStatus status;
    private LocalDateTime installationDate;
    private Long customerId;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getMeterNumber() { return meterNumber; }
    public void setMeterNumber(String meterNumber) { this.meterNumber = meterNumber; }
    public MeterType getMeterType() { return meterType; }
    public void setMeterType(MeterType meterType) { this.meterType = meterType; }
    public MeterStatus getStatus() { return status; }
    public void setStatus(MeterStatus status) { this.status = status; }
    public LocalDateTime getInstallationDate() { return installationDate; }
    public void setInstallationDate(LocalDateTime installationDate) { this.installationDate = installationDate; }
    public Long getCustomerId() { return customerId; }
    public void setCustomerId(Long customerId) { this.customerId = customerId; }
}
