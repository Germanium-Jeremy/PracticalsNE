package com.utility.billing.reading;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/readings")
public class MeterReadingController {

    private final MeterReadingService readingService;

    public MeterReadingController(MeterReadingService readingService) {
        this.readingService = readingService;
    }

    @PostMapping
    @PreAuthorize("hasRole('OPERATOR') or hasRole('ADMIN')")
    public ResponseEntity<MeterReadingResponse> captureReading(@Valid @RequestBody MeterReadingRequest request) {
        return ResponseEntity.ok(readingService.captureReading(request));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'OPERATOR')")
    public ResponseEntity<List<MeterReadingResponse>> getAllReadings() {
        return ResponseEntity.ok(readingService.getAllReadings());
    }
}
