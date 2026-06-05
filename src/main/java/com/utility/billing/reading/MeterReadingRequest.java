package com.utility.billing.reading;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
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
}
