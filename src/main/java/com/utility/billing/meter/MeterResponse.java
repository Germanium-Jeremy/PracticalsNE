package com.utility.billing.meter;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class MeterResponse {
    private Long id;
    private String meterNumber;
    private MeterType meterType;
    private MeterStatus status;
    private LocalDateTime installationDate;
    private Long customerId;
}
