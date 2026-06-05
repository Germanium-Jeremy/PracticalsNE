package com.utility.billing.tariff;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PenaltyConfigurationRepository extends JpaRepository<PenaltyConfiguration, Long> {
    Optional<PenaltyConfiguration> findByActiveTrue();
}
