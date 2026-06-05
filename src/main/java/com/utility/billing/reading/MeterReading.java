package com.utility.billing.reading;

import com.utility.billing.meter.Meter;
import com.utility.billing.user.User;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "meter_readings", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"meter_id", "billing_month", "billing_year"})
})
public class MeterReading {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "meter_id", nullable = false)
    private Meter meter;

    @Column(nullable = false)
    private Double previousReading;

    @Column(nullable = false)
    private Double currentReading;

    @Column(nullable = false)
    private LocalDateTime readingDate;

    @Column(name = "billing_month", nullable = false)
    private Integer month;

    @Column(name = "billing_year", nullable = false)
    private Integer year;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "captured_by", nullable = false)
    private User capturedBy;

    public MeterReading() {}

    public MeterReading(Long id, Meter meter, Double previousReading, Double currentReading, LocalDateTime readingDate, Integer month, Integer year, User capturedBy) {
        this.id = id;
        this.meter = meter;
        this.previousReading = previousReading;
        this.currentReading = currentReading;
        this.readingDate = readingDate;
        this.month = month;
        this.year = year;
        this.capturedBy = capturedBy;
    }

    public static MeterReadingBuilder builder() {
        return new MeterReadingBuilder();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Meter getMeter() { return meter; }
    public void setMeter(Meter meter) { this.meter = meter; }
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
    public User getCapturedBy() { return capturedBy; }
    public void setCapturedBy(User capturedBy) { this.capturedBy = capturedBy; }

    public static class MeterReadingBuilder {
        private Long id;
        private Meter meter;
        private Double previousReading;
        private Double currentReading;
        private LocalDateTime readingDate;
        private Integer month;
        private Integer year;
        private User capturedBy;

        public MeterReadingBuilder id(Long id) { this.id = id; return this; }
        public MeterReadingBuilder meter(Meter meter) { this.meter = meter; return this; }
        public MeterReadingBuilder previousReading(Double previousReading) { this.previousReading = previousReading; return this; }
        public MeterReadingBuilder currentReading(Double currentReading) { this.currentReading = currentReading; return this; }
        public MeterReadingBuilder readingDate(LocalDateTime readingDate) { this.readingDate = readingDate; return this; }
        public MeterReadingBuilder month(Integer month) { this.month = month; return this; }
        public MeterReadingBuilder year(Integer year) { this.year = year; return this; }
        public MeterReadingBuilder capturedBy(User capturedBy) { this.capturedBy = capturedBy; return this; }

        public MeterReading build() {
            return new MeterReading(id, meter, previousReading, currentReading, readingDate, month, year, capturedBy);
        }
    }
}
