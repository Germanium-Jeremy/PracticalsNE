package com.utility.billing.tariff;

import com.utility.billing.meter.MeterType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TariffRepository extends JpaRepository<Tariff, Long> {
    
    @Query("SELECT t FROM Tariff t WHERE t.meterType = :meterType AND t.active = true ORDER BY t.version DESC LIMIT 1")
    Optional<Tariff> findActiveTariffByMeterType(MeterType meterType);

    @Query("SELECT MAX(t.version) FROM Tariff t WHERE t.meterType = :meterType")
    Integer findMaxVersionByMeterType(MeterType meterType);
}
