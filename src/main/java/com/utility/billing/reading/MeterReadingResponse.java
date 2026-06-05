package com.utility.billing.reading;

import lombok.Data;
import java.time.LocalDateTime;

@Data
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
}
