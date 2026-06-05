package com.utility.billing.tariff;

import com.utility.billing.meter.MeterType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TariffService {

    private final TariffRepository tariffRepository;

    @Transactional
    public Tariff createTariff(MeterType meterType, TariffType tariffType, Double rate) {
        // Deactivate old active tariffs for this meter type
        tariffRepository.findActiveTariffByMeterType(meterType).ifPresent(t -> {
            t.setActive(false);
            tariffRepository.save(t);
        });

        Integer lastVersion = tariffRepository.findMaxVersionByMeterType(meterType);
        int nextVersion = (lastVersion == null) ? 1 : lastVersion + 1;

        Tariff tariff = Tariff.builder()
                .meterType(meterType)
                .tariffType(tariffType)
                .rate(rate)
                .version(nextVersion)
                .effectiveFrom(LocalDateTime.now())
                .active(true)
                .build();

        return tariffRepository.save(tariff);
    }

    public List<Tariff> getAllTariffs() {
        return tariffRepository.findAll();
    }

    public Tariff getActiveTariff(MeterType meterType) {
        return tariffRepository.findActiveTariffByMeterType(meterType)
                .orElseThrow(() -> new RuntimeException("No active tariff found for " + meterType));
    }
}
