package com.utility.billing.billing;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BillRepository extends JpaRepository<Bill, Long> {
    Optional<Bill> findByBillNumber(String billNumber);
    List<Bill> findByCustomerId(Long customerId);
    boolean existsByMeterIdAndBillingMonthAndBillingYear(Long meterId, Integer month, Integer year);
}
