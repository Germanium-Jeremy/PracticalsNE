package com.utility.billing.reading;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MeterReadingRepository extends JpaRepository<MeterReading, Long> {
    
    boolean existsByMeterIdAndMonthAndYear(Long meterId, Integer month, Integer year);

    @Query("SELECT mr FROM MeterReading mr WHERE mr.meter.id = :meterId ORDER BY mr.year DESC, mr.month DESC LIMIT 1")
    Optional<MeterReading> findLatestReadingByMeterId(Long meterId);
}
