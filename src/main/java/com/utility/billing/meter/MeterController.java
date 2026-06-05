package com.utility.billing.meter;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/meters")
@RequiredArgsConstructor
public class MeterController {

    private final MeterService meterService;

    @PostMapping
    public ResponseEntity<MeterResponse> createMeter(@Valid @RequestBody MeterRequest request) {
        return ResponseEntity.ok(meterService.createMeter(request));
    }

    @GetMapping
    public ResponseEntity<List<MeterResponse>> getAllMeters() {
        return ResponseEntity.ok(meterService.getAllMeters());
    }

    @GetMapping("/{id}")
    public ResponseEntity<MeterResponse> getMeterById(@PathVariable Long id) {
        return ResponseEntity.ok(meterService.getMeterById(id));
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<MeterResponse>> getMetersByCustomer(@PathVariable Long customerId) {
        return ResponseEntity.ok(meterService.getMetersByCustomer(customerId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<MeterResponse> updateMeter(@PathVariable Long id, @Valid @RequestBody MeterRequest request) {
        return ResponseEntity.ok(meterService.updateMeter(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMeter(@PathVariable Long id) {
        meterService.deleteMeter(id);
        return ResponseEntity.noContent().build();
    }
}
