package com.utility.billing.reading;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class MeterReadingRequest {
    @NotNull(message = "Meter ID is required")
    private Long meterId;
    
    @NotNull(message = "Current reading is required")
    @Min(0)
    private Double currentReading;
    
    @NotNull(message = "Month is required")
    @Min(1) @Max(12)
    private Integer month;
    
    @NotNull(message = "Year is required")
    private Integer year;

    public Long getMeterId() { return meterId; }
    public void setMeterId(Long meterId) { this.meterId = meterId; }
    public Double getCurrentReading() { return currentReading; }
    public void setCurrentReading(Double currentReading) { this.currentReading = currentReading; }
    public Integer getMonth() { return month; }
    public void setMonth(Integer month) { this.month = month; }
    public Integer getYear() { return year; }
    public void setYear(Integer year) { this.year = year; }
}
