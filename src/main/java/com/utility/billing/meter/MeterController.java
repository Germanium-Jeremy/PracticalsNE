package com.utility.billing.meter;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/meters")
public class MeterController {

    private final MeterService meterService;

    public MeterController(MeterService meterService) {
        this.meterService = meterService;
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<MeterResponse> createMeter(@Valid @RequestBody MeterRequest request) {
        return ResponseEntity.ok(meterService.createMeter(request));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'OPERATOR')")
    public ResponseEntity<List<MeterResponse>> getAllMeters() {
        return ResponseEntity.ok(meterService.getAllMeters());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'OPERATOR')")
    public ResponseEntity<MeterResponse> getMeterById(@PathVariable Long id) {
        return ResponseEntity.ok(meterService.getMeterById(id));
    }

    @GetMapping("/customer/{customerId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'OPERATOR') or @securityService.isCustomerOwner(#customerId)")
    public ResponseEntity<List<MeterResponse>> getMetersByCustomer(@PathVariable Long customerId) {
        return ResponseEntity.ok(meterService.getMetersByCustomer(customerId));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<MeterResponse> updateMeter(@PathVariable Long id, @Valid @RequestBody MeterRequest request) {
        return ResponseEntity.ok(meterService.updateMeter(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteMeter(@PathVariable Long id) {
        meterService.deleteMeter(id);
        return ResponseEntity.noContent().build();
    }
}
