package com.utility.billing.tariff;

import com.utility.billing.meter.MeterType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TariffService {

    private final TariffRepository tariffRepository;
    private final PenaltyConfigurationRepository penaltyConfigurationRepository;

    public TariffService(TariffRepository tariffRepository, PenaltyConfigurationRepository penaltyConfigurationRepository) {
        this.tariffRepository = tariffRepository;
        this.penaltyConfigurationRepository = penaltyConfigurationRepository;
    }

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

    /**
     * Creates a new penalty configuration. If the new one is active, all others are deactivated.
     * @param request the penalty configuration details
     * @return the saved penalty configuration
     */
    @Transactional
    public PenaltyConfiguration createPenaltyConfiguration(PenaltyConfigurationRequest request) {
        if (request.isActive()) {
            penaltyConfigurationRepository.findByActiveTrue().ifPresent(p -> {
                p.setActive(false);
                penaltyConfigurationRepository.save(p);
            });
        }

        PenaltyConfiguration penalty = PenaltyConfiguration.builder()
                .name(request.getName())
                .fixedAmount(request.getFixedAmount())
                .percentagePerMonth(request.getPercentagePerMonth())
                .active(request.isActive())
                .build();

        return penaltyConfigurationRepository.save(penalty);
    }

    /**
     * Returns all penalty configurations.
     */
    public List<PenaltyConfiguration> getAllPenaltyConfigurations() {
        return penaltyConfigurationRepository.findAll();
    }

    /**
     * Returns the currently active penalty configuration.
     */
    public PenaltyConfiguration getActivePenaltyConfiguration() {
        return penaltyConfigurationRepository.findByActiveTrue()
                .orElseThrow(() -> new RuntimeException("No active penalty configuration found"));
    }
}
