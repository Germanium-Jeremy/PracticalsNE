package com.utility.billing.tariff;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tariffs")
public class TariffController {

    private final TariffService tariffService;

    public TariffController(TariffService tariffService) {
        this.tariffService = tariffService;
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Tariff> createTariff(@Valid @RequestBody TariffRequest request) {
        return ResponseEntity.ok(tariffService.createTariff(request.getMeterType(), request.getTariffType(), request.getRate()));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'FINANCE', 'OPERATOR')")
    public ResponseEntity<List<Tariff>> getAllTariffs() {
        return ResponseEntity.ok(tariffService.getAllTariffs());
    }

    @GetMapping("/active/{meterType}")
    @PreAuthorize("hasAnyRole('ADMIN', 'FINANCE', 'OPERATOR', 'CUSTOMER')")
    public ResponseEntity<Tariff> getActiveTariff(@PathVariable com.utility.billing.meter.MeterType meterType) {
        return ResponseEntity.ok(tariffService.getActiveTariff(meterType));
    }
}
