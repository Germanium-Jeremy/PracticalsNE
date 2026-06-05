package com.utility.billing.reading;

import java.time.LocalDateTime;

public class MeterReadingResponse {
    private Long id;
    private Long meterId;
    private String meterNumber;
    private Double previousReading;
    private Double currentReading;
    private LocalDateTime readingDate;
    private Integer month;
    private Integer year;
    private String capturedBy;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getMeterId() { return meterId; }
    public void setMeterId(Long meterId) { this.meterId = meterId; }
    public String getMeterNumber() { return meterNumber; }
    public void setMeterNumber(String meterNumber) { this.meterNumber = meterNumber; }
    public Double getPreviousReading() { return previousReading; }
    public void setPreviousReading(Double previousReading) { this.previousReading = previousReading; }
    public Double getCurrentReading() { return currentReading; }
    public void setCurrentReading(Double currentReading) { this.currentReading = currentReading; }
    public LocalDateTime getReadingDate() { return readingDate; }
    public void setReadingDate(LocalDateTime readingDate) { this.readingDate = readingDate; }
    public Integer getMonth() { return month; }
    public void setMonth(Integer month) { this.month = month; }
    public Integer getYear() { return year; }
    public void setYear(Integer year) { this.year = year; }
    public String getCapturedBy() { return capturedBy; }
    public void setCapturedBy(String capturedBy) { this.capturedBy = capturedBy; }
}
