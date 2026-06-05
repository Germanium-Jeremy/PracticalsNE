package com.utility.billing.customer;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    Optional<Customer> findByNationalId(String nationalId);
    Optional<Customer> findByUserEmail(String email);
    boolean existsByNationalId(String nationalId);
}
