package com.utility.billing.reading;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/readings")
@RequiredArgsConstructor
public class MeterReadingController {

    private final MeterReadingService readingService;

    @PostMapping
    @PreAuthorize("hasRole('OPERATOR') or hasRole('ADMIN')")
    public ResponseEntity<MeterReadingResponse> captureReading(@Valid @RequestBody MeterReadingRequest request) {
        return ResponseEntity.ok(readingService.captureReading(request));
    }

    @GetMapping
    public ResponseEntity<List<MeterReadingResponse>> getAllReadings() {
        return ResponseEntity.ok(readingService.getAllReadings());
    }
}
