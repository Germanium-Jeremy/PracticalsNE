package com.utility.billing.meter;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class MeterRequest {
    @NotBlank(message = "Meter number is required")
    private String meterNumber;
    @NotNull(message = "Meter type is required")
    private MeterType meterType;
    @NotNull(message = "Customer ID is required")
    private Long customerId;
    private MeterStatus status;
}
